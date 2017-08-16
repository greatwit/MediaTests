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
import com.greatmedia.internet.NetInfo;
import com.greatmedia.internet.NetWork;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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


public class FragmentNetWork extends Fragment implements OnClickListener
{

	private Button btnServer1 = null, btnServer2 = null, btnClose = null;
	
	private TextView localIp, server1Tip = null, server2Tip = null;
	
	private EditText serverAddr1 = null, serverPort1 = null, serverAddr2 = null, serverPort2 = null, remoteAddr = null, remotePort = null;
	
	private String mRemoteAddr  = "", mLocalIp = "";
	
	
	Setting mDataSetting = new Setting();
	NetWork mNetWork = new NetWork();
	
    private String TAG = "FragmentNetWork";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
  {
	    View v = inflater.inflate(R.layout.menunetwork, container, false);
	    btnServer1 	= (Button)v.findViewById(R.id.btnServer1);
	    btnServer2 	= (Button)v.findViewById(R.id.btnServer2);
	    btnClose 	= (Button)v.findViewById(R.id.btnClose);
		
	    server1Tip	 	 = (TextView)v.findViewById(R.id.server1Tip);
	    server2Tip	 	 = (TextView)v.findViewById(R.id.server2Tip);
	    localIp			 = (TextView)v.findViewById(R.id.localIp);
		btnServer1.setOnClickListener(this);
		btnServer2.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		
		
		//String addr = mNetWork.GetLocalAddr(true);
		//localIp.setText(addr);
		
		mDataSetting.setClickSound(MainActivity.contx, false);
		
		int sock = mNetWork.CreateStunServer(NetWork.mSendPort);
		//Log.e("MainOpenslActivity", "sock:"+sock);
		//mNetWork.getStunAddr("120.76.204.188", "3478");
		
		
		serverAddr1	 = (EditText)v.findViewById(R.id.serverAddr1);
		serverAddr1.setText("" + mDataSetting.readData(MainActivity.contx, 11));
		serverAddr1.addTextChangedListener(new TextWatcher() 
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
				Log.e("MainActivity", "emoteAddr: " + serverAddr1.getText().toString() );
				String addr = serverAddr1.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 11, addr);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		serverPort1   = (EditText)v.findViewById(R.id.serverPort1);
		serverPort1.setText("" + mDataSetting.readData(MainActivity.contx, 21));
		serverPort1.addTextChangedListener(new TextWatcher() 
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
				Log.e("MainActivity", "emoteAddr: " + serverPort1.getText().toString() );
				String port = serverPort1.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 21, port);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		serverAddr2	 = (EditText)v.findViewById(R.id.serverAddr2);
		serverAddr2.setText("" + mDataSetting.readData(MainActivity.contx, 12));
		serverAddr2.addTextChangedListener(new TextWatcher() 
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
				Log.e("MainActivity", "emoteAddr2: " + serverAddr2.getText().toString() );
				String addr = serverAddr2.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 12, addr);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		serverPort2   = (EditText)v.findViewById(R.id.serverPort2);
		serverPort2.setText("" + mDataSetting.readData(MainActivity.contx, 22));
		serverPort2.addTextChangedListener(new TextWatcher() 
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
				Log.e("MainActivity", "emoteAddr2: " + serverPort2.getText().toString() );
				String port = serverPort2.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 22, port);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		remoteAddr	 = (EditText)v.findViewById(R.id.remoteAddr);
		remoteAddr.setText("" + mDataSetting.readData(MainActivity.contx, 13));
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
				Log.e("MainActivity", "remoteAddr: " + remoteAddr.getText().toString() );
				String addr = remoteAddr.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 13, addr);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		remotePort   = (EditText)v.findViewById(R.id.remotePort);
		remotePort.setText("" + mDataSetting.readData(MainActivity.contx, 23));
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
				Log.e("MainActivity", "remotePort: " + remotePort.getText().toString() );
				String port = remotePort.getText().toString();
				mDataSetting.InsertOrUpdate(MainActivity.contx, 23, port);
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		WifiManager wifimanage=(WifiManager)MainActivity.contx.getSystemService(Context.WIFI_SERVICE);//获取WifiManager  
		  
		//检查wifi是否开启
		if(!wifimanage.isWifiEnabled())  
		{
			wifimanage.setWifiEnabled(true);
		}  
		  
		WifiInfo wifiinfo= wifimanage.getConnectionInfo();  
		  
		mLocalIp = intToIp(wifiinfo.getIpAddress());  
		localIp.setText(mLocalIp);
		
	    return v;
   }


  
  private String intToIp(int i)  
  {
	  return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ((i >> 24 ) & 0xFF);
  }
  
	@Override
	public void onClick(View v) 
	{
		NetInfo info = new NetInfo();
		switch(v.getId())
		{ 
			case R.id.btnServer1:
				
				String addr1 =  serverAddr1.getText().toString().trim();
				String port1 =  serverPort1.getText().toString().trim();
				int iport = Integer.parseInt(port1);
				mNetWork.GetStunInfo(addr1, iport, info);
				server1Tip.setText(info.getOuterIp()+":"+info.getOuterPort());
				localIp.setText(mLocalIp+":"+info.getInnerPort());
				mDataSetting.InsertOrUpdate(MainActivity.contx, 24, ""+info.getInnerPort());
				Log.e(TAG, "getStunAddr IP1:"+info.getOuterIp() + " port1:"+info.getOuterPort());
				break;
				
			case R.id.btnServer2:
				String addr2 =  serverAddr2.getText().toString();
				String port2 =  serverPort2.getText().toString();
				int iport2 = Integer.parseInt(port2);
				mNetWork.GetStunInfo(addr2, iport2, info);
				server2Tip.setText(info.getOuterIp()+":"+info.getOuterPort());
				localIp.setText(mLocalIp+":"+info.getInnerPort());
				mDataSetting.InsertOrUpdate(MainActivity.contx, 24, ""+info.getInnerPort());
				Log.e(TAG, "getStunAddr IP2:"+info.getOuterIp() + " port2:"+info.getOuterPort());
				break;
				
			case R.id.btnClose:
				mNetWork.CloseStunServer();
				break;
		}
	}

	class StunThread extends Thread 
	{   
        public void run()
        {
			int res = mNetWork.getStunAddr("120.76.204.188", "3478");
			Log.e("MainOpenslActivity", "--getStunAddr result:"+res);
        }
	}
	
	@Override
	public void onDestroy() 
	{
		mDataSetting.setClickSound(MainActivity.contx, true);
		
		mNetWork.CloseStun();
		
		super.onDestroy();
	}
	
}

