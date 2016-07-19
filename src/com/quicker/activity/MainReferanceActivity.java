package com.quicker.activity;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quicker.R;

public class MainReferanceActivity extends Activity {

	private int imageIds[];
	private String[] titles;
	private ArrayList<ImageView> images;
	private ArrayList<View> dots;
	private TextView title;
	private ViewPager mViewPager;
	private ViewPagerAdapter adapter;

	private Button mnotice;
	private Button mactivity;
	private Button mclasssituation;
	private Button mmysettings;

	private int oldPosition = 0;
	private int currentItem;
	private ScheduledExecutorService scheduledExecutorService;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		findView();
		init();
		Log.d("MainReferanceActivity", "start");
		// ͼƬID
		imageIds = new int[] { R.drawable.main_background1,
				R.drawable.main_background2, R.drawable.main_background3,
				R.drawable.main_background4, R.drawable.main_background5, };

		titles = new String[] { "你好，成电！你好，2016！", "电子科技大学60周年校庆，等你来助力",
				"周涛教授当选“2015年度十大创新人物”", "副主席、副总理点赞“成电造”机器人",
				"白岩松登成电讲坛谈“打造更好的自己”" };

		images = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);

			images.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot_0));
		dots.add(findViewById(R.id.dot_1));
		dots.add(findViewById(R.id.dot_2));
		dots.add(findViewById(R.id.dot_3));
		dots.add(findViewById(R.id.dot_4));

		title = (TextView) findViewById(R.id.title);
		title.setText(titles[0]);

		mViewPager = (ViewPager) findViewById(R.id.vp);

		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				title.setText(titles[position]);

				dots.get(oldPosition).setBackgroundResource(
						R.drawable.dot_normal);
				dots.get(position)
						.setBackgroundResource(R.drawable.dot_focused);

				oldPosition = position;
				currentItem = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void findView() {
		mnotice = (Button) findViewById(R.id.notice);
		mactivity = (Button) findViewById(R.id.activity);
		mclasssituation = (Button) findViewById(R.id.class_situation);
		mmysettings = (Button) findViewById(R.id.my_settings);

	}

	private void init() {

		OnClickListener noticeOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						NoticeActivity.class);
				startActivity(intent);
			}
		};
		mnotice.setOnClickListener(noticeOnClickListener);

		OnClickListener activityOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						ActivityActivity.class);
				startActivity(intent);
			}
		};
		mactivity.setOnClickListener(activityOnClickListener);

		OnClickListener myclasssituationOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						ClassSituationActivity.class);
				startActivity(intent);
			}
		};
		mclasssituation.setOnClickListener(myclasssituationOnClickListener);
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			// view.removeViewAt(position);
			view.removeView(images.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			// TODO Auto-generated method stub
			view.addView(images.get(position));

			return images.get(position);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2,
				2, TimeUnit.SECONDS);
	}

	private class ViewPagerTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			currentItem = (currentItem + 1) % imageIds.length;

			// handler.sendEmptyMessage(0);
			handler.obtainMessage().sendToTarget();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			mViewPager.setCurrentItem(currentItem);
			// System.out.println(currentItem);
		}

	};

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
