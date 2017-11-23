package net.preibisch.intelligentacquisition;

import java.util.ArrayList;

public abstract class AbstractCompoundResultListener<R> implements ResultListener< R >
{

	protected ArrayList< ResultListener< R > > children;

	public AbstractCompoundResultListener()
	{
		this.children = new ArrayList<>();
	}

	public abstract <RS extends R> void handleResult(RS result);

	@Override
	public <RS extends R> void notifyWithResult(RS result)
	{
		handleResult( result );
		for (ResultListener< R > child : children)
			child.notifyWithResult( result );
	}

	public <RS extends R> void addChild(ResultListener< RS > listener)
	{
		this.children.add( (ResultListener< R >) listener );
	}

}
