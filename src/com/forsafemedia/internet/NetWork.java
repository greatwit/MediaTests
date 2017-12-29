package com.forsafemedia.internet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class NetWork 
{
	private static String TAG = "NetWork";
	public static short mStunPort = 5000, mOuterSendPort = 5120,  mSendPort = 8000, mRecvPort = 9000;
	
	public static String ServerIp1 = "120.76.204.188";  //stun 服务器1地址
	public static String ServerPort1 = "3478";			//stun 服务器1端口
	
	public static String ServerIp2 = "119.29.10.87";	//stun 服务器2地址
	public static String ServerPort2 = "3478";			//stun 服务器2端口
	
	private Handler mHandler;
	
	public NetWork(Handler hand)
	{
		mHandler = hand;
	}
	
	static
	{
		try
		{
			System.loadLibrary("OpenAudio");		//网络库
			System.loadLibrary("NetWork");		//网络库
		}
		catch(Throwable e)
		{
			Log.e(TAG, "loadLibrary:"+e.toString());
		}

	}
	
	int toUnsigned(short s) 
	{ 
            return s & 0x0FFFF; 
    }
	
    public void SetInfo(  String outerIp, String innerIp, int outerPort, int innerPort, byte[] tranid) 
    {
    	mOuterIp 	= outerIp;
		mInnerIp 	= innerIp;
		mOuterPort 	= outerPort;
		mInnerPort  = innerPort;
		
		if(mHandler!=null)
		{
			Message message =  mHandler.obtainMessage();  
			message.what = 0;
		    Bundle bundle = new Bundle();
		    bundle.putByteArray("chanid", tranid);
		    bundle.putString("outerIp", outerIp);
		    bundle.putInt("outerPort", outerPort);
		    bundle.putString("innerIp", innerIp);
		    bundle.putInt("innerPort", innerPort);
		    message.setData(bundle);
		    mHandler.sendMessage(message);
		}
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
    
    private String mOuterIp 		= ""; 	//外网的ip地址
    private String mInnerIp 		= "";	//内网的ip地址
    private int  mOuterPort 		= 0;	//外网的端口号
    private int  mInnerPort 		= 0;	//内网的端口号
    
    
    public native byte[] GetStunMessage(byte[] outinfo);
    

	/**
	 * @函数说明：创建stun服务
	 * @param  bindPort 是本地绑定的网络端口，可以任意指定
	 * @return int类型，大于0为成功，小于等于0为失败
	 */
    public native int  	CreateStunServer(int bindPort);
    
	/**
	 * @函数说明：获取stun穿透的网络信息，包括内网ip和端口，外网的ip和端口号，超时为2秒
	 * @param  destip是stun服务器的ip地址，port是stun服务器的端口号，info是获取到的网络信息返回值
	 * @return int类型，大于0为成功，小于等于0为失败
	 */
    public native int  	GetStunInfo(String destip, int port, int milliSecond, NetInfo info);
    
	/**
	 * @函数说明：关闭stun服务
	 * @param  无
	 * @return 无
	 */
    public native void 	CloseStunServer();
    
	//////////////////////////////////////////////////////////////////////////////////
    
    public native int CreateStunServerLoop(int bindPort);
    public native byte[] SendData(String destip, int destport);
    public native void CloseStunServerLoop();
}

