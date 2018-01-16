
package com.greatmedia;

import com.great.happyness.Codec.CodecMedia;



import android.annotation.SuppressLint;
import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelfCodecActivity extends Activity implements OnClickListener
{
	private final int width 	= 1280;//1920;//
	private final int height 	= 720;//1080;//
	
	private SurfaceHolder sendHolder = null, recvHolder = null;
	private Button btSelfTestStart 	 = null;
	
    private CodecMedia mCodecMedia  	=  new CodecMedia();
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_native_selftest);
		
		SurfaceView sfv_send = (SurfaceView) findViewById(R.id.sfv_send);
		sendHolder = sfv_send.getHolder();
		sendHolder.addCallback(new SendCallBack());
		
		SurfaceView sfv_recv = (SurfaceView) findViewById(R.id.sfv_recv);
		recvHolder = sfv_recv.getHolder();
		recvHolder.addCallback(new RecvCallBack());
		
		btSelfTestStart = (Button) findViewById(R.id.btSelfTestStart);
		btSelfTestStart.setOnClickListener(this);
	}
	
	private Runnable workHour = new Runnable() 
	{
	    public void run()
	    {
	    	
	    }
	};
	
	public Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
		}
	};
	
	//
	class SendCallBack implements SurfaceHolder.Callback
	{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
		{
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCodecMedia.StopCodecSender();
		}
	};
	
	class RecvCallBack implements SurfaceHolder.Callback
	{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
		{
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCodecMedia.StartCodecRecv(width, height, holder.getSurface());
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCodecMedia.StopCodecRecver();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		    case R.id.btSelfTestStart:
		    	mCodecMedia.StartCodecSend("127.0.0.1", width, height, sendHolder.getSurface());
				break;
		}
	};

}


