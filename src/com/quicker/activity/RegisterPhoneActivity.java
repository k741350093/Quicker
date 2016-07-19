package com.quicker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.quicker.R;



public class RegisterPhoneActivity extends Activity {

	private Context mContext;
	/*private TitleBarView mTitleBarView;*/
	
	private Button next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register1);
		mContext=this;
		findView();
		Log.d("RegisterPhoneActivity", "start");

		/*initTitleView();
		initTvUrl();*/
		init();
	}
	
	private void findView(){
		/*mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		mTextViewURL=(TextURLView) findViewById(R.id.tv_url);*/
		next=(Button) findViewById(R.id.btn_next);
	}
	
	private void init(){
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,RegisterInfoActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	/*private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_phoneNumber);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}*/
	
	/*private void initTvUrl(){
		mTextViewURL.setText(R.string.tv_xieyi_url);
	}
*/
}
