package net.preibisch.intelligentacquisition;

public interface DataChainable<I, O> extends DataListener< I >
{
	public void addChild(DataListener< O > listener);
}
