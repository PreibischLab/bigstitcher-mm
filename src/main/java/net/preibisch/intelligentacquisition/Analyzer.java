package net.preibisch.intelligentacquisition;

public interface Analyzer<D, R> extends DataListener<D>, DataChainable< D, D >, ResultPoster< R >
{
	
}
