package net.preibisch.intelligentacquisition.imagedemo;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;
import net.preibisch.intelligentacquisition.MicData;

public class MicDataImpl<ID> implements MicData< ID >
{
	private RandomAccessibleInterval< ? > rai;
	private ID id;
	
	public MicDataImpl(RandomAccessibleInterval< ? > rai, ID id)
	{
		this.rai = rai;
		this.id = id;
	}

	@Override
	public ID getID() { return id; }

	@Override
	public RandomAccessibleInterval< ? > getData() { return rai; }

	@Override
	public Object getType() {
		return Views.iterable( rai ).firstElement();
	}

}
