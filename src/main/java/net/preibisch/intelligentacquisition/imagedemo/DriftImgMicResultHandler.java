package net.preibisch.intelligentacquisition.imagedemo;

import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineTransform;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.MicResultHandler;

public class DriftImgMicResultHandler implements MicResultHandler< AffineGet >
{
	AffineTransform correction;
	DriftImgGenerator dg;
	Conduit< ?, ? super AffineGet > conduit;

	public DriftImgMicResultHandler(DriftImgGenerator dg)
	{
		this.dg = dg;
		correction = new AffineTransform( dg.numDimensions() );
	}

	@Override
	public void notifyWithResult(AffineGet result)
	{
		correction.preConcatenate( result  );
	}

	@Override
	public void setCondiuit(Conduit< ?, ? super AffineGet > conduit)
	{
		this.conduit = conduit;
	}

	@Override
	public void execute()
	{
		dg.applyTransform( correction );
		//correction = new AffineTransform( dg.numDimensions() );
	}

}
