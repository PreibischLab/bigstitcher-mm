package net.preibisch.intelligentacquisition.imagedemo;

import java.util.List;

import org.scijava.ui.behaviour.util.InputActionBindings;
import org.scijava.ui.behaviour.util.TriggerBehaviourBindings;

import bdv.BigDataViewer;
import bdv.tools.brightness.ConverterSetup;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandle;
import bdv.util.BdvHandleFrame;
import bdv.util.BdvStackSource;
import bdv.viewer.SourceAndConverter;
import ij.IJ;
import net.imglib2.EuclideanSpace;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRandomAccessible;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.realtransform.RealViews;
import net.imglib2.realtransform.Translation2D;
import net.imglib2.realtransform.Translation3D;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class DriftImgGenerator implements EuclideanSpace
{
	private RandomAccessibleInterval< FloatType > img;
	private AffineTransform tr;
	private AffineTransform drift;
	private int ctr;

	public DriftImgGenerator(String path, AffineGet drift)
	{
		ctr = 0;
		img = ImageJFunctions.convertFloat( IJ.openImage( path ) );
		tr = new AffineTransform( img.numDimensions() );
		this.drift = new AffineTransform( img.numDimensions() );
		this.drift = this.drift.copy().preConcatenate( (AffineGet) drift.copy() ).copy();
	}

	public MicDataImpl< Integer > getNextImg()
	{
		AffineRandomAccessible< FloatType, AffineGet > transformed = RealViews.affine(
				Views.interpolate(
						Views.extendMirrorDouble( img ),
						new NLinearInterpolatorFactory<FloatType>() ),
				tr.copy() );
		IntervalView< FloatType > res = Views.interval( Views.raster( transformed ), img);
		return new MicDataImpl<>( res, ctr++ );
	}

	public void applyDrift()
	{
		tr = tr.preConcatenate( drift.copy() ).copy();
	}

	public void applyTransform(AffineGet t)
	{
		tr = tr.preConcatenate( (AffineGet) t.copy() ).copy();
	}

	@Override
	public int numDimensions()
	{
		return img.numDimensions();
	}

	public static void main(String[] args) throws InterruptedException
	{
		DriftImgGenerator dg = new DriftImgGenerator( "/Users/david/Desktop/PollenMovement/pollen.tif", new Translation3D( 100, 0, 0 ) );
		for (int i = 0; i<5; i++)
		{
			MicDataImpl< Integer > dta = dg.getNextImg();
			dg.applyDrift();
			BdvStackSource< ? > bdvS = BdvFunctions.show( dta.getData(), "BDV" );
			bdvS.setDisplayRange( 0, 1000 );

			Thread.sleep( 2000 );

			bdvS.getBdvHandle().close();
		}
	}
}
