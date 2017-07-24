
package com.greatmedia;

import android.app.Activity;


import android.os.Bundle;
import android.util.Log;

import android.view.WindowManager;
import android.widget.ImageView;

public class AudioActivity extends Activity
{

	private ImageView imageView  = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_decode);

	}

	protected void onDestroy() 
	{
		super.onDestroy();
	}

}


