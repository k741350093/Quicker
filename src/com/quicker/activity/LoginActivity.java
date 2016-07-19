package com.quicker.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.quicker.R;
import com.quicker.tools.Result;
import com.quicker.tools.StaticConstant;

public class LoginActivity extends Activity {

	private Context mContext;
	private Button mLogin;
	private Button register;
	private EditText account;
	private EditText password;

	private ProgressDialog dialog;// 登录等待窗口

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Log.d("LoginActivity", "start");
		mContext = this;
		findView();
		init();

	}

	private void findView() {
		mLogin = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);

	}

	private void init() {
		mLogin.setOnClickListener(loginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
		account.setText("123456");
		password.setText("123456");
		dialog = new ProgressDialog(LoginActivity.this);
	}

	private OnClickListener registerOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, RegisterPhoneActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener loginOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			User_Local.setUsername(account.getText().toString());
			User_Local.setPassword(password.getText().toString());
			StaticConstant.stuIdConstant = account.getText().toString();
			if (User_Local.getPassword().equals("")
					|| User_Local.getUsername().equals("")) {
				Toast.makeText(LoginActivity.this, "用户名或密码不能为空",
						Toast.LENGTH_SHORT).show();
			} else {
				dialog.setMessage("登录中");
				dialog.show();

				// Intent intent=new Intent(mContext, WaitActivity.class);
				// startActivity(intent);
				/*
				 * new Thread(new Runnable(){
				 * 
				 * @Override public void run() { LoginToServer(); } }).start();
				 */
				LoginToServer();
			}

		}
	};

	public static class User_Local {

		public static String username;
		public static String password;

		public static String getUsername() {
			return username;
		}

		public static void setUsername(String username) {
			User_Local.username = username;
		}

		public static String getPassword() {
			return password;
		}

		public static void setPassword(String password) {
			User_Local.password = password;
		}
	}

	public void LoginToServer() {
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/login";
		StringRequest loginRequest = new StringRequest(Request.Method.POST,
				url, new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						DealResponseFromServer(s);
						// Log.i("TAG",s);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Log.d("error", arg0.toString());
						Toast.makeText(getBaseContext(), "无法连接到服务器",
								Toast.LENGTH_SHORT).show();
					}

				}) {

			@Override
			protected Map<String, String> getParams() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("username", User_Local.getUsername());
				map.put("password", User_Local.getPassword());
				return map;
			}
		};

		loginRequest.setTag("POST");
		RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
		mQueue.add(loginRequest);
	}

	public void DealResponseFromServer(String s) {
		Gson gson = new Gson();
		Result result = gson.fromJson(s, Result.class);
		Log.d("Json", result.toString());
		if (result.isStatus()) {
			dialog.dismiss();
			Intent intent = new Intent(getApplicationContext(),
					MainReferanceActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}
	}

}