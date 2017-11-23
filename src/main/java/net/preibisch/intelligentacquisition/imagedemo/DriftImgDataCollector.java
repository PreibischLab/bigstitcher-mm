package net.preibisch.intelligentacquisition.imagedemo;

import java.util.ArrayList;
import java.util.List;

import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.DataTransformer;

public class DriftImgDataCollector implements DataTransformer< MicDataImpl< Integer >, Pair<MicDataImpl< Integer >, ? extends List<MicDataImpl< Integer > > > >
{

	private ArrayList<DataListener< Pair< MicDataImpl< Integer >, ? extends List< MicDataImpl< Integer > > > > > listeners;
	private ArrayList<MicDataImpl< Integer >> imgs;

	public DriftImgDataCollector()
	{
		listeners = new ArrayList<>();
		imgs = new ArrayList<>();
	}

	@Override
	public <D extends MicDataImpl< Integer >> void notifyWithData(D data)
	{
		imgs.add( data );
		System.out.println( "Collected data, in list: " + imgs.size() );
		ValuePair<MicDataImpl< Integer >, ArrayList< MicDataImpl< Integer > > > res = new ValuePair<>( (MicDataImpl< Integer >) data, imgs );
		for (DataListener< Pair< MicDataImpl< Integer >, ? extends List< MicDataImpl< Integer > > > > listener : listeners)
			listener.notifyWithData( res );
	}

	@Override
	public void addChild(DataListener< Pair< MicDataImpl< Integer >, ? extends List< MicDataImpl< Integer > > > > listener)
	{
		listeners.add( listener );
	}

}
