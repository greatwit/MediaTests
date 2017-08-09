package com.greatmedia.audio;

import android.util.Log;




public class AudioWorker 
{
	public static short mSendPort = 1300, mRecvPort = 1200;
	
	public AudioWorker()
	{
		 
	}
	
	static
	{
		try
		{
			Log.e("..", "----------------1");
			System.loadLibrary("OpenAudio");
			System.loadLibrary("stlport");
			System.loadLibrary("AudioTest");
			Log.e("..", "----------------1");
		}
		catch(Throwable e)
		{
			Log.e("..", "----------------e:"+e.toString());
		}

	}


	/*
	public native boolean StartAudioTalk(String destip, short destPort, short localPort, int cardid);
	public native boolean StopAudioTalk();
	

	public native boolean AudioCreateRecv(short recvPort);
	public native boolean AudioFinishRecv();
	public native boolean AudioStartRecv(int cardid);
	public native boolean AudioStopRecv();
	 
	public native boolean AudioCreateSend(short sendPort);
	public native boolean AudioFinishSend();
	public native boolean AudioStartSend(int cardid);
	public native boolean AudioStopSend();
	public native boolean AudioConnectDest(String destip, short port);
	*/
	public native boolean setSoundCardMode(boolean talkback);
	public native boolean deInitSoundCard();
	
	
	
	public native boolean StartAllAlsa(String destip, int destPort, int localPort);
	public native boolean StopAllAlsa();
	 
	public native boolean StartAlsaRecv(int recvPort);
	public native boolean StopAlsaRecv();

	public native boolean StartAlsaSend(int sendPort, String destip, int destPort);
	public native boolean StopAlsaSend();


	public native boolean StartAllOpensl(String destip, int destPort, int localPort);
	public native boolean StopAllOpensl();
	 
	public native boolean StartOpenslRecv(int localPort);
	public native boolean StopOpenslRecv();

	public native boolean StartOpenslSend(String destip, int destPort, int localPort);
	public native boolean StopOpenslSend();
	
}


