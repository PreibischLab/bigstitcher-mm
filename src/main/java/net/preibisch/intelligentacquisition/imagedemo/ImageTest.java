package net.preibisch.intelligentacquisition.imagedemo;

import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.Translation2D;
import net.imglib2.realtransform.Translation3D;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.demo.SimpleConduitImpl;

public class ImageTest
{
	public static void main(String[] args)
	{
		final Conduit< MicDataImpl< Integer >, AffineGet > conduit = new SimpleConduitImpl<>();
		final DriftImgMicController micController = 
				new DriftImgMicController( "/Users/david/Desktop/PollenMovement/pollen2d.tif", new Translation2D( 100, 0 ), 2000, 1000 );
		final DriftImgMicResultHandler micResultHandler = new DriftImgMicResultHandler( micController.getImageGenerator() );
		micController.addChild( micResultHandler );
		micController.setCondiuit( conduit );
		micResultHandler.setCondiuit( conduit );
		conduit.registerResultListener( micController );
		final DriftImgDataCollector dataCollector = new DriftImgDataCollector();
		final DriftImgDisplayer displayer = new DriftImgDisplayer();
		final DriftImgCorrector corrector = new DriftImgCorrector();
		dataCollector.addChild( displayer );
		//dataCollector.addChild( corrector );
		corrector.setCondiuit( conduit );
		conduit.registerDataListener( dataCollector );

		micController.runAcq();
		
	}
}
