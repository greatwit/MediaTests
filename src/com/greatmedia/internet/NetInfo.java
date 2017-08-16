package com.greatmedia.internet;



public class NetInfo 
{
    public void set( String outerIp, String innerIp, int outerPort, int innerPort) 
    {
    	mOuterIp 	= outerIp;
		mInnerIp 	= innerIp;
		mOuterPort 	= outerPort;
		mInnerPort 	= innerPort;
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
    private int  mOuterPort 	= 0;
    private int  mInnerPort 	= 0;
};


