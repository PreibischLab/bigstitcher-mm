package net.preibisch.stitcher.mm;

import org.json.JSONObject;
import org.micromanager.MMOptions;
import org.micromanager.MMStudio;
import org.micromanager.api.ImageCache;

import ij.IJ;
import ij.ImageJ;
import mmcorej.CMMCore;
import mmcorej.TaggedImage;
import mpicbg.spim.io.IOFunctions;
import net.preibisch.stitcher.plugin.BigStitcher;

public class MMTest
{
	public static void main(String[] args)
	{
		new ImageJ();
		new BigStitcher().run();
		
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
		
		try
		{
			doAcquisition( mm, core );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doAcquisition(MMStudio gui, CMMCore mmc) throws Exception
	{
		gui.closeAllAcquisitions();
		gui.clearMessageWindow();
		
		// use DCam for now
		mmc.setProperty( "Core", "Camera", "DCam" );
		// mmc.setProperty("Core", "Camera", "FakeCamera");

		int nrFrames = 3;
		int nrSlices = 60;
		String acqName = gui.getUniqueAcquisitionName( "test" );
		String rootDirName = "";
		String fakeRoot = "/Users/david/Desktop/PollenMovement/t";
		String fakeFile = "/pollen-??.tif";

		gui.openAcquisition( acqName, rootDirName, nrFrames, 1, nrSlices );

		for ( int f = 0; f < nrFrames; f++ )
		{
			String fakePath = fakeRoot + ( f + 1 ) + fakeFile;
			gui.message( fakePath );
			mmc.setProperty( "FakeCamera", "Path mask", fakePath );
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
			// channel, slice, frame, position
			TaggedImage img = imgCache.getImage( 0, 5, f, 0 );
			
			Object pixels = img.pix;
			JSONObject tags = img.tags; // JSONObject

			// Note that a listener can be attached to an ImageCache
			// Note: access to images in 1.4 is through an "Acquisition object".
			// This is an ill-defined
			// structure (contained in the internal MMAcquisition, and access is
			// only by name
			// 2.0 uses a Datastore to which images are added (and images are
			// retrieved from a DataProvider)
			// in 2.0, objects from the class Images contain information about
			// width, height, etc..

		}

		
	}
}
