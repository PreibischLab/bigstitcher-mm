package net.preibisch.intelligentacquisition.imagedemo;

import java.util.List;

import bdv.util.BdvFunctions;
import bdv.util.BdvHandle;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import net.imglib2.util.Pair;
import net.preibisch.intelligentacquisition.DataListener;

public class DriftImgDisplayer implements DataListener< Pair<MicDataImpl< Integer >, ? extends List<MicDataImpl< Integer > > > >
{

	private BdvHandle handle;
	private BdvOptions options = BdvOptions.options();

	@Override
	public <D extends Pair< MicDataImpl< Integer >, ? extends List< MicDataImpl< Integer > > >> void notifyWithData(D data)
	{
		BdvStackSource< ? > src = BdvFunctions.show( data.getA().getData(), "BDV", options);
		src.setDisplayRange( 0, 1000 );
		handle = src.getBdvHandle();
		options.addTo( handle );
	}

}
