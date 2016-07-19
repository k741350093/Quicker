package com.quicker.activity;

import com.quicker.R;
import com.quicker.tools.NoticeFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InformActivity extends Activity {
	private String content=NoticeFragment.informContent;
	private TextView informTitleTV;// informTitle_textview
	private TextView informContentTV;// informContent_textview
	private int childrenNum,grpNum;//通知编号，所在组号
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inform);
		Log.d("InformActivity", "start");
		init();
		setContent();//添加标题及内容
	}

	private void setContent() {
		// TODO 添加标题及内容
		informTitleTV.setText(NoticeFragment.informChildrenData[grpNum][childrenNum]);
		informContentTV.setText(content);
	}

	private void init() {
		// TODO 初始化
		informTitleTV = (TextView) findViewById(R.id.inform_title);
		informContentTV = (TextView) findViewById(R.id.inform_content);
		Intent intent = getIntent();//获取上个活动传入的通知位置
		grpNum = intent.getIntExtra("groupPosition", 0);
		childrenNum = intent.getIntExtra("childPosition", 0);
	}

}
