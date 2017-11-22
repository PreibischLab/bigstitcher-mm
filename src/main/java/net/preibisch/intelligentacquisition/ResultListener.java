package net.preibisch.intelligentacquisition;

public interface ResultListener<R>
{
	public void notifyWithResult(R result);
}
