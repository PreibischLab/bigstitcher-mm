package net.preibisch.intelligentacquisition;

public interface Conduit<D, R>
{
	public <DS extends D> void postData(DS data);
	public <RS extends R> void postResult(RS response);
	public <DS extends D> void registerDataListener(DataListener< DS > analyzer);
	public <RS extends R> void registerResultListener(ResultListener< RS > listener);
}
