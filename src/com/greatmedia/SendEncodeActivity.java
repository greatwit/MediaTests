
package com.greatmedia;

import com.forsafemedia.internet.NetWork;
import com.great.happyness.Codec.CodecMedia;
import com.great.happyness.Codec.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


@SuppressLint("UseValueOf")
public class SendEncodeActivity extends Activity implements SurfaceHolder.Callback , OnClickListener
{
	//private String TAG = SendEncodeActivity.class.getSimpleName();
	
	private final int width 	= 1280;//1920;//
	private final int height 	= 720;//1080;//
	
	private SurfaceHolder mHolder 		= null;
	private Button btEncodecStart		= null;
	private EditText destAddr	 		= null;
    
    private CodecMedia mCodecMedia  	= new CodecMedia();
    Setting mDataSetting 				= new Setting();
    
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_native_encode);
		SurfaceView sfv_video = (SurfaceView) findViewById(R.id.sfv_video);
		mHolder = sfv_video.getHolder();
		mHolder.addCallback(this);
		
		btEncodecStart	 = (Button)findViewById(R.id.btEncodecStart);
		btEncodecStart.setOnClickListener(this);
		
		String addr1 = mDataSetting.readData(MainActivity.contx, 101);
		destAddr	 = (EditText)findViewById(R.id.destAddr);
		if("".equals(addr1))
			destAddr.setText("" + NetWork.ServerIp1);
		else
			destAddr.setText("" + addr1);
		destAddr.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				// TODO Auto-generated method stub
				Log.e("MainActivity", "remoteAddr: " + destAddr.getText().toString() );
				String addr = destAddr.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 101, addr);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}

	
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
        
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
		
        mCodecMedia.StopCodecSender();
	}
/*
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) 
	{
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
	*/
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		    case R.id.btEncodecStart:
		    	mCodecMedia.StartCodecSend(destAddr.getText().toString(), width, height, mHolder.getSurface());
				break;
		}
	}
	
}


