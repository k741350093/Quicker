package com.quicker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.quicker.R;

public class WaitActivity extends Activity{
   
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait);
		Log.d("WaitActivity", "start");
}
}
