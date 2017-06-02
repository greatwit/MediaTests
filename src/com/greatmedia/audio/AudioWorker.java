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
	 public native boolean AudioCreateRecv(short recvPort);
	 public native boolean AudioFinishRecv();
	 public native boolean AudioStartRecv(int cardid);
	 public native boolean AudioStopRecv();
	 
	 public native boolean AudioCreateSend(short sendPort);
	 public native boolean AudioFinishSend();
	 public native boolean AudioStartSend(int cardid);
	 public native boolean AudioStopSend();
	 public native boolean AudioConnectDest(String destip, short port);
	
}


