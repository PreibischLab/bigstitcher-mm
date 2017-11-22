package net.preibisch.intelligentacquisition.imagedemo;

import java.util.List;
import java.util.concurrent.Executors;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.Translation;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;
import net.imglib2.util.Util;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.ResultPoster;
import net.preibisch.stitcher.algorithm.PairwiseStitching;
import net.preibisch.stitcher.algorithm.PairwiseStitchingParameters;
import net.preibisch.stitcher.algorithm.TransformTools;
import net.preibisch.stitcher.algorithm.globalopt.TransformationTools;

public class DriftImgCorrector implements DataListener< Pair<MicDataImpl< Integer >, ? extends List<MicDataImpl< Integer > > > >, ResultPoster< AffineGet >
{

	private Conduit< ?, ? super AffineGet > conduit;

	@Override
	public void setCondiuit(Conduit< ?, ? super AffineGet > conduit)
	{
		this.conduit = conduit;
	}

	@Override
	public void notifyWithData(Pair< MicDataImpl< Integer >, ? extends List< MicDataImpl< Integer > > > data)
	{
		PairwiseStitchingParameters params = new PairwiseStitchingParameters( 0, 3, false, false );
		// not enough data yet
		if (data.getB().size() < 2)
			return;
		MicDataImpl< Integer > md1 = data.getB().get( data.getB().size() - 2 );
		MicDataImpl< Integer > md2 = data.getB().get( data.getB().size() - 1 );

		RandomAccessibleInterval< RealType > img1 = (RandomAccessibleInterval< RealType >) md1.getData();
		RandomAccessibleInterval< RealType > img2 = (RandomAccessibleInterval< RealType >) md2.getData();

		Pair< Translation, Double > res = 
				PairwiseStitching.getShift( 
						img1, img2,
						new Translation( img1.numDimensions() ), new Translation( img2.numDimensions() ),
						params, Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() ) );
		System.out.println( Util.printCoordinates( res.getA().copy().getTranslationCopy() ) + ": " + res.getB() );

		conduit.postResult( res.getA().copy() );

	}

}
