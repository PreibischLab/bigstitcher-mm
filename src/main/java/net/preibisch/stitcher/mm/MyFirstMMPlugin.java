package net.preibisch.stitcher.mm;

import java.util.List;

import org.json.JSONException;
import org.micromanager.api.DataProcessor;
import org.micromanager.api.MMPlugin;
import org.micromanager.api.MMTags;
import org.micromanager.api.ScriptInterface;
import org.micromanager.api.TaggedImageAnalyzer;

import mmcorej.CMMCore;
import mmcorej.TaggedImage;

public class MyFirstMMPlugin implements MMPlugin
{

	@Override
	public String getCopyright()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfo()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setApp(final ScriptInterface mm)
	{
		CMMCore mmc = mm.getMMCore();

		TaggedImageAnalyzer tia = new TaggedImageAnalyzer()
		{
			
			@Override
			protected void analyze(TaggedImage img)
			{
				mm.getAcquisitionEngine2010().getSummaryMetadata();
				
			}
		};
		mm.addImageProcessor( tia );
		
	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub

	}

}
