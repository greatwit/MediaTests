/*
 *  Copyright (c) 2013 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package com.greatmedia;

import com.greatmedia.audio.AudioWorker;
import com.greatmedia.audio.Setting;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class FragmentAudioAlsa extends Fragment implements OnClickListener, TextWatcher
{

	private Button btnAudioSend = null, btnAudioRecv = null, btnAudioAll = null;
	private EditText remoteAddr = null;
	private TextView sendTip = null, recvTip = null;
	
	private String mPreAddress  = "192.168.0.";
	private String mRemoteAddr  = "";
	private short mSendPort = 1300, mRecvPort = 1200;
	private boolean mSending = false, mRecving = false, mAllAudio = false;
	
	AudioWorker mAudio   = new AudioWorker();
	Setting mDataSetting = new Setting();
	
    private String TAG;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
  {
    View v = inflater.inflate(R.layout.menuaudio, container, false);
	btnAudioSend = (Button)v.findViewById(R.id.btnAudioSend);
	btnAudioRecv = (Button)v.findViewById(R.id.btnAudioRecv);
	btnAudioAll  = (Button)v.findViewById(R.id.btnAudioAll);
	remoteAddr	 = (EditText)v.findViewById(R.id.remoteAddr);
	sendTip	 	 = (TextView)v.findViewById(R.id.sendTip);
	recvTip	 	 = (TextView)v.findViewById(R.id.recvTip);
	btnAudioSend.setOnClickListener(this);
	btnAudioRecv.setOnClickListener(this);
	btnAudioAll.setOnClickListener(this);
	remoteAddr.addTextChangedListener(this);
	
	remoteAddr.setText("" + mDataSetting.readData(MainActivity.contx, 0));
	
	mDataSetting.setClickSound(MainActivity.contx, false);
	
    return v;
  }

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{ 
			case R.id.btnAudioSend:
				//startActivity(new Intent().setClass(MainActivity.this, OpenslActivity.class));
				
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
					mRemoteAddr = mPreAddress + mDataSetting.readData(MainActivity.contx, 0);
					
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
			
			case R.id.btnAudioAll:
				if(mAllAudio)
				{
					mAudio.StopAudioWork();
					
					mAudio.setSoundCardMode(false);
					mAudio.deInitSoundCard();
					
					recvTip.setText(getResources().getString(R.string.audio_stop));
					recvTip.setTextColor(Color.RED);
					sendTip.setText(getResources().getString(R.string.audio_stop));
					sendTip.setTextColor(Color.RED);
					
					mAllAudio = false;
				}
				else
				{ 
					mAudio.setSoundCardMode(true);
					
					mRemoteAddr = mPreAddress + mDataSetting.readData(MainActivity.contx, 0);
					mAudio.StartAudioWork(mRemoteAddr.trim(), mRecvPort, mSendPort, 1);
					
					sendTip.setText(getResources().getString(R.string.audio_sending));
					sendTip.setTextColor(Color.GREEN);
					
					recvTip.setText(getResources().getString(R.string.audio_recving));
					recvTip.setTextColor(Color.GREEN);
					
					mAllAudio = true;
				}
				
				break;
		}
	}

	
	@Override
	public void onDestroy() 
	{
		mDataSetting.setClickSound(MainActivity.contx, true);
		super.onDestroy();
	}
	
@Override
public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	// TODO Auto-generated method stub
	
}

@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	Log.e("MainActivity", "emoteAddr: " + remoteAddr.getText().toString() );
	String addr = remoteAddr.getText().toString();
	mDataSetting.InsertOrUpdate(MainActivity.contx, 0, addr);
}

@Override
public void afterTextChanged(Editable s) {
	// TODO Auto-generated method stub
	
}




}