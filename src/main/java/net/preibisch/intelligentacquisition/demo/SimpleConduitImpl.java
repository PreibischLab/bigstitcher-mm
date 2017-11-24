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
	public synchronized <DS extends D> void postData(DS data)
	{
		System.out.println( "Data posted" );
		for (DataListener< D > listener : dataListeners)
			listener.notifyWithData( data );
	}

	@Override
	public synchronized <RS extends R> void postResult(RS response)
	{
		for(ResultListener< R > listener : resultListeners)
			listener.notifyWithResult( response );
	}

	@Override
	public synchronized <DS extends D> void registerDataListener(DataListener< DS > analyzer)
	{
		dataListeners.add( (DataListener< D >) analyzer );
	}

	@Override
	public synchronized <RS extends R> void registerResultListener(ResultListener< RS > listener)
	{
		resultListeners.add( (ResultListener< R >) listener );
	}

}
