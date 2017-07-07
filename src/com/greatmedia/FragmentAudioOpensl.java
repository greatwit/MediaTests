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

import com.greatmedia.opensles.tester.CaptureTester;
import com.greatmedia.opensles.tester.NativeAudioTester;
import com.greatmedia.opensles.tester.PlayerTester;
import com.greatmedia.opensles.tester.Tester;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class FragmentAudioOpensl extends Fragment implements OnClickListener
{

    private Spinner mTestSpinner;
    private Tester mTester;
    private Button startButton = null, stopButton = null;

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
	    
	    startButton = (Button)v.findViewById(R.id.startButton);
	    stopButton	= (Button)v.findViewById(R.id.stopButton);
	    startButton.setOnClickListener(this);
	    stopButton.setOnClickListener(this);
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
		}
	}

}


