package com.greatmedia.audio;

import android.util.Log;




public class AudioWorker 
{
	
	
	public AudioWorker()
	{
		 
	}
	
	static
	{
		try
		{
			Log.e("..", "----------------1");
			System.loadLibrary("stlport");
			System.loadLibrary("OpenAudio");
			System.loadLibrary("AudioTest");
			Log.e("..", "----------------1");
		}
		catch(Throwable e)
		{
			Log.e("..", "----------------e:"+e.toString());
		}

	}


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


