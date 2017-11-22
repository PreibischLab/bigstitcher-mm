package net.preibisch.intelligentacquisition.imagedemo;

import net.imglib2.realtransform.AffineGet;
import net.preibisch.intelligentacquisition.AbstractCompoundResultListener;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.DataAndResultPoster;
import net.preibisch.intelligentacquisition.MicResultHandler;
import net.preibisch.intelligentacquisition.ResultListener;

public class DriftImgMicController extends AbstractCompoundResultListener< AffineGet > implements DataAndResultPoster< MicDataImpl< Integer >, AffineGet > 
{

	private Conduit< ? super MicDataImpl< Integer >, ? super AffineGet > conduit;
	private DriftImgGenerator dg;
	private int waitTime;
	private int imgTime;

	public DriftImgMicController(String path, AffineGet drift, int waitTime, int imgTime)
	{
		dg = new DriftImgGenerator( path, drift );
		this.waitTime = waitTime;
		this.imgTime = imgTime;
	}

	public DriftImgGenerator getImageGenerator()
	{
		return dg;
	}

	public void runAcq()
	{
		new Thread( new Runnable()
		{
			
			@Override
			public void run()
			{
				while (true)
				{
					// img takes some time
					try{ Thread.sleep( imgTime ); } catch ( InterruptedException e ) {}

					// post data and wait a little bit
					conduit.postData( dg.getNextImg() );
					dg.applyDrift();
					System.out.println( "Posted Data" );
					try{ Thread.sleep( waitTime ); } catch ( InterruptedException e ) {}

					// execute all MicResultHandlers
					for (ResultListener< AffineGet > child : children)
					{
						if (MicResultHandler.class.isInstance( child ))
							((MicResultHandler< AffineGet >)child).execute();
					}
				}
			}
		} ).start();
		
	}

	@Override
	public void setCondiuit(Conduit< ? super MicDataImpl< Integer >, ? super AffineGet > conduit)
	{
		this.conduit = conduit;
	}

	@Override
	public void handleResult(AffineGet result) {} // NOP
}
