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

import java.util.HashMap;
import java.util.Map;

import com.great.happyness.Codec.CodecMedia;
import com.greatmedia.audio.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.Button;


public class FragmentVideoNative extends Fragment implements SurfaceHolder.Callback, OnClickListener
{
  private String TAG;
  private Button btnVideoEncode		= null, btnVideoDecode = null, btnLocalVideo = null;
  private boolean mbEncoding        = false, mbDecoding = false, mbFilePlaying = false;
  
  private CodecMedia mDecoder  		= null;
  Setting mDataSetting = new Setting();
  private SurfaceHolder holder = null;
  
  final String KEY_MIME 	= "mime";
  final String KEY_WIDTH 	= "width";
  final String KEY_HEIGHT   = "height";
  String[] mKeys = null;
  Object[] mValues = null;
  
  final int width  = 640;
  final int height = 480;
  
  @SuppressLint({ "InlinedApi", "UseValueOf" }) @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) 
  { 
    View v = inflater.inflate(R.layout.menuvideonative, container, false);
    btnVideoEncode 	 = (Button)v.findViewById(R.id.btnVideoEncode);
    btnVideoDecode	 = (Button)v.findViewById(R.id.btnVideoDecode);
    btnLocalVideo	 = (Button)v.findViewById(R.id.btnLocalVideo);
    btnVideoEncode.setOnClickListener(this);
    btnVideoDecode.setOnClickListener(this);
    btnLocalVideo.setOnClickListener(this);
    
    mDecoder  	= new CodecMedia();
    

    
	Map<String, Object> mMap = new HashMap<String, Object>();
	mMap.put(KEY_MIME, "video/avc");
	mMap.put(KEY_WIDTH, new Integer(width));
	mMap.put(KEY_HEIGHT, new Integer(height));
	mMap.put(MediaFormat.KEY_BIT_RATE, new Integer(width*height*5));
	mMap.put(MediaFormat.KEY_FRAME_RATE, new Integer(30));
	
	//mMap.put(MediaFormat.KEY_COLOR_FORMAT, new Integer(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar));//
	//mMap.put(MediaFormat.KEY_I_FRAME_INTERVAL, new Integer(1));
	
	Log.e("native", "=========size:"+mMap.size());


	mKeys 	= new String[mMap.size()];
	mValues = new Object[mMap.size()];

    int i = 0;
    for (Map.Entry<String, Object> entry: mMap.entrySet()) 
    {
    	mKeys[i] = entry.getKey();
    	mValues[i] = entry.getValue();
        ++i;
    }
	
	SurfaceView sfv_video = (SurfaceView) v.findViewById(R.id.sfv_video);

	holder = sfv_video.getHolder();
	holder.addCallback(this);
    
    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

  }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId())
	{
	/*
	    case R.id.btnVideoEncode:
	    	if(mbEncoding)
	    	{ 
	    		mDecoder.StopVideoSend();
	    		mbEncoding = false;
	    	}else
	    	{
	    		//mDecoder.StartVideoSend(mKeys, mValues, holder.getSurface(), null, "192.168.0."+mDataSetting.readData(MainActivity.contx, 0), MediaCodec.CONFIGURE_FLAG_ENCODE, (short)2300, (short)2200);
	    		Thread EncoderThread = new Thread(new Runnable() 
	    		{
	    			@SuppressLint("NewApi")
	    			@Override
	    			public void run() 
	    			{
	    				mDecoder.StartVideoSend(mKeys, mValues, holder.getSurface(), null, "192.168.0."+mDataSetting.readData(MainActivity.contx, 0), MediaCodec.CONFIGURE_FLAG_ENCODE, (short)2300, (short)2200);
	    			}
	    		});
	    		EncoderThread.start();
	    		mbEncoding = true;
	    	}
			break;
			
	    case R.id.btnVideoDecode:
	    	if(mbDecoding)
	    	{
	    		mbDecoding = false;
	    	}else
	    	{
	    		mDecoder.StartVideoRecv(mKeys, mValues, holder.getSurface(), null, 0, (short)2200);
	    		mbDecoding = true;
	    	}
	    	break;
    	
	    case R.id.btnLocalVideo:
	    	if(mbFilePlaying)
	    	{
	    		mDecoder.StopVideoFilePlay();
	    		mbFilePlaying = false;
	    	}
	    	else
	    	{
	    		mDecoder.StartVideoFilePlay(mKeys, mValues, holder.getSurface(), null, 0);
	    		mbFilePlaying = true;
	    	}
	    	break;
	    	*/
	}
}



@SuppressLint("InlinedApi") @Override
public void surfaceCreated(SurfaceHolder holder) 
{
	// TODO Auto-generated method stub
	/*
	if(!mbEncoding)
	{
		mDecoder.StartVideoSend(mKeys, mValues, holder.getSurface(), null, "192.168.0."+mDataSetting.readData(MainActivity.contx, 0), MediaCodec.CONFIGURE_FLAG_ENCODE, (short)2300, (short)2200);
		mbEncoding = true;
	}
	*/
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