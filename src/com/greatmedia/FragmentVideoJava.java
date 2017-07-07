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

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.Button;



public class FragmentVideoJava extends Fragment implements OnClickListener
{
  private String TAG;
  private Button btnVideoEncode		= null, btnVideoDecode = null;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) 
  {
    View v = inflater.inflate(R.layout.menuvideojava, container, false);
    btnVideoEncode 	 = (Button)v.findViewById(R.id.btnVideoEncode);
    btnVideoDecode	 = (Button)v.findViewById(R.id.btnVideoDecode);
    btnVideoEncode.setOnClickListener(this);
    btnVideoDecode.setOnClickListener(this);
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
	    case R.id.btnVideoEncode:
	    	startActivity(new Intent().setClass(MainActivity.contx, EncodeActivity.class));//NativeDecodeActivity
			break;
			
	    case R.id.btnVideoDecode:
	    	startActivity(new Intent().setClass(MainActivity.contx, NativeDecodeActivity.class));
	    	break;
    	
	}
}


}