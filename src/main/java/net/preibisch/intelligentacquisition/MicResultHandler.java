package net.preibisch.intelligentacquisition;

public interface MicResultHandler<R> extends ResultListener< R >, ResultPoster< R >
{
	public void execute();
}
