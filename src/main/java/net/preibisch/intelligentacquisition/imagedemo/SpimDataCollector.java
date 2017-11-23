package net.preibisch.intelligentacquisition.imagedemo;

import mpicbg.spim.data.SpimData;
import mpicbg.spim.data.sequence.SequenceDescription;
import net.imglib2.util.Pair;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.DataTransformer;
import net.preibisch.intelligentacquisition.MicData;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;

public class SpimDataCollector implements DataTransformer< MicData<Integer>, Pair<MicData<Integer>, SpimData >>
{

	private SpimData sd;
	
	private void addToData(MicData< Integer > newData)
	{
		//new SequenceDescription( timepoints, setups );
		//new SpimData( sd.getBasePath(), sequenceDescription, viewRegistrations );
	}
	
	@Override
	public <DS extends MicData< Integer >> void notifyWithData(DS data)
	{
		
		
	}

	@Override
	public void addChild(DataListener< Pair< MicData< Integer >, SpimData > > listener)
	{
		// TODO Auto-generated method stub
		
	}


}
