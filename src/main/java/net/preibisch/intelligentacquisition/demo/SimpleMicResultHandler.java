package net.preibisch.intelligentacquisition.demo;

import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.MicResultHandler;

public class SimpleMicResultHandler implements MicResultHandler< String >
{
	private String currentResult;

	@Override
	public void notifyWithResult(String result)
	{
		this.currentResult = result;
	}

	@Override
	public void execute()
	{
		System.out.println( this.getClass().getSimpleName() + ": executed." );
	}

	@Override
	public void setCondiuit(Conduit< ?, ? super String > conduit)
	{
		// TODO Auto-generated method stub
		
	}

}
