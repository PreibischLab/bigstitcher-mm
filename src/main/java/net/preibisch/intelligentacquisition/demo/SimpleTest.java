package net.preibisch.intelligentacquisition.demo;

import java.util.ArrayList;

import net.preibisch.intelligentacquisition.Analyzer;
import net.preibisch.intelligentacquisition.Conduit;
import net.preibisch.intelligentacquisition.MicData;
import net.preibisch.intelligentacquisition.Result;

public class SimpleTest
{
	public static void main(String[] args)
	{
		final SimpleConduitImpl< Integer, String > conduit = new SimpleConduitImpl<>();
		final SimpleCounterMicController ctr = new SimpleCounterMicController();

		ctr.setCondiuit( conduit );
		ctr.addChild( new SimpleMicResultHandler() );
		conduit.registerResultListener( ctr );

		final SimpleToStringDataCollector< Integer > collector = new SimpleToStringDataCollector<>();
		final SimplePrintDataListener printer = new SimplePrintDataListener();
		collector.addChild( printer );
		printer.setCondiuit( conduit );

		conduit.registerDataListener( collector );

		ctr.runCounter();
	}
}
