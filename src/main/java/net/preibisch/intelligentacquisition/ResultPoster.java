package net.preibisch.intelligentacquisition;

public interface ResultPoster<R>
{
	public void setConduit(Conduit< ?, ? super R > conduit);
}
