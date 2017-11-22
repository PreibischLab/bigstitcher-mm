package net.preibisch.intelligentacquisition.demo;

import net.imglib2.util.Pair;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.ResultPoster;

public class SimplePrintDataListener implements DataListener< Pair<Integer, String> >, ResultPoster< String >
{

	private Conduit< ?, ? super String > conduit;

	@Override
	public void notifyWithData(Pair< Integer, String > data)
	{
		System.out.println( data.getB() );
		conduit.postResult( data.getB() );
	}

	@Override
	public void setCondiuit(Conduit< ?, ? super String > conduit)
	{
		this.conduit = conduit;
	}


}
