package net.preibisch.intelligentacquisition.imagedemo;

import net.imglib2.EuclideanSpace;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.Translation2D;
import net.imglib2.realtransform.Translation3D;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.ResultListener;
import net.preibisch.intelligentacquisition.demo.SimpleConduitImpl;

public class ImageTest
{
	public static void main(String[] args)
	{
		final Conduit< MicDataImpl< Integer >, EuclideanSpace > conduit = new SimpleConduitImpl<>();
		final DriftImgMicController micController = 
				new DriftImgMicController( "/Users/david/Desktop/PollenMovement/pollen2d.tif", new Translation2D( 100, 0 ), 100, 500 );
		final DriftImgMicResultHandler micResultHandler = new DriftImgMicResultHandler( micController.getImageGenerator() );
		micController.addChild( micResultHandler );
		micController.setCondiuit( conduit );
		micResultHandler.setConduit( conduit );
		conduit.registerResultListener( micController );
		final DriftImgDataCollector dataCollector = new DriftImgDataCollector();
		final DriftImgDisplayer displayer = new DriftImgDisplayer();
		final DriftImgCorrector corrector = new DriftImgCorrector();
		dataCollector.addChild( displayer );
		dataCollector.addChild( corrector );
		corrector.setConduit( conduit );
		conduit.registerDataListener( dataCollector );
		
		conduit.registerResultListener( new ResultListener< EuclideanSpace >()
		{

			@Override
			public <RS extends EuclideanSpace> void notifyWithResult(RS result)
			{
				// TODO Auto-generated method stub
				
			}
		} );

		micController.runAcq();
		
	}
}
