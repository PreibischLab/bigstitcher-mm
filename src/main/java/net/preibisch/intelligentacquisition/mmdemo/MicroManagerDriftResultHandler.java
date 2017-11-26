package net.preibisch.intelligentacquisition.mmdemo;

import mmcorej.CMMCore;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineTransform;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.MicResultHandler;

public class MicroManagerDriftResultHandler implements MicResultHandler< AffineGet >
	
{
	private Conduit< ?, ? super AffineGet > conduit;
	final private CMMCore mmc;
	private AffineTransform tr;

	public MicroManagerDriftResultHandler(CMMCore mmc)
	{
		this.mmc = mmc;
	}

	@Override
	public <RS extends AffineGet> void notifyWithResult(RS result)
	{
		// update our stored transform
		if (tr == null)
			tr = new AffineTransform( result.numDimensions() );
		tr.preConcatenate( result );
	}

	@Override
	public void setConduit(Conduit< ?, ? super AffineGet > conduit)
	{
		this.conduit = conduit;
	}

	@Override
	public void execute()
	{
		System.out.println( "Drift correction executing..." );
		// nothing to do yet
		if (tr == null)
			return;

		double[] shift = new double[tr.numDimensions()];
		tr.apply( shift, shift );

		
		try
		{
			mmc.setRelativeXYPosition( shift[0], shift[1] );
			mmc.setRelativePosition( shift[2] );
		}
		catch ( Exception e ){ e.printStackTrace(); }

	}


}
