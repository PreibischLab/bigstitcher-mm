package net.preibisch.intelligentacquisition.mmdemo;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.micromanager.acquisition.MMAcquisition;
import org.micromanager.api.MMTags;
import org.micromanager.api.ScriptInterface;
import org.micromanager.api.TaggedImageAnalyzer;
import org.micromanager.utils.MMScriptException;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import ij.IJ;
import mmcorej.TaggedImage;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.util.Util;
import net.preibisch.intelligentacquisition.AbstractCompoundResultListener;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataAndResultPoster;
import net.preibisch.intelligentacquisition.imagedemo.MicDataImpl;

public class MicroManagerMicController extends AbstractCompoundResultListener< AffineGet > implements DataAndResultPoster< MicDataImpl< Integer >, AffineGet > 
{
	private Conduit< ? super MicDataImpl< Integer >, ? super AffineGet > conduit;
	private final ScriptInterface mm;
	private final Set<String> acquisitionNames;
	private String newestAcquisitionName;
	private TaggedImageAnalyzer imagePipelineCallback;
	
	// (hacky) update acquisition names
	// the most recent acquisition is set (may be null if there a no acqs yet)
	// if there are multiple new acquisitions, one of them is set as the newest. 
	public void updateAcquisitionNames()
	{
		SetView< String > newAcqs = Sets.difference( new HashSet<>(Arrays.asList( mm.getAcquisitionNames() )), acquisitionNames );
		if (newAcqs.size() >= 1)
			newestAcquisitionName = newAcqs.iterator().next();
		acquisitionNames.addAll( newAcqs );
	}

	public MicroManagerMicController(ScriptInterface mm)
	{
		this.mm = mm;
		acquisitionNames = new HashSet<>();
		updateAcquisitionNames();

		this.mm.attachRunnable( 0, 0, 0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				MicroManagerMicController.this.updateAcquisitionNames();
				System.out.println( MicroManagerMicController.this.newestAcquisitionName );
			}
		} );
		
		imagePipelineCallback = new TaggedImageAnalyzer()
		{
			@Override
			protected void analyze(TaggedImage img)
			{
				MMAcquisition acquisition = null;
				try
				{
					acquisition = MicroManagerMicController.this.mm.getAcquisition(newestAcquisitionName);
				}
				catch ( MMScriptException e )
				{
					e.printStackTrace();
				}
				
				long totalSlices = acquisition.getSlices();
				long sliceCurrent = -1;
				try
				{
					sliceCurrent =  (Long) img.tags.get( MMTags.Image.SLICE_INDEX );
				}
				catch ( NumberFormatException | JSONException e )
				{
					e.printStackTrace();
				}

				IJ.log( Long.toString( sliceCurrent ) );
			}
		};
		mm.addImageProcessor( imagePipelineCallback );
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
