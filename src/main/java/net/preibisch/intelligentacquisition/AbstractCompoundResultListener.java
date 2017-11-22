package net.preibisch.intelligentacquisition;

import java.util.ArrayList;

public abstract class AbstractCompoundResultListener<R> implements ResultListener< R >
{

	protected ArrayList< ResultListener< R > > children;

	public AbstractCompoundResultListener()
	{
		this.children = new ArrayList<>();
	}

	public abstract void handleResult(R result);

	@Override
	public void notifyWithResult(R result)
	{
		handleResult( result );
		for (ResultListener< R > child : children)
			child.notifyWithResult( result );
	}

	public void addChild(ResultListener< R > listener)
	{
		this.children.add( listener );
	}

}
