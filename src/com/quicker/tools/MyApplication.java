package com.quicker.tools;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
	  public static RequestQueue requestQueue;

	  @Override
	  public void onCreate() {
	      super.onCreate();
	      requestQueue = Volley.newRequestQueue(getApplicationContext());
	  }

	  public  RequestQueue getHttpQueue(){
	      return requestQueue;
	  }
}