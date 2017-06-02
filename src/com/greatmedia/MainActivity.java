package com.greatmedia;

import com.greatmedia.audio.AudioWorker;
import com.greatmedia.audio.Setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity  implements OnClickListener, TextWatcher
{
	
	private Button btnAudioSend = null;
	private Button btnAudioRecv = null;
	private Button btnVideo		= null;
	private EditText remoteAddr = null;
	private TextView sendTip = null, recvTip = null;
	
	Setting mDataSetting = new Setting();
	AudioWorker mAudio   = new AudioWorker();
	
	private String mPreAddress  = "192.168.0.";
	private String mRemoteAddr  = "";
	private short mSendPort = 1300, mRecvPort = 1200;
	private boolean mSending = false, mRecving = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initWidgets();  
		
		mDataSetting.setClickSound(this, false);
		mAudio.setSoundCardMode(true);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	private void initWidgets()
	{
		btnAudioSend = (Button)findViewById(R.id.btnAudioSend);
		btnAudioRecv = (Button)findViewById(R.id.btnAudioRecv);
		btnVideo 	 = (Button)findViewById(R.id.btnVideo);
		remoteAddr	 = (EditText)findViewById(R.id.remoteAddr);
		sendTip	 	 = (TextView)findViewById(R.id.sendTip);
		recvTip	 	 = (TextView)findViewById(R.id.recvTip);
		

	    remoteAddr.setText("" + mDataSetting.readData(this, 0));
		
		btnAudioSend.setOnClickListener(this);
		btnAudioRecv.setOnClickListener(this);
		btnVideo.setOnClickListener(this);
		remoteAddr.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{ 
			case R.id.btnAudioSend:
				if(mSending)
				{
					mAudio.AudioStopSend();
					mAudio.AudioFinishSend();
					
					mSending = false;
					sendTip.setText(getResources().getString(R.string.audio_stop));
					sendTip.setTextColor(Color.RED);
					
					Log.e("MainActivity", "btnAudioSend stop...." );
				}
				else
				{
					mRemoteAddr = mPreAddress + mDataSetting.readData(this, 0);
					
					mAudio.AudioCreateSend(mSendPort);
					mAudio.AudioConnectDest(mRemoteAddr.trim(), mRecvPort);
					mAudio.AudioStartSend(1);
					mSending = true;
					sendTip.setText(getResources().getString(R.string.audio_sending));
					sendTip.setTextColor(Color.GREEN);
					
					Log.e("MainActivity", "mRemoteAddr: " + mRemoteAddr );
					Log.e("MainActivity", "btnAudioSend start...." );
				}

				break;
				
			case R.id.btnAudioRecv:
				if(mRecving)
				{
					mAudio.AudioStopRecv();
					mAudio.AudioFinishRecv();
					mRecving = false;
					recvTip.setText(getResources().getString(R.string.audio_stop));
					recvTip.setTextColor(Color.RED);
					
					Log.e("MainActivity", "btnAudioRecv stop...." );
				}
				else
				{
					mAudio.AudioCreateRecv(mRecvPort);
					mAudio.AudioStartRecv(1);
					mRecving = true;
					recvTip.setText(getResources().getString(R.string.audio_recving));
					recvTip.setTextColor(Color.GREEN);
					
					Log.e("MainActivity", "btnAudioRecv start...." );
				}
				break;
				
				
			case R.id.btnVideo:
				startActivity(new Intent().setClass(MainActivity.this, NativeDecodeActivity.class));
				break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		// TODO Auto-generated method stub
		Log.e("MainActivity", "emoteAddr: " + remoteAddr.getText().toString() );
		String addr = remoteAddr.getText().toString();
		mDataSetting.InsertOrUpdate(this, 0, addr);
	}

	@Override
	public void afterTextChanged(Editable s) 
	{
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onDestroy() 
	{
		mAudio.setSoundCardMode(false);
		mAudio.deInitSoundCard();
		mDataSetting.setClickSound(this, true);
		super.onDestroy();
	}
	
}


