package net.preibisch.intelligentacquisition;

public interface DataListener<D>
{
	public <DS extends D> void notifyWithData(DS data);
}
