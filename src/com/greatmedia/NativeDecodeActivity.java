
package com.greatmedia;


import java.util.HashMap;
import java.util.Map;

import com.great.happyness.Codec.CodecMedia;



import android.annotation.SuppressLint;
import android.app.Activity;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;

public class NativeDecodeActivity extends Activity implements SurfaceHolder.Callback 
{
	private final int width = 1280;
	private final int height = 720;
	
	private static String fileString = "/sdcard/camera.h264";
	private PlayerThread mPlayer = null;
	private SurfaceHolder holder = null;
	private ImageView imageView = null;
	
	final String KEY_MIME = "mime";
    final String KEY_WIDTH = "width";
    final String KEY_HEIGHT = "height";
	

    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_decode);
		SurfaceView sfv_video = (SurfaceView) findViewById(R.id.sfv_video);
		imageView = (ImageView)findViewById(R.id.image_view);
		if(null == imageView)
		{
			Log.d("Fuck002", "can not find imageView");
		}
		holder = sfv_video.getHolder();
		holder.addCallback(this);
		
		
		
		MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
		mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2500000);
		mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
		
		//Map<String, Object> formatMap = mediaFormat.getMap();

    
        //
		
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
	
	private class PlayerThread extends Thread 
	{
		private CodecMedia decoder  	= null;
		private ImageView  imageView 	= null;
		private Surface surface = null;
		private SurfaceHolder surfaceHolder = null;

		public PlayerThread(ImageView imageView2, Surface surface, SurfaceHolder surfaceHolder) 
		{
			this.imageView = imageView2;
			this.surface = surface;
			this.surfaceHolder = surfaceHolder;
		}

		@SuppressLint("NewApi")
		@Override
		public void run() 
		{
			Map<String, Object> mMap = new HashMap();
			mMap.put(KEY_MIME, "video/avc");
			mMap.put(KEY_WIDTH, new Integer(width));
			mMap.put(KEY_HEIGHT, new Integer(height));
			mMap.put(MediaFormat.KEY_BIT_RATE, new Integer(2500000));
			mMap.put(MediaFormat.KEY_FRAME_RATE, new Integer(20));
			
	        String[] keys = null;
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
	        
	        Log.e("native", "=========size:"+mMap.size());
			
			MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
			mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2500000);
			mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
			Log.e("native", "-------------------------1");
			decoder = CodecMedia.createDecoderByType("video/avc");
			Log.e("native", "-------------------------2");
			decoder.configure(keys, values, surface, null, 0);
			Log.e("native", "-------------------------3");
			decoder.start();
			Log.e("native", "-------------------------4");
			

			// new BufferInfo();
			/*
			ByteBuffer[] inputBuffers  = decoder.getInputBuffers();
			ByteBuffer[] outputBuffers = decoder.getOutputBuffers(); 
			if (null == inputBuffers) 
			{
				Log.d("Fuck", "null == inputBuffers");
			}
			if (null == outputBuffers) 
			{
				Log.d("Fuck", "null == outbputBuffers 111");
			}

			Log.e("native", "-------------------------inputbuffer size:"+inputBuffers.length + "outputbuffer size:"+outputBuffers.length);
			*/
			
			
			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			decoder.startCodec(bufferInfo);
			
			/*
			FileInputStream file = null;
			try 
			{
				file = new FileInputStream(fileString);
			} catch (FileNotFoundException e) 
			{
				Log.d("Fuck", "open file error: " + e.toString());
				return;
			}
			int read_size = -1;
			int mCount = 0;

			for (;;) 
			{
				byte[] h264 = null;
				try 
				{
					byte[] length_bytes = new byte[4];
					read_size = file.read(length_bytes);
					if (read_size < 0) 
					{
						Log.d("Fuck", "read_size<0 pos1");
						break;
					} 
					int byteCount = bytesToInt(length_bytes, 0);
					Log.e("Fuck", "byteCount: " + byteCount); 
					
					h264 = new byte[byteCount];
					read_size = file.read(h264, 0, byteCount);
					// Log.d("Fuck", "read_size: " + read_size);
					if (read_size < 0) 
					{
						Log.d("Fuck", "read_size<0 pos2");
						break;
					}
					// Log.d("Fuck", "pos: " + file.)
				} 
				catch (IOException e) 
				{
					Log.d("Fuck", "read_size 2: " + read_size);
					Log.d("Fuck", "e.toStrinig(): " + e.toString());
					break;
				}

				int inputBufferIndex = decoder.dequeueInputBuffer(-1);
				if (inputBufferIndex >= 0) 
				{
					ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
					inputBuffer.clear();
					inputBuffer.put(h264);
					// long sample_time = ;
					decoder.queueInputBuffer(inputBufferIndex, 0, h264.length, mCount * 1000000 / 20, 0);
					++mCount;
				} 
				else 
				{
					Log.d("Fuck", "dequeueInputBuffer error");
				}

				//Log.e("native", "-------------------------index:" + inputBufferIndex);
				
				ByteBuffer outputBuffer = null;
				MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
				int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
				while (outputBufferIndex >= 0) 
				{
					outputBuffer = outputBuffers[outputBufferIndex];
					decoder.releaseOutputBuffer(outputBufferIndex, true);
					outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
				}

				
				if (outputBufferIndex >= 0) 
				{
					decoder.releaseOutputBuffer(outputBufferIndex, false);
				} 
				else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) 
				{
					outputBuffers = decoder.getOutputBuffers();
					Log.d("Fuck", "outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED");
				} 
				else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) 
				{
					// Subsequent data will conform to new format.
					Log.d("Fuck", "outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
				}
				
				try 
				{
					Thread.sleep(1000/20);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}// end of for

			if (null != file)
			{
				try 
				{
					file.close();
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			Log.d("Fuck", "Finish");
		}// end of run


		
		public int bytesToInt(byte[] src, int offset) 
		{  
		    int value;    
		    value = (int) ((src[offset] & 0xFF)   
		            | ((src[offset+1] & 0xFF)<<8)   
		            | ((src[offset+2] & 0xFF)<<16)   
		            | ((src[offset+3] & 0xFF)<<24));  
		    return value;  
		} 
		
	}// end of class
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
		if (mPlayer == null) 
		{
			mPlayer = new PlayerThread(imageView, holder.getSurface(), holder);
			mPlayer.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}


