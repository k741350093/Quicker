package com.quicker.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.quicker.R;
import com.quicker.adapter.ActivityAdapter;

public class ActivityFragment extends Fragment {
	private static final String ARG_POSITION = "position";
	private int position;
	private View container = null;
	private List<String> myActivityList = new ArrayList<String>();
	private List<String> hotActivityList = new ArrayList<String>();
	private List<String> newsActivityList = new ArrayList<String>();
	private List<String> AllDepartments = new ArrayList<String>();
	private ProgressDialog dialog;
	private ActivityAdapter activityAdapter;
	private ListView activityListView;

	private RadioGroup radiogroup;
	private RequestQueue mQueue;
	private RadioButton hot;// 获取所有活动（热度排序）
	private RadioButton time;// 获取所有活动（时间排序）

	public static ActivityFragment newInstance(int position) {
		ActivityFragment f = new ActivityFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		dialog = new ProgressDialog(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.container = inflater.inflate(R.layout.activity_fragment,
				container, false);
		// initListView();
		initData();
		return this.container;
	}

	private void initData() {
		// TODO 从服务器获取活动
		mQueue = Volley.newRequestQueue(getActivity());
		activityListView = (ListView) container
				.findViewById(R.id.activity_list_view);
		radiogroup = (RadioGroup) container.findViewById(R.id.radio_group);
		hot = (RadioButton) container.findViewById(R.id.radio_button_hot);
		time = (RadioButton) container.findViewById(R.id.radio_button_time);

		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == hot.getId()) {// 按照热度排序
					dialog.setMessage("获取数据中");
					dialog.show();
					getHotActivities();// 获取所有活动，热度排序
				} else {// 按照时间排序
					dialog.setMessage("获取数据中");
					dialog.show();
					getNewstActivities();// 获取所有活动，时间排序
				}
			}
		});
		
		switch (position) {
		case 0:// 我的关注
			dialog.setMessage("获取数据中");
			dialog.show();
			getMyActivities();// 获取我的关注活动
			break;
		case 1:// 所有活动
			radiogroup.setVisibility(View.VISIBLE);
			dialog.setMessage("获取数据中");
			dialog.show();
			getHotActivities();// 获取所有活动，热度排序
			activityListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 跳转活动页面
					Toast.makeText(getActivity(), "activity"+position, Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case 2:// 部门组织
			dialog.setMessage("获取数据中");
			dialog.show();
			getAllDepartments();// 获取所有部门
			break;
		}
	}

	private void getMyActivities() {
		// TODO 获取我的关注活动
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getMyActivities/"
				+ StaticConstant.stuIdConstant;
		StringRequest getMyActivities = new StringRequest(Request.Method.POST,
				url, new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Gson gson = new Gson();
						Result result = gson.fromJson(s, Result.class);
						if (result.isStatus()) {
							Object obj = result.getJsonString();
							myActivityList = (List<String>) obj;
							activityAdapter = new ActivityAdapter(
									getActivity(), R.layout.activity_child,
									myActivityList);
							activityListView.setAdapter(activityAdapter);
							dialog.dismiss();
						} else {
							dialog.dismiss();
							Toast.makeText(getActivity(), "获取活动失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Log.d("error", arg0.toString());
					}
				});

		getMyActivities.setTag("POST");
		mQueue.add(getMyActivities);
	}

	private void getHotActivities() {
		// TODO 获取所有活动，热度排序
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getHotActivities/";
		StringRequest getHotActivities = new StringRequest(Request.Method.POST,
				url, new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Gson gson = new Gson();
						Result result = gson.fromJson(s, Result.class);
						if (result.isStatus()) {
							Object obj = result.getJsonString();
							hotActivityList = (List<String>) obj;
							activityAdapter = new ActivityAdapter(
									getActivity(), R.layout.activity_child,
									hotActivityList);
							activityListView.setAdapter(activityAdapter);
							dialog.dismiss();
						} else {
							dialog.dismiss();
							Toast.makeText(getActivity(), "获取活动失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Log.d("error", arg0.toString());
					}
				});

		getHotActivities.setTag("POST");
		mQueue.add(getHotActivities);
	}

	protected void getNewstActivities() {
		// TODO 获取所有活动，时间排序
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getNewstActivities/";
		StringRequest getNewstActivities = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Gson gson = new Gson();
						Result result = gson.fromJson(s, Result.class);
						if (result.isStatus()) {
							Object obj = result.getJsonString();
							newsActivityList = (List<String>) obj;
							activityAdapter = new ActivityAdapter(
									getActivity(), R.layout.activity_child,
									newsActivityList);
							activityListView.setAdapter(activityAdapter);
							dialog.dismiss();
						} else {
							dialog.dismiss();
							Toast.makeText(getActivity(), "获取活动失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Log.d("error", arg0.toString());
					}
				});
		getNewstActivities.setTag("POST");
		mQueue.add(getNewstActivities);
	}

	private void getAllDepartments() {
		// TODO 获取所有部门
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getAllDepartments/";
		StringRequest getAllDepartments = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Gson gson = new Gson();
						Result result = gson.fromJson(s, Result.class);
						if (result.isStatus()) {
							Object obj = result.getJsonString();
							AllDepartments = (List<String>) obj;
							activityAdapter = new ActivityAdapter(
									getActivity(), R.layout.activity_child,
									AllDepartments);
							activityListView.setAdapter(activityAdapter);
							dialog.dismiss();
						} else {
							dialog.dismiss();
							Toast.makeText(getActivity(), "获取活动失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Log.d("error", arg0.toString());
					}
				});
		getAllDepartments.setTag("POST");
		mQueue.add(getAllDepartments);
	}

	public class Activity {
		private String activityTitle;
		private String activitycontentl;

		public Activity(String string, String string2) {
			// TODO Auto-generated constructor stub
			activityTitle = string;
			activitycontentl = string2;
		}

		public Activity(String string) {
			// TODO Auto-generated constructor stub
			activityTitle = string;
			activitycontentl = null;
		}

		public String getActivityTitle() {
			return activityTitle;
		}

		public void setActivityTitle(String activityTitle) {
			this.activityTitle = activityTitle;
		}

		public String getActivitycontentl() {
			return activitycontentl;
		}

		public void setActivitycontentl(String activitycontentl) {
			this.activitycontentl = activitycontentl;
		}

	}

}
