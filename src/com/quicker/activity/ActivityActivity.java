package com.quicker.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.quicker.R;
import com.quicker.activity.ActivityActivity.MyPagerAdapter;
import com.quicker.tools.ActivityFragment;
import com.quicker.tools.NoticeFragment;
import com.quicker.views.CategoryTabStrip2;

public class ActivityActivity extends FragmentActivity {
	private CategoryTabStrip2 tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("ActivityActivity", "start");

		tabs = (CategoryTabStrip2) findViewById(R.id.category_strip2);
		pager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final List<String> catalogs = new ArrayList<String>();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			catalogs.add(getString(R.string.activity1));
			catalogs.add(getString(R.string.activity2));
			catalogs.add(getString(R.string.activity3));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return catalogs.get(position);
		}

		@Override
		public int getCount() {
			return catalogs.size();
		}

		@Override
		public Fragment getItem(int position) {
			return ActivityFragment.newInstance(position);
		}

	}

}
