package net.preibisch.intelligentacquisition;

public interface Conduit<D, R>
{
	public void postData(D data);
	public void postResult(R response);
	public void registerDataListener(DataListener<D> analyzer);
	public void registerResultListener(ResultListener<R> listener);
}
