package net.preibisch.intelligentacquisition.demo;

import java.util.ArrayList;

import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.ResultListener;

public class SimpleConduitImpl< D, R > implements Conduit< D, R >
{
	private ArrayList< DataListener< D > > dataListeners;
	private ArrayList< ResultListener< R > > resultListeners;

	public SimpleConduitImpl()
	{
		dataListeners = new ArrayList<>();
		resultListeners = new ArrayList<>();
	}

	@Override
	public void postData(D data)
	{
		for (DataListener< D > listener : dataListeners)
			listener.notifyWithData( data );
	}

	@Override
	public void postResult(R response)
	{
		for(ResultListener< R > listener : resultListeners)
			listener.notifyWithResult( response );
	}

	@Override
	public void registerDataListener(DataListener< D > analyzer)
	{
		dataListeners.add( analyzer );
	}

	@Override
	public void registerResultListener(ResultListener< R > listener)
	{
		resultListeners.add( listener );
	}

}
