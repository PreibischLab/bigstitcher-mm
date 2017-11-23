package net.preibisch.intelligentacquisition;

public interface ResultListener<R>
{
	public <RS extends R> void notifyWithResult(RS result);
}
