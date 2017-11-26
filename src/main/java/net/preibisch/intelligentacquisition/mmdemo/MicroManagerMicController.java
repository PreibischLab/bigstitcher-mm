package net.preibisch.intelligentacquisition.mmdemo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.micromanager.acquisition.MMAcquisition;
import org.micromanager.api.MMTags;
import org.micromanager.api.ScriptInterface;
import org.micromanager.api.TaggedImageAnalyzer;
import org.micromanager.utils.MMScriptException;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import ij.IJ;
import mmcorej.TaggedImage;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.multithreading.SimpleMultiThreading;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.util.Util;
import net.preibisch.intelligentacquisition.AbstractCompoundResultListener;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataAndResultPoster;
import net.preibisch.intelligentacquisition.MicResultHandler;
import net.preibisch.intelligentacquisition.ResultListener;
import net.preibisch.intelligentacquisition.imagedemo.MicDataImpl;

public class MicroManagerMicController extends AbstractCompoundResultListener< AffineGet > implements DataAndResultPoster< MicDataImpl< Integer >, AffineGet > 
{
	private Conduit< ? super MicDataImpl< Integer >, ? super AffineGet > conduit;
	private final ScriptInterface mm;
	private final Set<String> acquisitionNames;
	private String newestAcquisitionName;
	private TaggedImageAnalyzer imagePipelineCallback;

	// hacky, change key to a nice class maybe?
	private Map<List<Integer>, List<TaggedImage>> stackMap;


	public MicroManagerMicController(ScriptInterface mm)
	{
		this.mm = mm;
		stackMap = new HashMap<>();
		acquisitionNames = new HashSet<>();
		updateAcquisitionNames();

		imagePipelineCallback = new TaggedImageAnalyzer()
		{
			private int counter = 0;
			private int sliceCurrent = -1;
			private int channelCurrent = -1;
			private int positionCurrent = -1;
			private int frameCurrent = -1;
			private int slicesTotal = -1;
			private int framesTotal = -1;
			private int channelsTotal = -1;
			private int positionsTotal = -1;

			@Override
			protected void analyze(TaggedImage img)
			{

				// get the newest acquisition name in case a new one was started
				final String oldAcq = MicroManagerMicController.this.newestAcquisitionName;
				MicroManagerMicController.this.updateAcquisitionNames();
				if (!MicroManagerMicController.this.newestAcquisitionName.equals( oldAcq ))
				{
					IJ.log( "Started new acquisition " + MicroManagerMicController.this.newestAcquisitionName );

					// clear references to old image
					stackMap.clear();

					// get dimensions of new acquisition
					JSONObject summaryMetadata = MicroManagerMicController.this.mm.getAcquisitionEngine2010().getSummaryMetadata();
					try
					{
						channelsTotal = ((Long)summaryMetadata.get( MMTags.Summary.CHANNELS )).intValue();
						framesTotal = ((Long)summaryMetadata.get( MMTags.Summary.FRAMES )).intValue();
						slicesTotal = ((Long)summaryMetadata.get( MMTags.Summary.SLICES )).intValue();
						positionsTotal = ((Long)summaryMetadata.get( MMTags.Summary.POSITIONS )).intValue();
					}
					catch ( JSONException e ){ e.printStackTrace(); }

				}

				// NB: after acquisition finishes, we get an image with pix==null & tags==null
				if (img.pix == null)
				{
					sliceCurrent = -1;
					channelCurrent = -1;
					positionCurrent = -1;

					return;
				}

				// update current img indices
				try
				{
					// NB: the != null check may not be necessary here
					sliceCurrent =  (int) (img.tags.get( MMTags.Image.SLICE_INDEX ) != null ? (Long) img.tags.get( MMTags.Image.SLICE_INDEX ) : -1);
					channelCurrent =  ((Long) img.tags.get( MMTags.Image.CHANNEL_INDEX )).intValue();
					positionCurrent =  ((Long) img.tags.get( MMTags.Image.POS_INDEX )).intValue();
					frameCurrent =  ((Long) img.tags.get( MMTags.Image.FRAME_INDEX )).intValue();
				}
				catch ( NumberFormatException | JSONException e )
				{
					e.printStackTrace();
				}
				
				IJ.log( newestAcquisitionName + ": " + sliceCurrent + "/" + slicesTotal );

				// key to stack map = (ch, pos, fr)
				List< Integer > key = Arrays.asList( new Integer[] {channelCurrent, positionCurrent, frameCurrent} );

				// insert current img into map
				if (!stackMap.containsKey( key ))
					stackMap.put( key, new ArrayList<TaggedImage>() );
				stackMap.get( key ).add( img );

				// we are at the end of a stack -> post data to conduit and execute pending stage movements
				if (sliceCurrent == (slicesTotal - 1) )
				{
					List< TaggedImage > imgs = stackMap.get( key );
					RandomAccessibleInterval< ? > wrappedImg = MicroManagerUtils.wrapTaggedImageList( imgs );
					conduit.postData( new MicDataImpl< Integer >( wrappedImg, counter++ ) );

					// execute all MicResultHandlers
					for (ResultListener< AffineGet > child : children)
					{
						if (MicResultHandler.class.isInstance( child ))
							((MicResultHandler< AffineGet >)child).execute();
					}

				}

			}
		};

		MicroManagerMicController.this.mm.addImageProcessor( imagePipelineCallback );

	}

	// (hacky) update acquisition names
	// the most recent acquisition is set (may be null if there a no acqs yet)
	// if there are multiple new acquisitions, one of them is set as the newest.
	public void updateAcquisitionNames()
	{
		SetView< String > newAcqs = Sets.difference( new HashSet<>( Arrays.asList( mm.getAcquisitionNames() ) ),
				acquisitionNames );
		if ( newAcqs.size() >= 1 )
			newestAcquisitionName = newAcqs.iterator().next();
		acquisitionNames.addAll( newAcqs );
	}

	@Override
	public void setCondiuit(Conduit< ? super MicDataImpl< Integer >, ? super AffineGet > conduit)
	{
		this.conduit = conduit;
	}

	@Override
	public <RS extends AffineGet> void handleResult(RS result)
	{
		System.out.println( Util.printCoordinates( result.getRowPackedCopy() ) );
	}

}
