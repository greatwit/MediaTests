package com.forsafe.devicecontrol;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

@SuppressLint("NewApi") 
public class VideoWorker 
{
	private static String TAG = "VideoWorker";
	
	private static MediaCodec mCodec = null;
	
	public VideoWorker()
	{
	}
	
	static
	{
		try
		{
			System.loadLibrary("RemoteTalk");  //音频传输
		}
		catch(Throwable e)
		{
			Log.e(TAG, "loadLibrary:"+e.toString());
		}
	}
	
	public void StartVideoRender(MediaFormat mediaformat, Surface surf, int recvPort)
	{
        try {
			mCodec = MediaCodec.createDecoderByType("video/avc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mCodec.configure(mediaformat, surf, null, 0);
        mCodec.start();
        
        StartVideo(recvPort);
	}
	
	public void StartVideoRender(Surface surf, int width, int height, int frameRate, int recvPort)
	{
	    final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", width, height);
	    //
	    mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
	
	    StartVideoRender(mediaformat, surf, recvPort);
	}
	
	public void StopVideoRender()
	{
		StopVideo();
		
        mCodec.stop();
        mCodec.release();
	}
	
    public void NatInfo( String outerIp, int outerPort, byte[] tranid) 
    {
    	Log.e(TAG, "outerIp:"+outerIp + " outerPort:" + outerPort + " tranid:" + tranid);
    }
    
    public void FrameInfo(int len, byte[] fbuffer) 
    {
    	Log.e(TAG, " framebuffer len:" + len + " buffer len:"+fbuffer.length);
    	long timeoutUs = 10000;
    	
    	MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
    	ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
        int inIndex = mCodec.dequeueInputBuffer(timeoutUs);
        if (inIndex >= 0) 
        {
            ByteBuffer byteBuffer = inputBuffers[inIndex];
            byteBuffer.clear();
            byteBuffer.put(fbuffer, 0, len);

            mCodec.queueInputBuffer(inIndex, 0, len, 0, 0);
            
            int outIndex = mCodec.dequeueOutputBuffer(info, timeoutUs);
            if (outIndex >= 0) 
                mCodec.releaseOutputBuffer(outIndex, (info.size != 0));
        }
    }
    
    
	public native boolean StartVideo(int recvPort);
	public native boolean StopVideo();
	public native byte[]  VideoSendMessage(String destip, int destport, boolean isRtp);
}

