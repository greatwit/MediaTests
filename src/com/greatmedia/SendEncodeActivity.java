
package com.greatmedia;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.great.happyness.Codec.CodecMedia;
import com.greatmedia.audio.AudioWorker;
import com.greatmedia.audio.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;


@SuppressWarnings("deprecation")
@SuppressLint("UseValueOf")
public class SendEncodeActivity extends Activity implements SurfaceHolder.Callback ,PreviewCallback, OnClickListener
{
	private String TAG = SendEncodeActivity.class.getSimpleName();
	
	private final int width = 640;
	private final int height = 480;
	
	private static String fileString = "/sdcard/camera.h264";
	private SurfaceHolder mHolder 		= null;
	
	private Button btEncodecStart		= null;
	
	final String KEY_MIME  = "mime";
    final String KEY_WIDTH = "width";
    final String KEY_HEIGHT = "height";
	
    Setting mDataSetting = new Setting();
    private CodecMedia mCodecMedia  	=  new CodecMedia();
    String[] keys 	= null;
    Object[] values = null; 
    
    private Camera mCamera;
    private Parameters parameters;
    
    int mFrameCount = 0;
    private String mRemoteAddr  = "";
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_native_encode);
		SurfaceView sfv_video = (SurfaceView) findViewById(R.id.sfv_video);
		mHolder = sfv_video.getHolder();
		mHolder.addCallback(this);
		
		btEncodecStart	 = (Button)findViewById(R.id.btEncodecStart);
		btEncodecStart.setOnClickListener(this);
		
		
		
		Map<String, Object> mMap = new HashMap();
		mMap.put(KEY_MIME, "video/avc");
		mMap.put(KEY_WIDTH, new Integer(width));
		mMap.put(KEY_HEIGHT, new Integer(height));
		mMap.put(MediaFormat.KEY_COLOR_FORMAT, new Integer(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)); 
		mMap.put(MediaFormat.KEY_BIT_RATE, new Integer(width*height*5));
		mMap.put(MediaFormat.KEY_FRAME_RATE, new Integer(20));
		mMap.put(MediaFormat.KEY_I_FRAME_INTERVAL, new Integer(1));
		
		



        keys = new String[mMap.size()];
        values = new Object[mMap.size()];

        int i = 0;
        for (Map.Entry<String, Object> entry: mMap.entrySet()) 
        {
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
            ++i;
        } 
        
        Log.e("native", "=========size 1:"+mMap.size());

        
	}

	class BufferInfo 
	{
        public void set(int newOffset, int newSize, long newTimeUs, int newFlags) 
        {
            offset = newOffset;
            size = newSize;
            presentationTimeUs = newTimeUs;
            flags = newFlags;
        }

        public int offset;
        public int size;
        public long presentationTimeUs;
        public int flags;
    };
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
		mRemoteAddr = mDataSetting.readData(MainActivity.contx, 0);
        mCodecMedia.StartCodecSender(keys, values, null, null, mRemoteAddr, CodecMedia.mRecvPort, CodecMedia.mSendPort, MediaCodec.CONFIGURE_FLAG_ENCODE);
        Log.e("SendEncodeActivity", "mRemoteAddr: " + mRemoteAddr );
        
        mCamera = getBackCamera();
        startcamera(mCamera);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//mCodecMedia.StopVideoSend();
		
        if (null != mCamera) 
        {
        	mCamera.setPreviewCallback(null);
        	mCamera.stopPreview();
        	mCamera.release();
        	mCamera = null;
        }
		
        mCodecMedia.StopCodecSender();
	}

    private void startcamera(Camera camera)
    {
        if(camera != null)
        {
            try {
            	camera.setPreviewCallback(this);
            	camera.setDisplayOrientation(90);
                if(parameters == null)
                {
                    parameters = camera.getParameters();
                }
                parameters = camera.getParameters();
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(width, height);
                camera.setParameters(parameters);
                camera.setPreviewDisplay(mHolder);
                camera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	private Camera getBackCamera() 
	{
        Camera c = null;
        try 
        {
            c = Camera.open(0); // attempt to get a Camera instance
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		Log.e(TAG, "camera data:" + data.length);
		
		int uvlen = width*height/2;
		int ylen  = width*height;
		for(int i=0;i<uvlen;)
		{
			byte tmp = data[ylen+i];
			data[ylen+i] = data[ylen+i+1];
			data[ylen+i+1] = tmp;
			i+=2;
		}
			
		
		mCodecMedia.CodecSenderData(data, data.length);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		
		    case R.id.btEncodecStart:
		        mCamera = getBackCamera();
		        startcamera(mCamera);
				break;
		}
	}
	
}


