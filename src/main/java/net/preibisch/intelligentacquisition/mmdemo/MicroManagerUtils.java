package net.preibisch.intelligentacquisition.mmdemo;

import org.json.JSONObject;
import org.micromanager.acquisition.MMAcquisition;

import mmcorej.TaggedImage;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.AbstractNativeImg;
import net.imglib2.img.NativeImg;
import net.imglib2.img.basictypeaccess.ByteAccess;
import net.imglib2.img.basictypeaccess.ShortAccess;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class MicroManagerUtils
{

	public static <T extends RealType<T> & NativeType<T>> RandomAccessibleInterval< T > wrapMMAcquisition(MMAcquisition acquisition, int frame, int channel, int position)
	{
		final long[] dim = new long[]{ acquisition.getWidth(), acquisition.getHeight(), acquisition.getSlices() };
		final int byteDepth = acquisition.getByteDepth();

		final RandomAccessibleInterval< T > res;
		if (byteDepth == 1)
		{
			res = (RandomAccessibleInterval< T >) new PlanarImg< UnsignedByteType, ByteArray >( dim, new UnsignedByteType().getEntitiesPerPixel() );
			final UnsignedByteType linkedType = new UnsignedByteType( (NativeImg< ?, ? extends ByteAccess >) res );
			( (AbstractNativeImg< UnsignedByteType, ByteArray >) res ).setLinkedType( linkedType );
		}
		else if (byteDepth == 2)
		{
			res = (RandomAccessibleInterval< T >) new PlanarImg< UnsignedShortType, ShortArray >( dim, new UnsignedShortType().getEntitiesPerPixel() );
			final UnsignedShortType linkedType = new UnsignedShortType( (NativeImg< ?, ? extends ShortAccess >) res );
			( (AbstractNativeImg< UnsignedShortType, ShortAccess >) res ).setLinkedType( linkedType );
		}
		else
			throw new IllegalArgumentException( "can only wrap 8-/16-bit unsigned images." );

		for ( int z = 0; z < acquisition.getSlices(); z++ )
		{
			// channel, slice, frame, position
			TaggedImage image = acquisition.getImageCache().getImage( channel, z, frame, position );

			if (byteDepth == 1)
			{
				byte[] pixelsPlane = (byte[])image.pix;
				( (PlanarImg< UnsignedByteType, ByteArray >) res ).setPlane( z, new ByteArray( pixelsPlane ) );
			}
			else if (byteDepth == 2)
			{
				short[] pixelsPlane = (short[])image.pix;
				( (PlanarImg< UnsignedShortType, ShortArray >) res ).setPlane( z, new ShortArray( pixelsPlane ) );
			}
		}

		return res;
	}
}
