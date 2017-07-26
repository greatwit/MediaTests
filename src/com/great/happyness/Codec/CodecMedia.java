package com.great.happyness.Codec;


import android.media.MediaCrypto;
import android.os.Build;
import android.util.Log;
import android.view.Surface;



public class CodecMedia 
{
	
	public static String mPreAddress  = "192.168.";
	public static short mSendPort = 9000, mRecvPort = 8000;
	
	public CodecMedia()
	{
		
	}
	
    public static CodecMedia createDecoderByType(String type) 
    {
        return new CodecMedia(type, true /* nameIsType */, false /* encoder */);
    }

    /**
     * Instantiate an encoder supporting output data of the given mime type.
     * @param type The desired mime type of the output data.
     */
    public static CodecMedia createEncoderByType(String type) 
    {
        return new CodecMedia(type, true /* nameIsType */, true /* encoder */);
    }

    /**
     * If you know the exact name of the component you want to instantiate
     * use this method to instantiate it. Use with caution.
     * Likely to be used with information obtained from {@link android.media.MediaCodecList}
     * @param name The name of the codec to be instantiated.
     */
    public static CodecMedia createByCodecName(String name) 
    {
        return new CodecMedia(name, false /* nameIsType */, false /* unused */);
    }

    private CodecMedia(String name, boolean nameIsType, boolean encoder) 
    {
        //native_setup(name, nameIsType, encoder);
    }

    @Override
    protected void finalize() 
    {
    	//release();
    }
	
    public void configure( String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags) 
    {
        //native_configure(keys, values, surface, crypto, flags);
    }
    

	static
	{
		Log.e("..", "----------------build version:"+Build.VERSION.SDK_INT);
		try
		{
			switch(Build.VERSION.SDK_INT)
			{
				case Build.VERSION_CODES.JELLY_BEAN_MR1: //4.2, 4.2.2
					System.loadLibrary("CodecBase");
					System.loadLibrary("great_media");
					break;
					
				case Build.VERSION_CODES.LOLLIPOP: //5.0
					break;
					
				case Build.VERSION_CODES.M: //android 6.0
					System.loadLibrary("CodecBase6");
					System.loadLibrary("great_media6");
					break;
			}

			System.loadLibrary("stlport");
			
			Log.e("..", "----------------2");
		}
		catch(Throwable e)
		{ 
			Log.e("..", "----------------2e:"+e.toString());
		}

	}

	/*
	private native void native_configure( String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags);
	public native final void startCodec();

	public native boolean StartVideoSend(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags, BufferInfo info);
	public native boolean StopVideoSend();
	*/
	public native boolean StartFileDecoder(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags);
	public native boolean StopFileDecoder();
	
	public native boolean StartFileEncoder(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags);
	public native boolean StopFileEncoder();
	public native boolean AddEncoderData(byte[] data, int len);
	
	
	public native boolean StartCodecSender(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, String destip, short destport, short localport, int flags);
	public native boolean CodecSenderData(byte[] data, int len);
	public native boolean StopCodecSender();
	
	public native boolean StartCodecRecver(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags, short recvport);
	public native boolean StopCodecRecver();
	
	/*
	public native final boolean StartVideoSend(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, String remoteIp, int flags, short localSendPort, short remotePort);
	public native final boolean StopVideoSend();
	
	public native final boolean StartVideoRecv(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags, short localRecvPort);
	public native final boolean StopVideoRecv();
	
	public native final boolean StartVideoFilePlay(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags);
	public native final boolean StopVideoFilePlay();
	*/
}


