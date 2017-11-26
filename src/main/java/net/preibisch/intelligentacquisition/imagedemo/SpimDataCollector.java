package net.preibisch.intelligentacquisition.imagedemo;

import mpicbg.spim.data.SpimData;
import mpicbg.spim.data.generic.sequence.ImgLoaderHint;
import mpicbg.spim.data.sequence.ImgLoader;
import mpicbg.spim.data.sequence.SequenceDescription;
import mpicbg.spim.data.sequence.SetupImgLoader;
import mpicbg.spim.data.sequence.VoxelDimensions;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.preibisch.intelligentacquisition.DataListener;
import net.preibisch.intelligentacquisition.DataTransformer;
import net.preibisch.intelligentacquisition.MicData;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;

public class SpimDataCollector implements DataTransformer< MicData<Integer>, Pair<MicData<Integer>, SpimData2 >>
{

	private SpimData2 sd;
	
	private void addToData(MicData< Integer > newData)
	{
		sd.getSequenceDescription();
		//new SequenceDescription( timepoints, setups )
		//new SequenceDescription( timepoints, setups );
		//new SpimData( sd.getBasePath(), sequenceDescription, viewRegistrations );
	}
	
	@Override
	public <DS extends MicData< Integer >> void notifyWithData(DS data)
	{
		
		
	}

	@Override
	public void addChild(DataListener< Pair< MicData< Integer >, SpimData2 > > listener)
	{
		// TODO Auto-generated method stub
		
	}

}
