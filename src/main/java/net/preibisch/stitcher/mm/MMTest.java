package net.preibisch.stitcher.mm;

import java.util.Iterator;

import org.json.JSONObject;
import org.micromanager.MMOptions;
import org.micromanager.MMStudio;
import org.micromanager.api.ImageCache;

import bdv.util.BdvFunctions;
import ij.IJ;
import ij.ImageJ;
import mmcorej.CMMCore;
import mmcorej.TaggedImage;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImg;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.multithreading.SimpleMultiThreading;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.preibisch.intelligentacquisition.demo.SimpleConduitImpl;
import net.preibisch.intelligentacquisition.imagedemo.DriftImgCorrector;
import net.preibisch.intelligentacquisition.imagedemo.DriftImgDataCollector;
import net.preibisch.intelligentacquisition.imagedemo.DriftImgDisplayer;
import net.preibisch.intelligentacquisition.imagedemo.MicDataImpl;
import net.preibisch.intelligentacquisition.mmdemo.MicroManagerDriftResultHandler;
import net.preibisch.intelligentacquisition.mmdemo.MicroManagerMicController;
import net.preibisch.intelligentacquisition.mmdemo.MicroManagerUtils;
import net.preibisch.stitcher.plugin.BigStitcher;

public class MMTest
{
	public static void main(String[] args)
	{
		new ImageJ();
		//new BigStitcher().run();

		boolean runScript = false;

		final MMOptions options = new MMOptions();
		options.loadSettings();
		options.doNotAskForConfigFile_ = true;
		options.startupScript_ = runScript ? MMTest.class.getResource( "/demoAcquisition_MM14.bsh" ).getFile() : "";
		options.saveSettings();

		MMStudio mm = new MMStudio( false );
		IJ.log( mm.getSysConfigFile() );
		mm.setSysConfigFile( MMTest.class.getResource( "/testConfig.cfg" ).getFile() );
		mm.setConfigChanged( true );

		CMMCore core = mm.getCore();

		MicroManagerMicController controller = new MicroManagerMicController( mm );
		SimpleConduitImpl< MicDataImpl< Integer >, Object > conduit = new SimpleConduitImpl<MicDataImpl< Integer >, Object>();
		controller.setCondiuit( conduit );
		controller.addChild( new MicroManagerDriftResultHandler( core ) );

		conduit.registerResultListener( controller );
		final DriftImgDataCollector dataCollector = new DriftImgDataCollector();
		final DriftImgDisplayer displayer = new DriftImgDisplayer();
		final DriftImgCorrector corrector = new DriftImgCorrector();
		dataCollector.addChild( displayer );
		dataCollector.addChild( corrector );
		corrector.setConduit( conduit );
		conduit.registerDataListener( dataCollector );

		/*
		try
		{
			doAcquisition( mm, core, false );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public static void doAcquisition( final MMStudio gui, final CMMCore mmc, final boolean fromDisk ) throws Exception
	{
		gui.closeAllAcquisitions();
		gui.clearMessageWindow();


		if ( fromDisk )
			mmc.setProperty("Core", "Camera", "FakeCamera");
		else
			mmc.setProperty( "Core", "Camera", "DCam" );

		int nrFrames = 3;
		int nrSlices = 60;
		String acqName = gui.getUniqueAcquisitionName( "test" );
		String rootDirName = "";
		String fakeRoot = "/Users/david/Desktop/PollenMovement/t";
		String fakeFile = "/pollen-??.tif";

		gui.openAcquisition( acqName, rootDirName, nrFrames, 1, nrSlices );

		//gui.runAcquisition();

		for ( int f = 0; f < nrFrames; f++ )
		{
			if ( fromDisk )
			{
				String fakePath = fakeRoot + ( f + 1 ) + fakeFile;
				gui.message( fakePath );
				mmc.setProperty( "FakeCamera", "Path mask", fakePath );
			}

			for ( int z = 0; z < nrSlices; z++ )
			{
				gui.setStagePosition( z );
				gui.snapAndAddImage( acqName, f, 0, z, 0 );
			}
			// A full Z-stack is now available
			// get images as follows:

			// only valid after images were added to the acquisition
			int width = gui.getAcquisitionImageHeight( acqName );			
			int height = gui.getAcquisitionImageHeight( acqName );
			int nrBytes = gui.getAcquisitionImageByteDepth( acqName );
			ImageCache imgCache = gui.getAcquisitionImageCache( acqName );

			System.out.println( "w=" + width );
			System.out.println( "h=" + height );
			System.out.println( "bytes=" + nrBytes );

			RandomAccessibleInterval< ?  > mmImg = MicroManagerUtils.wrapMMAcquisition( gui.getAcquisitionWithName( acqName ), f, 0, 0 );
			//BdvFunctions.show( mmImg, "BDV" );

			/*
			// channel, slice, frame, position
			TaggedImage img = imgCache.getImage( 0, 5, f, 0 );


			Object pixels = img.pix;
			JSONObject tags = img.tags; // JSONObject

			if ( pixels instanceof byte[] )
			{
				System.out.println( ( (byte[]) pixels ).length );
			}
			else if ( pixels instanceof short[] )
			{
			}
			else
			{
				throw new RuntimeException( "Don't know how to handle images of pixel type: " + pixels.getClass().getSimpleName() );
			}

			final Iterator< String > keys = tags.keys();
			while ( keys.hasNext() )
			{
				String key = keys.next();
				System.out.println( key + ": " + tags.get( key ) );
			}

			System.out.println( pixels.getClass().getSimpleName() );

			// Note that a listener can be attached to an ImageCache
			// Note: access to images in 1.4 is through an "Acquisition object".
			// This is an ill-defined
			// structure (contained in the internal MMAcquisition, and access is
			// only by name
			// 2.0 uses a Datastore to which images are added (and images are
			// retrieved from a DataProvider)
			// in 2.0, objects from the class Images contain information about
			// width, height, etc..
			 */
			//SimpleMultiThreading.threadHaltUnClean();
		}

		
	}
}
