package net.preibisch.intelligentacquisition;

public interface ResultPoster<R>
{
	public void setCondiuit(Conduit< ?, ? super R > conduit);
}
