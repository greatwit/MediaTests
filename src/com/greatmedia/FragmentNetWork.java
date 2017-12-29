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

import java.util.Arrays;

import com.forsafemedia.internet.NetInfo;
import com.forsafemedia.internet.NetWork;
import com.great.happyness.Codec.Setting;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
	
	public NetworkHandler mHandler 	= new NetworkHandler();
	Setting mDataSetting 			= new Setting();
	NetWork mNetWork 				= new NetWork(mHandler);

	
	byte[] mTranid1 = null , mTranid2 = null;
	
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
		
		mDataSetting.setClickSound(MainActivity.contx, false);
		
		//int sock = mNetWork.CreateStunServer(NetWork.mSendPort);
		new MessageThread().start();
		//Log.e("MainOpenslActivity", "sock:"+sock);
		//mNetWork.getStunAddr("120.76.204.188", "3478");
		
		
		byte[] msg = mNetWork.GetStunMessage(null);
		Log.e(TAG, "message length:"+msg.length);
		for(int i=0;i<msg.length;i++)
			Log.e(TAG, ""+msg[i]);
		
		String addr1 = mDataSetting.readData(MainActivity.contx, 11);
		serverAddr1	 = (EditText)v.findViewById(R.id.serverAddr1);
		if("".equals(addr1))
			serverAddr1.setText("" + NetWork.ServerIp1);
		else
			serverAddr1.setText("" + addr1);
		serverAddr1.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
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
		String port1  = mDataSetting.readData(MainActivity.contx, 21);
		if("".equals(port1))
			serverPort1.setText("" + NetWork.ServerPort1);
		else
			serverPort1.setText("" + port1);
		serverPort1.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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
		String addr2 = mDataSetting.readData(MainActivity.contx, 12);
		if("".equals(addr2))
			serverAddr2.setText("" + NetWork.ServerIp2);
		else
			serverAddr2.setText("" + addr2);
		serverAddr2.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
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
		String port2  = mDataSetting.readData(MainActivity.contx, 22);
		if("".equals(port1))
			serverPort2.setText("" + NetWork.ServerPort2);
		else
			serverPort2.setText("" + port2);
		serverPort2.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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
		
//		WifiManager wifimanage=(WifiManager)MainActivity.contx.getSystemService(Context.WIFI_SERVICE);//获取WifiManager  
//		  
//		//检查wifi是否开启
//		if(!wifimanage.isWifiEnabled())  
//			wifimanage.setWifiEnabled(true);
//		  
//		WifiInfo wifiinfo= wifimanage.getConnectionInfo();  
//		  
//		mLocalIp = intToIp(wifiinfo.getIpAddress());  
//		localIp.setText(mLocalIp);
		
	    return v;
   }

  class NetworkHandler extends Handler 
  {
           public NetworkHandler() {
           }
   
           public NetworkHandler(Looper L) 
           {
               super(L);
           }
           // 子类必须重写此方法,接受数据
           @Override
           public void handleMessage(Message msg) 
           {
               // TODO Auto-generated method stub
	   			switch(msg.what)
	   			{
		   			case 0:
		   				Bundle bundle=msg.getData();
		   				String outerIp	=	bundle.getString("outerIp");
		   				int outerPort 	= 	bundle.getInt("outerPort");
		   				String innerIp	=	bundle.getString("innerIp");
		   				int innerPort 	= 	bundle.getInt("innerPort");
		   				byte[] id 		= 	bundle.getByteArray("chanid");
		   				Log.e(TAG, "get outerIp:"+outerIp + " id:" + id + " tranid1:" + mTranid1+ " tranid2:" + mTranid2);
		   				if(id!=null && mTranid1!=null && Arrays.equals(id, mTranid1))
		   				{
		   					server1Tip.setText(outerIp+":" + outerPort);
		   					localIp.setText(mLocalIp+":" + innerPort);
		   					mDataSetting.InsertOrUpdate(MainActivity.contx, 24, ""+innerPort);
		   				}
		   				if(id!=null && mTranid2!=null && Arrays.equals(id, mTranid2))
		   				{
		   					server2Tip.setText(outerIp+":" + outerPort);
		   					localIp.setText(mLocalIp+":" + innerPort);
		   					mDataSetting.InsertOrUpdate(MainActivity.contx, 24, ""+innerPort);
		   				}
		   				break;
		   				
		   				default:
		   					break;
	   			}
           }
  }
  
  class MessageThread extends Thread 
  {   
      public void run()
      {
    	  mNetWork.CreateStunServerLoop(NetWork.mStunPort);
      }
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
				//mNetWork.GetStunInfo(addr1, iport, info);
				mTranid1 = mNetWork.SendData(addr1, iport);
//				server1Tip.setText(info.getOuterIp()+":"+info.getOuterPort());
//				localIp.setText(mLocalIp+":"+info.getInnerPort());
//				mDataSetting.InsertOrUpdate(MainActivity.contx, 24, ""+info.getInnerPort());
//				//Log.e(TAG, "getStunAddr IP1:"+info.getOuterIp() + " port1:"+info.getOuterPort());
				break;
				
			case R.id.btnServer2:
				String addr2 =  serverAddr2.getText().toString();
				String port2 =  serverPort2.getText().toString();
				int iport2 = Integer.parseInt(port2);
				//mNetWork.GetStunInfo(addr2, iport2, info);
				mTranid2 = mNetWork.SendData(addr2, iport2);
//				server2Tip.setText(info.getOuterIp()+":"+info.getOuterPort());
//				localIp.setText(mLocalIp+":"+info.getInnerPort());
//				mDataSetting.InsertOrUpdate(MainActivity.contx, 24, ""+info.getInnerPort());
//				Log.e(TAG, "get return msg2:" + mTranid2.length);
				break;
				
			case R.id.btnClose:
				mNetWork.CloseStunServerLoop();
				break;
		}
	}

	
	@Override
	public void onDestroy() 
	{
		mDataSetting.setClickSound(MainActivity.contx, true);
		
		mNetWork.CloseStunServerLoop();
		
		super.onDestroy();
	}
	
}

