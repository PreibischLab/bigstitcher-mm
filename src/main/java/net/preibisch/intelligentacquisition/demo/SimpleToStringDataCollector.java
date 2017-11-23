package net.preibisch.intelligentacquisition.demo;

import java.util.ArrayList;

import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.DataTransformer;

public class SimpleToStringDataCollector<D> implements DataTransformer< D, Pair<D, String> >
{
	private ArrayList< DataListener< Pair< D, String > > > listeners;


	public SimpleToStringDataCollector()
	{
		listeners = new ArrayList<>();
	}
	
	@Override
	public <DS extends D> void notifyWithData(DS data)
	{
		Pair< D, String > transformed = new ValuePair<>( (D) data, data.toString() );
		System.out.println( this.getClass().getSimpleName() + ": transformed data (type " + data.getClass().getSimpleName() + ") to String." );
		for (DataListener< Pair< D, String > > listener : listeners)
			listener.notifyWithData( transformed );
	}

	@Override
	public void addChild(DataListener< Pair< D, String > > listener)
	{
		listeners.add( listener );
	}

}
