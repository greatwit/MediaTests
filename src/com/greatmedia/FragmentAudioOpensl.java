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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	public void onClick(View v) 
	{
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

					recvTip.setText(getResources().getString(R.string.audio_stop));
					recvTip.setTextColor(Color.RED);
					
					mRecving = false;
				}
				else
				{
					int recvPort = 0;
					
					if("".equals(mDataSetting.readData(MainActivity.contx, 23)))
						recvPort = NetWork.mRecvPort;
					else
					{
						String sPort = mDataSetting.readData(MainActivity.contx, 24);
						Log.e("MainOpenslActivity", "openslRecvButton 1 mRecvPort:" +  sPort);
						if(sPort!=null && !"".equals(sPort))
						{
							Log.e("MainOpenslActivity", "openslRecvButton 2 mRecvPort:" +  sPort);
							recvPort = Integer.parseInt(sPort);
						}
						else
							recvPort = NetWork.mSendPort;
					}
					
					Log.e("MainOpenslActivity", "openslRecvButton 3 mRecvPort:" +  recvPort);
					mAudio.StartOpenslRecv(recvPort);//AudioWorker.mRecvPort

					recvTip.setText(getResources().getString(R.string.audio_recving));
					recvTip.setTextColor(Color.GREEN);
				    
					Log.e("MainOpenslActivity", "openslRecvButton 4 mRecvPort:" +  recvPort);
					mRecving = true;

				}
				//mNetWork.CloseStun();
				
				break;
				

			case R.id.openslSendButton:
				if(mSending)
				{
					mAudio.StopOpenslSend();
					sendTip.setText(getResources().getString(R.string.audio_stop));
					sendTip.setTextColor(Color.RED);
					
					mSending = false;
				}
				else
				{
					int destPort = 0, localPort = 0;
					String sdPort = mDataSetting.readData(MainActivity.contx, 23);
					String slPort = mDataSetting.readData(MainActivity.contx, 24);
					if("".equals(sdPort))
						destPort = NetWork.mRecvPort;
					else
						destPort 	= Integer.parseInt(sdPort);
					
					if("".equals(slPort))
						localPort = NetWork.mSendPort;
					else
						localPort 	= Integer.parseInt(slPort);

					mRemoteAddr = mDataSetting.readData(MainActivity.contx, 13);
					Log.e("MainOpenslActivity", "mRemoteAddr: " + mRemoteAddr + " destPort:" + destPort + " localPort:"+localPort);
					
					mAudio.StartOpenslSend(mRemoteAddr, destPort, localPort);//AudioWorker.mRecvPort,

					sendTip.setText(getResources().getString(R.string.audio_sending));
					sendTip.setTextColor(Color.GREEN);
					
					
					mSending = true;
				}

				break;
				
			case R.id.openslAllButton:
				break;
		}
	}
	

}


