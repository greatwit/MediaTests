package com.forsafe.devicecontrol;

import android.util.Log;




public class AudioWorker 
{
	private static String TAG = "AudioWorker";
	
	public AudioWorker()
	{
		 
	}
	
	static
	{
		try
		{
			System.loadLibrary("stlport");
			System.loadLibrary("OpenAudio"); //opensl 库
			System.loadLibrary("RemoteTalk");  //音频传输
		}
		catch(Throwable e)
		{
			Log.e(TAG, "loadLibrary:"+e.toString());
		}
	}

    public void NatInfo( String outerIp, int outerPort, byte[] tranid) 
    {
    	Log.e(TAG, "outerIp:"+outerIp + " outerPort:" + outerPort + " tranid:" + tranid);
    }
	
	public native boolean StartAllAlsa(String destip, int destPort, int localPort, int cardid);
	public native boolean StopAllAlsa();
	 
	public native boolean StartAlsaRecv(int recvPort, int cardid);
	public native boolean StopAlsaRecv();

	public native boolean StartAlsaSend(String destip, int destPort, int sendPort, int cardid);
	public native boolean StopAlsaSend();


	/**
	 * @函数说明：打开opensl的音频接收
	 * @param  localPort 是数据接收端口
	 * @return true为打开成功；false为失败
	 */
	public native boolean StartOpenslRecv(int localPort);
	
	/**
	 * @函数说明：关闭opensl的音频接收
	 * @param  无
	 * @return true为打开成功；false为失败
	 */
	public native boolean StopOpenslRecv();

	public native byte[]  AudioSendMessage(String destip, int destport, boolean isRtp);
	
	/**
	 * @函数说明：开始opensl的播放
	 * @param int samplerate是采样率，  int inChannels是内通道数, int outChannel是外通道数, int frameSize是帧率
	 * @return true为打开成功；false为失败
	 */
	public native boolean StartOpenslPlay(int samplerate, int inChannels, int outChannel, int frameSize);

	/**
	 * @函数说明：关闭opensl的音频播放
	 * @param 无
	 * @return true为打开成功；false为失败
	 */
	public native boolean StopOpenslPlay();
	
	/**
	 * @函数说明：打开opensl的音频发送
	 * @param  destip是对方的ip地址; destPort是对方端口; localPort是本地发送端口，可以任意指定
	 * @return true为打开成功；false为失败
	 */
	public native boolean StartOpenslSend(String destip, int destPort, int localPort);
	
	/**
	 * @函数说明：关闭opensl的音频发送
	 * @param  无
	 * @return true为打开成功；false为失败
	 */
	public native boolean StopOpenslSend();
    
}


