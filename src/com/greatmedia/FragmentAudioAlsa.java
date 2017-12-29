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

import com.forsafe.devicecontrol.AudioWorker;
import com.forsafemedia.internet.NetWork;
import com.great.happyness.Codec.Setting;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class FragmentAudioAlsa extends Fragment implements OnClickListener
{

	private Button btnAudioSend = null, btnAudioRecv = null, btnAudioAll = null;
	
	private TextView sendTip = null, recvTip = null;
	
	private String mRemoteAddr  = "";
	
	private boolean mSending = false, mRecving = false, mAllAudio = false;
	
	AudioWorker mAudio   = new AudioWorker();
	Setting mDataSetting = new Setting();
	
    private String TAG;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
  {
    View v = inflater.inflate(R.layout.menualsa, container, false);
	btnAudioSend = (Button)v.findViewById(R.id.btnAudioSend);
	btnAudioRecv = (Button)v.findViewById(R.id.btnAudioRecv);
	btnAudioAll  = (Button)v.findViewById(R.id.btnAudioAll);
	
	sendTip	 	 = (TextView)v.findViewById(R.id.sendTip);
	recvTip	 	 = (TextView)v.findViewById(R.id.recvTip);
	btnAudioSend.setOnClickListener(this);
	btnAudioRecv.setOnClickListener(this);
	btnAudioAll.setOnClickListener(this);

	
	mDataSetting.setClickSound(MainActivity.contx, false);
	
	//mAudio.setSoundCardMode(true);
	
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
					mAudio.StopAlsaSend();
					//mAudio.AudioStopSend();
					//mAudio.AudioFinishSend();
					
					mSending = false;
					sendTip.setText(getResources().getString(R.string.audio_stop));
					sendTip.setTextColor(Color.RED);
					
					Log.e("MainActivity", "btnAudioSend stop...." );
				}
				else
				{
					mRemoteAddr = mDataSetting.readData(MainActivity.contx, 0);
					
					mAudio.StartAlsaSend( mRemoteAddr.trim(), NetWork.mRecvPort, NetWork.mSendPort, 0);
					/*
					mAudio.AudioCreateSend(mSendPort);
					mAudio.AudioConnectDest(mRemoteAddr.trim(), mRecvPort);
					mAudio.AudioStartSend(1);
					*/
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
					mAudio.StopAlsaRecv();
					/*
					mAudio.AudioStopRecv();
					mAudio.AudioFinishRecv();
					*/
					mRecving = false;
					recvTip.setText(getResources().getString(R.string.audio_stop));
					recvTip.setTextColor(Color.RED);
					
					Log.e("MainActivity", "btnAudioRecv stop...." );
				}
				else
				{
					mAudio.StartAlsaRecv(NetWork.mRecvPort, 0);
					/*
					mAudio.AudioCreateRecv(mRecvPort);
					mAudio.AudioStartRecv(1);
					*/
					mRecving = true;
					recvTip.setText(getResources().getString(R.string.audio_recving));
					recvTip.setTextColor(Color.GREEN);
					
					Log.e("MainActivity", "btnAudioRecv start...." );
				}
				break;
			
			case R.id.btnAudioAll:
				if(mAllAudio)
				{
					mAudio.StopAllAlsa();
					/*
					mAudio.StopAudioWork();
					
					mAudio.setSoundCardMode(false);
					mAudio.deInitSoundCard();
					*/
					recvTip.setText(getResources().getString(R.string.audio_stop));
					recvTip.setTextColor(Color.RED);
					sendTip.setText(getResources().getString(R.string.audio_stop));
					sendTip.setTextColor(Color.RED);
					
					mAllAudio = false;
				}
				else
				{ 
					mAudio.StartAllAlsa( mRemoteAddr.trim(), NetWork.mRecvPort, NetWork.mSendPort, 0);
					/*
					mAudio.setSoundCardMode(true);
					
					mRemoteAddr = mPreAddress + mDataSetting.readData(MainActivity.contx, 0);
					mAudio.StartAudioWork(mRemoteAddr.trim(), mRecvPort, mSendPort, 1);
					*/
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
		//mAudio.setSoundCardMode(false);
		//mAudio.deInitSoundCard();
		mDataSetting.setClickSound(MainActivity.contx, true);
		super.onDestroy();
	}


}