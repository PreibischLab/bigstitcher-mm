package net.preibisch.intelligentacquisition.imagedemo;

import java.util.List;

import bdv.util.BdvFunctions;
import bdv.util.BdvHandle;
import bdv.util.BdvStackSource;
import net.imglib2.util.Pair;
import net.preibisch.intelligentacquisition.DataListener;

public class DriftImgDisplayer implements DataListener< Pair<MicDataImpl< Integer >, ? extends List<MicDataImpl< Integer > > > >
{

	private BdvHandle handle;

	@Override
	public <D extends Pair< MicDataImpl< Integer >, ? extends List< MicDataImpl< Integer > > >> void notifyWithData(D data)
	{
		if (handle != null)
			handle.close();
		BdvStackSource< ? > src = BdvFunctions.show( data.getA().getData(), "BDV" );
		src.setDisplayRange( 0, 1000 );
		handle = src.getBdvHandle();
	}

}
