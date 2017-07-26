package com.greatmedia.audio;

import android.util.Log;




public class AudioWorker 
{
    
	public static String mPreAddress  = "192.168.";
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
	
	public static String IpFactory(String readIp)
	{
		String resIp = "";
		if(readIp.length()<=7)
			resIp = mPreAddress + readIp;
		else
			resIp = readIp;
			
		return resIp;
	}

	public boolean StartAudioWork(String destAddr, short destPort, short localPort, int devCardid)
	{
		/*
		AudioCreateSend(localPort);
		AudioConnectDest(destAddr, destPort);
		AudioStartSend(devCardid);

		AudioCreateRecv(destPort);
		AudioStartRecv(devCardid);
		*/
		//StartAudioTalk(destAddr, destPort, localPort, devCardid);
		StartAudio(destAddr, destPort, localPort);
		return true;
	}
	
	public boolean StopAudioWork()
	{
		/*
		AudioStopRecv();
		AudioFinishRecv();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AudioStopSend();
		AudioFinishSend();
		*/
		//StopAudioTalk();
		StopAudio();
		return true;
	}
	
	public native boolean StartAudio(String destip, short destPort, short localPort);
	public native boolean StopAudio();
	
	public native boolean StartAudioTalk(String destip, short destPort, short localPort, int cardid);
	public native boolean StopAudioTalk();
	
	public native boolean setSoundCardMode(boolean talkback);
	public native boolean deInitSoundCard();
	public native boolean AudioCreateRecv(short recvPort);
	public native boolean AudioFinishRecv();
	public native boolean AudioStartRecv(int cardid);
	public native boolean AudioStopRecv();
	 
	public native boolean AudioCreateSend(short sendPort);
	public native boolean AudioFinishSend();
	public native boolean AudioStartSend(int cardid);
	public native boolean AudioStopSend();
	public native boolean AudioConnectDest(String destip, short port);
	
	
	
	
	public native boolean StartAllAlsa(String destip, short destPort, short localPort);
	public native boolean StopAllAlsa();
	 
	public native boolean StartAlsaRecv(short recvPort);
	public native boolean StopAlsaRecv();

	public native boolean StartAlsaSend(short sendPort, String destip, short destPort);
	public native boolean StopAlsaSend();


	public native boolean StartAllOpensl(String destip, short destPort, short localPort);
	public native boolean StopAllOpensl();
	 
	public native boolean StartOpenslRecv(short localPort);
	public native boolean StopOpenslRecv();

	public native boolean StartOpenslSend(String destip, short destPort, short localPort);
	public native boolean StopOpenslSend();
	
}


