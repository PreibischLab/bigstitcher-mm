package net.preibisch.intelligentacquisition;

public interface DataListener<D>
{
	public void notifyWithData(D data);
}
