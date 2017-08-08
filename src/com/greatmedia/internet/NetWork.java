package com.greatmedia.internet;

import android.util.Log;





public class NetWork 
{
	public NetWork()
	{
		 
	}
	
	static
	{
		try
		{
			System.loadLibrary("NetWork");
		}
		catch(Throwable e)
		{
			Log.e("..", "----------------e:"+e.toString());
		}

	}
	
	public native static int  getStunAddr(String destip, String destport);
}