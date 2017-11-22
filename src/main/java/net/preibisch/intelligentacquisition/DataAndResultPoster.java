package net.preibisch.intelligentacquisition;

public interface DataAndResultPoster<D, R> 
{
	public void setCondiuit(Conduit< ? super D, ? super R > conduit);
}
