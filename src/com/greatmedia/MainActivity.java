package com.greatmedia;



import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;


@SuppressWarnings("deprecation")
public class MainActivity extends Activity  implements OnClickListener
{
	public static Context contx;
	
	private String TAG = "MainActivity";

	  // From http://developer.android.com/guide/topics/ui/actionbar.html
	  public static class TabListener<T extends Fragment> implements ActionBar.TabListener 
	  {
	    private Fragment fragment;
	    private final Activity activity;
	    private final String tag;
	    private final Class<T> instance;
	    private final Bundle args;

	    public TabListener(Activity activity, String tag, Class<T> clz) {
	      this(activity, tag, clz, null);
	    }

	    public TabListener(Activity activity, String tag, Class<T> clz,
	        Bundle args) {
	      this.activity = activity;
	      this.tag 		= tag;
	      this.instance = clz;
	      this.args 	= args;
	    }

	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	      // Check if the fragment is already initialized
	      if (fragment == null) {
	        // If not, instantiate and add it to the activity
	        fragment = Fragment.instantiate(activity, instance.getName(), args);
	        ft.add(android.R.id.content, fragment, tag);
	      } else {
	        // If it exists, simply attach it in order to show it
	        ft.attach(fragment);
	      }
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	      if (fragment != null) {
	        // Detach the fragment, because another one is being attached
	        ft.detach(fragment);
	      }
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	      // User selected the already selected tab. Do nothing.
	    }
	}
	  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contx = this;
		initWidgets(); 
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		if(dm.widthPixels<dm.heightPixels)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	
	private void initWidgets()
	{
	    // Create action bar with all tabs.
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    Tab tab = actionBar.newTab()
		        .setText("Network")
		        .setTabListener(new TabListener<FragmentNetWork>(
		            this, "network", FragmentNetWork.class));
		    actionBar.addTab(tab);
	    
	    tab = actionBar.newTab()
	        .setText("Audio")
	        .setTabListener(new TabListener<FragmentAudioAlsa>(
	            this, "audio", FragmentAudioAlsa.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab()
	        .setText("Opensl")
	        .setTabListener(new TabListener<FragmentAudioOpensl>(
	            this, "Opensl", FragmentAudioOpensl.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab()
	        .setText("Video")
	        .setTabListener(new TabListener<FragmentVideoJava>(
	            this, "Video", FragmentVideoJava.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab()
	        .setText("Native")
	        .setTabListener(new TabListener<FragmentVideoNative>(
	            this, "Native", FragmentVideoNative.class));
	    actionBar.addTab(tab);
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{ 
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}
	
}


