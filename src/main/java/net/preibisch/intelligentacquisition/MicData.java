package net.preibisch.intelligentacquisition;

import net.imglib2.RandomAccessibleInterval;

public interface MicData <ID>
{
	public ID getID();
	public RandomAccessibleInterval< ? > getData();
	public Object getType();
}
