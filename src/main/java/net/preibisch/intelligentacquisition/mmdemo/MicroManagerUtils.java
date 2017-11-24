package net.preibisch.intelligentacquisition.mmdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.micromanager.acquisition.MMAcquisition;
import org.micromanager.api.MMTags;
import org.micromanager.utils.MMScriptException;

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

	// FIXME: this only works if acquisition was initiated via code?
	public static RandomAccessibleInterval< ? > wrapMMAcquisition(MMAcquisition acquisition, int frame, int channel, int position)
	{
		final int byteDepth = acquisition.getByteDepth();

		long width = 0;
		long height = 0;
		long slices = 0;


		// we have to read dimensions from metadata? .getHeight()/... seems to return 0
		try
		{
			width = (Long) acquisition.getSummaryMetadata().get( MMTags.Summary.WIDTH );
			height = (Long) acquisition.getSummaryMetadata().get( MMTags.Summary.HEIGHT );
			slices = (Long) acquisition.getSummaryMetadata().get( MMTags.Summary.SLICES );
		}
		catch ( JSONException e ){ e.printStackTrace(); }

		final long[] dim = new long[]{ width, height, slices };

		final RandomAccessibleInterval< ? > res;
		if (byteDepth == 1)
		{
			res =  new PlanarImg< UnsignedByteType, ByteArray >( dim, new UnsignedByteType().getEntitiesPerPixel() );
			final UnsignedByteType linkedType = new UnsignedByteType( (NativeImg< ?, ? extends ByteAccess >) res );
			( (AbstractNativeImg< UnsignedByteType, ByteArray >) res ).setLinkedType( linkedType );
		}
		else if (byteDepth == 2)
		{
			res = (RandomAccessibleInterval< ? >) new PlanarImg< UnsignedShortType, ShortArray >( dim, new UnsignedShortType().getEntitiesPerPixel() );
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

	public static RandomAccessibleInterval< ? > wrapTaggedImageList(List<TaggedImage> imgs)
	{
		ArrayList< TaggedImage > imgsCpy = new ArrayList<>(imgs);
		Collections.sort( imgsCpy, new Comparator< TaggedImage >()
		{

			@Override
			public int compare(TaggedImage o1, TaggedImage o2)
			{
				try
				{
					int slice1 = ((Long) o1.tags.get( MMTags.Image.SLICE_INDEX )).intValue();
					int slice2 = ((Long) o2.tags.get( MMTags.Image.SLICE_INDEX )).intValue();
					return slice1 - slice2;
				}
				catch ( JSONException e ){ e.printStackTrace();}
				return 0;
			}
		} );
		
		long bitDepth = 0;
		long width = 0;
		long height = 0;
		try
		{
			width = (Long) imgsCpy.iterator().next().tags.get( MMTags.Image.WIDTH );
			height = (Long) imgsCpy.iterator().next().tags.get( MMTags.Image.HEIGHT );
			bitDepth = (Long) imgsCpy.iterator().next().tags.get( MMTags.Image.BIT_DEPTH );
		}
		catch ( JSONException e ){ e.printStackTrace();}
		
		final long[] dim = new long[]{ width, height, imgsCpy.size() };

		final RandomAccessibleInterval< ? > res;
		if (bitDepth == 8)
		{
			res =  new PlanarImg< UnsignedByteType, ByteArray >( dim, new UnsignedByteType().getEntitiesPerPixel() );
			final UnsignedByteType linkedType = new UnsignedByteType( (NativeImg< ?, ? extends ByteAccess >) res );
			( (AbstractNativeImg< UnsignedByteType, ByteArray >) res ).setLinkedType( linkedType );
		}
		else if (bitDepth == 16)
		{
			res = (RandomAccessibleInterval< ? >) new PlanarImg< UnsignedShortType, ShortArray >( dim, new UnsignedShortType().getEntitiesPerPixel() );
			final UnsignedShortType linkedType = new UnsignedShortType( (NativeImg< ?, ? extends ShortAccess >) res );
			( (AbstractNativeImg< UnsignedShortType, ShortAccess >) res ).setLinkedType( linkedType );
		}
		else
			throw new IllegalArgumentException( "can only wrap 8-/16-bit unsigned images." );
		
		for ( int z = 0; z < imgsCpy.size(); z++ )
		{
			// channel, slice, frame, position
			TaggedImage image = imgsCpy.get( z );

			if (bitDepth == 8)
			{
				byte[] pixelsPlane = (byte[])image.pix;
				( (PlanarImg< UnsignedByteType, ByteArray >) res ).setPlane( z, new ByteArray( pixelsPlane ) );
			}
			else if (bitDepth == 16)
			{
				short[] pixelsPlane = (short[])image.pix;
				( (PlanarImg< UnsignedShortType, ShortArray >) res ).setPlane( z, new ShortArray( pixelsPlane ) );
			}
		}

		return res;
		
	}
}
