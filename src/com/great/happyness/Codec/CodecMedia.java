package com.great.happyness.Codec;


import java.util.HashMap;
import java.util.Map;

import com.great.happyness.Codec.GreatCamera.Parameters;
import com.greatmedia.MainActivity;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;


@SuppressWarnings("deprecation")
@SuppressLint("UseValueOf")
public class CodecMedia 
{
	
	public static short mSendPort = 5008, mRecvPort = 5012;
	
	final String KEY_MIME 	= "mime";
    final String KEY_WIDTH 	= "width";
    final String KEY_HEIGHT = "height";
    
    Setting mDataSetting = new Setting();
    
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
				case Build.VERSION_CODES.JELLY_BEAN_MR1: 	//17 4.2, 4.2.2
				case 18: 									//18 4.3 JELLY_BEAN_MR2
				case 19:									//19 4.4 KITKAT
				case 20:									//20 4.4W KITKAT_WATCH
					System.loadLibrary("Camera4");
					System.loadLibrary("CodecBase4");
					System.loadLibrary("great_media");
					break;
					
				case 21: 									//21 5.0 LOLLIPOP
				case 22:									//22 5.1 LOLLIPOP_MR1
					break;
					
				case 23: 									//6.0
					System.loadLibrary("CodecBase6");
					System.loadLibrary("great_media");
					break;
					
				case 24:									//7.0
				case 25:									//7.1.1/7.1
					break;
					
				case 26:									//8.0 O
					break;
					
					default:
						System.loadLibrary("CodecBase6"); 
						System.loadLibrary("great_media");
						break;
			}

			System.loadLibrary("stlport");
			
			Log.e("..", "----------------2");
		}
		catch(Throwable e)
		{ 
			Log.e("..", "----------------2e:"+e.toString());
		}
		LoadBaseLib(Build.VERSION.SDK_INT);
	}
	
	public native static boolean LoadBaseLib(int version);
	
	public native boolean StopVideoSend();
	
	
	public native boolean StartFileDecoder(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags, String filepath);
	public native boolean StopFileDecoder();
	
	public native boolean StartFileEncoder(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags, String filepath);
	public native boolean StopFileEncoder();
	public native boolean AddEncoderData(byte[] data, int len);
	
	
	public native boolean StartCodecSender(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, String destip, short destport, short localport, int flags);
	public native boolean CodecSenderData(byte[] data, int len);
	public native boolean StartCameraVideo(Surface surface);
	public native boolean StopCameraVideo();
	public native String  GetCameraParameter();
	public native boolean SetCameraParameter(String param);
	public native boolean StopCodecSender();
	
	public native boolean StartCodecRecver(String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags, short recvport);
	public native boolean StopCodecRecver();

	@SuppressLint("NewApi")
	public boolean StartCodecRecv(int width, int height, Surface surface)
	{
		Map<String, Object> mMap = new HashMap();
		mMap.put(KEY_MIME, "video/avc");
		mMap.put(KEY_WIDTH, new Integer(width));
		mMap.put(KEY_HEIGHT, new Integer(height));
		mMap.put(MediaFormat.KEY_COLOR_FORMAT, new Integer(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar));
		mMap.put(MediaFormat.KEY_BIT_RATE, new Integer(width*height*5));
		mMap.put(MediaFormat.KEY_FRAME_RATE, new Integer(20));
		
        String[] keys 	= null;
        Object[] values = null;


        keys = new String[mMap.size()];
        values = new Object[mMap.size()];
 
        int i = 0;
        for (Map.Entry<String, Object> entry: mMap.entrySet()) 
        {
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
            ++i;
        }
        return StartCodecRecver(keys, values, surface, null, 0, CodecMedia.mRecvPort);
	}
	
	@SuppressLint("NewApi")
	public boolean StartCodecSend(int width, int height, Surface surface)
	{
		Map<String, Object> mMap = new HashMap();
		mMap.put(KEY_MIME, "video/avc");
		mMap.put(KEY_WIDTH, new Integer(width));
		mMap.put(KEY_HEIGHT, new Integer(height));
		mMap.put(MediaFormat.KEY_COLOR_FORMAT, new Integer(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)); 
		mMap.put(MediaFormat.KEY_BIT_RATE, new Integer(width*height*5));
		mMap.put(MediaFormat.KEY_FRAME_RATE, new Integer(20));
		mMap.put(MediaFormat.KEY_I_FRAME_INTERVAL, new Integer(2)); //i frame
		
		
        String[] keys 	= null;
        Object[] values = null;


        keys = new String[mMap.size()];
        values = new Object[mMap.size()];

        int i = 0;
        for (Map.Entry<String, Object> entry: mMap.entrySet()) 
        {
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
            ++i;
        }
        
        String remoteAddr = mDataSetting.readData(MainActivity.contx, 0);
        
        remoteAddr = "192.168.43.101";
        StartCodecSender(keys, values, null, null, remoteAddr, CodecMedia.mRecvPort, CodecMedia.mSendPort, MediaCodec.CONFIGURE_FLAG_ENCODE);
        
        String param = GetCameraParameter();
        GreatCamera p  = new GreatCamera();
        GreatCamera.Parameters gp = p.getParameters(param);
        gp.setPreviewFormat(ImageFormat.NV21);
        gp.setPreviewSize(1280, 720);
        String flatParam = gp.flatten();
        SetCameraParameter(flatParam);
        
        StartCameraVideo(surface);
        
        Log.e("SendEncodeActivity", "mRemoteAddr: " + remoteAddr + " camera param:"+flatParam);
        //mCamera.setParameters(parameters);
        return true;
	}
	
}


