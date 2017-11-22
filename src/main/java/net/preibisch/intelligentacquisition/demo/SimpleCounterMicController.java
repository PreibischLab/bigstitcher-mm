package net.preibisch.intelligentacquisition.demo;

import net.preibisch.intelligentacquisition.AbstractCompoundResultListener;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataAndResultPoster;
import net.preibisch.intelligentacquisition.MicResultHandler;
import net.preibisch.intelligentacquisition.ResultListener;

public class SimpleCounterMicController extends AbstractCompoundResultListener< String > implements DataAndResultPoster< Integer, String > 
{
	private Conduit< ? super Integer, ? super String > conduit;
	private int counter = 0;

	public void runCounter()
	{
		new Thread( new Runnable()
		{
			
			@Override
			public void run()
			{
				while (true)
				{
					// post data and wait a little bit
					conduit.postData( counter++ );
					try{ Thread.sleep( 100 ); } catch ( InterruptedException e ) {}

					// execute all MicResultHandlers
					for (ResultListener< String > child : children)
					{
						if (MicResultHandler.class.isInstance( child ))
							((MicResultHandler< String >)child).execute();
					}

					// sleep some more until next image
					try{ Thread.sleep( 3000 ); } catch ( InterruptedException e ) {}
				}
			}
		} ).start();
		
	}

	@Override
	public void setCondiuit(Conduit< ? super Integer, ? super String > conduit)
	{
		this.conduit = conduit;
	}

	@Override
	public void handleResult(String result) {} // NOP

}
