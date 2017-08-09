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
	
	int toUnsigned(short s) 
	{ 
            return s & 0x0FFFF; 
    }
	
    public void SetInfo( String outerIp, String innerIp, short outerPort, short innerPort) 
    {
    	mOuterIp 	= outerIp;
		mInnerIp 	= innerIp;
		mOuterPort 	= toUnsigned(outerPort);
		mInnerPort  = toUnsigned(innerPort);
		
    	Log.e("NetWork", "oip:" + mOuterIp + " iIp:"+mInnerIp + " oPort:" + mOuterPort + " iPort:"+mInnerPort);
    }
	
    public String getOuterIp()
    {
    	return mOuterIp;
    }
    
    public String getInnerIp()
    {
    	return mInnerIp;
    }
    
    public int getOuterPort()
    {
    	return mOuterPort;
    }
    
    public int getInnerPort()
    {
    	return mInnerPort;
    }
    
    private String mOuterIp 		= "";
    private String mInnerIp 		= "";
    private int  mOuterPort 		= 0;
    private int  mInnerPort 		= 0;
    
	public native int  getStunAddr(String destip, String destport);
}