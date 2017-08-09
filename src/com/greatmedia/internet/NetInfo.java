package com.greatmedia.internet;



public class NetInfo 
{
    public void set( String outerIp, String innerIp, short outerPort, short innerPort) 
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
    
    public short getOuterPort()
    {
    	return mOuterPort;
    }
    
    public short getInnerPort()
    {
    	return mInnerPort;
    }
    
    private String mOuterIp 		= "";
    private String mInnerIp 		= "";
    private short  mOuterPort 	= 0;
    private short  mInnerPort 	= 0;
};


