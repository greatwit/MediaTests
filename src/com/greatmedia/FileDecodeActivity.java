
package com.greatmedia;


import java.util.HashMap;
import java.util.Map;

import com.great.happyness.Codec.CodecMedia;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class FileDecodeActivity extends Activity implements SurfaceHolder.Callback 
{
	private final int width 	= 1280;//1920;//
	private final int height 	= 720;//1080;//
	
	private static String fileString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camera.h264";
	private SurfaceHolder holder = null;
	
	final String KEY_MIME 	= "mime";
    final String KEY_WIDTH 	= "width";
    final String KEY_HEIGHT = "height";
	
    private CodecMedia mCodecMedia  	=  new CodecMedia();
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_decode);
		SurfaceView sfv_video = (SurfaceView) findViewById(R.id.sfv_video);

		holder = sfv_video.getHolder();
		holder.addCallback(this);

		MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
		
		mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2500000);
		mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
		
		mCodecMedia.StopVideoSend();
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
	
	@SuppressLint({ "UseValueOf", "InlinedApi" }) @Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
		Map<String, Object> mMap = new HashMap();
		mMap.put(KEY_MIME, "video/avc");
		mMap.put(KEY_WIDTH, new Integer(width));
		mMap.put(KEY_HEIGHT, new Integer(height));
		mMap.put(MediaFormat.KEY_COLOR_FORMAT, new Integer(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar)); 
		mMap.put(MediaFormat.KEY_BIT_RATE, new Integer(2500000));
		mMap.put(MediaFormat.KEY_FRAME_RATE, new Integer(20));
		
        String[] keys = null;
        Object[] values = null;


        keys 	= new String[mMap.size()];
        values 	= new Object[mMap.size()];

        int i = 0;
        for (Map.Entry<String, Object> entry: mMap.entrySet()) 
        {
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
            ++i;
        } 
        
        Log.e("native", "=========size:"+mMap.size());
		
        mCodecMedia.StartFileDecoder(keys, values, holder.getSurface(), null, 0, fileString);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) { 
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCodecMedia.StopFileDecoder();
	}

}


