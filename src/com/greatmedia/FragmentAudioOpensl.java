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
import com.greatmedia.internet.NetWork;
import com.greatmedia.opensles.tester.CaptureTester;
import com.greatmedia.opensles.tester.NativeAudioTester;
import com.greatmedia.opensles.tester.PlayerTester;
import com.greatmedia.opensles.tester.Tester;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentAudioOpensl extends Fragment implements OnClickListener
{

    private Spinner mTestSpinner;
    private Tester mTester;
    private Button startButton = null, stopButton = null;
    private Button openslRecvButton = null, openslSendButton = null, openslAllButton = null;
    private TextView sendTip = null, recvTip = null;
    
    private EditText remoteAddr = null, remotePort = null;
    NetWork mNetWork = new NetWork();
    
    AudioWorker mAudio   = new AudioWorker();
	Setting mDataSetting = new Setting();
	
    private boolean mSending = false, mRecving = false, mAllAudio = false;
    private String mRemoteAddr  = "";
    
    
    public static final String[] TEST_PROGRAM_ARRAY = {
            "录制wav文件", "播放wav文件", "Native录制pcm", "Native播放pcm"
    };

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
  {
	    View v = inflater.inflate(R.layout.menuopensl, container, false);
	
	    remoteAddr	 = (EditText)v.findViewById(R.id.remoteAddr);
	    remoteAddr.setText("" + mDataSetting.readData(MainActivity.contx, 0));
		remoteAddr.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Log.e("MainActivity", "emoteAddr: " + remoteAddr.getText().toString() );
				String addr = remoteAddr.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 0, addr);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	    
		remotePort   = (EditText)v.findViewById(R.id.remotePort);
		remotePort.setText("" + mDataSetting.readData(MainActivity.contx, 1));
		remotePort.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Log.e("MainActivity", "emoteAddr: " + remoteAddr.getText().toString() );
				String port = remotePort.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 1, port);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	    mTestSpinner = (Spinner) v.findViewById(R.id.TestSpinner);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.contx, android.R.layout.simple_list_item_1, TEST_PROGRAM_ARRAY);
	    mTestSpinner.setAdapter(adapter);
	    
		sendTip	 	 = (TextView)v.findViewById(R.id.sendTip);
		recvTip	 	 = (TextView)v.findViewById(R.id.recvTip);
	    
	    startButton = (Button)v.findViewById(R.id.startButton);
	    stopButton	= (Button)v.findViewById(R.id.stopButton);
	    startButton.setOnClickListener(this);
	    stopButton.setOnClickListener(this);
	    
	    openslRecvButton = (Button)v.findViewById(R.id.openslRecvButton);
	    openslSendButton = (Button)v.findViewById(R.id.openslSendButton);
	    openslAllButton  = (Button)v.findViewById(R.id.openslAllButton);
	    openslRecvButton.setOnClickListener(this);
	    openslSendButton.setOnClickListener(this);
	    openslAllButton.setOnClickListener(this);

	    return v;
  }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{ 
			case R.id.startButton:
			    switch (mTestSpinner.getSelectedItemPosition()) 
			    {
			            case 0:
			                mTester = new CaptureTester();
			                break;
			            case 1:
			                mTester = new PlayerTester();
			                break;
			            case 2:
			                mTester = new NativeAudioTester(true);
			                break;
			            case 3:
			                mTester = new NativeAudioTester(false);
			                break;
			            default:
			                break;
		        }
		        if (mTester != null) 
		        {
		            mTester.startTesting();
		            Toast.makeText(MainActivity.contx, "Start Testing !", Toast.LENGTH_SHORT).show();
		        }
				break;
				
			case R.id.stopButton:
		        if (mTester != null) 
		        {
		            mTester.stopTesting();
		            Toast.makeText(MainActivity.contx, "Stop Testing !", Toast.LENGTH_SHORT).show();
		        }
				break;
				
			case R.id.openslRecvButton:
				if(mRecving)
				{
					mAudio.StopOpenslRecv();

					mRecving = false;
					recvTip.setText(getResources().getString(R.string.audio_stop));
					recvTip.setTextColor(Color.RED);
				}
				else
				{
					int recvPort = 0;
					if(mNetWork.getInnerPort()==0)
						recvPort = AudioWorker.mRecvPort;
					else
						recvPort = mNetWork.getInnerPort();
					
					mAudio.StartOpenslRecv(recvPort);//AudioWorker.mRecvPort
					
					mRecving = true;
					recvTip.setText(getResources().getString(R.string.audio_recving));
					recvTip.setTextColor(Color.GREEN);
					Log.e("MainOpenslActivity", "openslRecvButton  mRecvPort" +  recvPort);
				}
				break;
				

			case R.id.openslSendButton:
				if(mSending)
				{
					mAudio.StopOpenslSend();

					mSending = false;
					sendTip.setText(getResources().getString(R.string.audio_stop));
					sendTip.setTextColor(Color.RED);
				}
				else
				{
					String strPort =  remotePort.getText().toString();
					int recvPort = 0, sendPort = 0;
					if("".equals(remotePort.getText().toString()))
						recvPort = AudioWorker.mRecvPort;
					else
						recvPort = Integer.parseInt(strPort);
					
					if(mNetWork.getInnerPort()==0)
						sendPort = AudioWorker.mSendPort;
					else
						sendPort = mNetWork.getInnerPort();
					
					mRemoteAddr = mDataSetting.readData(MainActivity.contx, 0);
					mAudio.StartOpenslSend(mRemoteAddr, recvPort, sendPort);//AudioWorker.mRecvPort, 
					
					mSending = true;
					sendTip.setText(getResources().getString(R.string.audio_sending));
					sendTip.setTextColor(Color.GREEN);
					Log.e("MainOpenslActivity", "mRemoteAddr: " + mRemoteAddr + "remotePort:" + recvPort + " sendProt:"+sendPort);
				}
				break;
				
			case R.id.openslAllButton:
				int res = mNetWork.getStunAddr("120.76.204.188", "3478");
				Log.e("MainOpenslActivity", "--getStunAddr result:"+res);
				break;
		}
	}
	


}


