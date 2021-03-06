package com.greatmedia;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import com.great.happyness.Codec.AvcEncoder;
import com.great.happyness.Codec.GreatCamera;
import com.great.happyness.Codec.GreatCamera.Parameters;
import com.great.happyness.Codec.GreatCamera.PreviewCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class EncodeActivity extends Activity  implements SurfaceHolder.Callback,PreviewCallback
{
	private String TAG = EncodeActivity.class.getSimpleName();
	
	private SurfaceView surfaceview;
	
    private SurfaceHolder surfaceHolder;
	
	private GreatCamera camera;
	
    private Parameters parameters;
    
    int width 		= 1280;
    int height 		= 720;
    int framerate 	= 20;
    int biterate 	= 8500*1000;
    
    private static int yuvqueuesize = 10;
    
	public static ArrayBlockingQueue<byte[]> YUVQueue = new ArrayBlockingQueue<byte[]>(yuvqueuesize); 
	
	//private AvcEncoder avcCodec;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encode);
		surfaceview = (SurfaceView)findViewById(R.id.surfaceViewPlay);
        surfaceHolder = surfaceview.getHolder();
        surfaceHolder.addCallback(this);
        SupportAvcCodec();
	}
	

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = getBackCamera();
        startcamera(camera);
		//avcCodec = new AvcEncoder(width, height, framerate, biterate);
		//avcCodec.StartEncoderThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) 
    {
        if (null != camera) 
        {
        	camera.setPreviewCallback(null);
        	camera.stopPreview();
            camera.release();
            camera = null;
            //avcCodec.StopThread();
        }
    }


	@Override
	public void onPreviewFrame(byte[] data, com.great.happyness.Codec.GreatCamera camera) {
		// TODO Auto-generated method stub
		//putYUVData(data, data.length); 
		Log.e(TAG, "putYUVData len:" + data.length);
	}
	
	public void putYUVData(byte[] buffer, int length) {
		if (YUVQueue.size() >= 10) {
			YUVQueue.poll();
		}
		YUVQueue.add(buffer);
	}
	
	@SuppressLint("NewApi")
	private boolean SupportAvcCodec()
	{
		boolean bSupport = false;
		Log.e(TAG, "mediacodec build version:" + Build.VERSION.SDK_INT + " codecCount:" + MediaCodecList.getCodecCount());
		if(Build.VERSION.SDK_INT>=18)
		{
			for(int j = MediaCodecList.getCodecCount() - 1; j >= 0; j--)
			{
				MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(j);
	
				String[] types = codecInfo.getSupportedTypes();
				
				for (int i = 0; i < types.length; i++) 
				{
					Log.e(TAG, "mediacodec SupportedTypes:" + types[i]);
					if (types[i].equalsIgnoreCase("video/avc")) 
					{
						bSupport = true;
					}
				}
			}
		}
		return bSupport;
	}
	

    private void startcamera(GreatCamera mCamera)
    {
        if(mCamera != null)
        {
            try {
                mCamera.setPreviewCallback(this);
                //mCamera.setDisplayOrientation(90);
                
                if(parameters == null)
                    parameters = mCamera.getParameters();
                parameters = mCamera.getParameters();
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(width, height);
                Log.e(TAG, "parameters:" + parameters.flatten());
                mCamera.setParameters(parameters);
                
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	private GreatCamera getBackCamera() 
	{
		GreatCamera c = null;
        try 
        {
            c = GreatCamera.open(0); // attempt to get a Camera instance
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

}

