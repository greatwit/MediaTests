package com.greatmedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


import com.forsafe.devicecontrol.VideoWorker;


@SuppressLint("NewApi")
public class ComDecodeActivity extends Activity 
{
    private SurfaceView mSurface = null;
    private SurfaceHolder mSurfaceHolder;


    private static final int VIDEO_WIDTH 	= 1280;
    private static final int VIDEO_HEIGHT 	= 720;
    private int FrameRate 					= 25;
    
    VideoWorker mVideo   					= new VideoWorker();
    
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_decode);
        mSurface = (SurfaceView) findViewById(R.id.sfv_video);

        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() 
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder) 
            {
            	 /*
                final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", VIDEO_WIDTH, VIDEO_HEIGHT);
                //
                mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, FrameRate);
                mVideo.StartVideoRender(mediaformat, holder.getSurface(), 8000);
                */
            	mVideo.StartVideoRender(holder.getSurface(), VIDEO_WIDTH, VIDEO_HEIGHT, FrameRate, 8000);
                mVideo.VideoSendMessage("119.29.10.87", 3478, true);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) 
            {
            	mVideo.StopVideo();
            }
        });
    }
}

