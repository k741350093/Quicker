package com.quicker.tools;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.INotificationSideChannel.Stub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.quicker.R;
import com.quicker.activity.FormActivity;
import com.quicker.activity.InformActivity;
import com.quicker.activity.NoticeActivity;
import com.quicker.adapter.PinnedHeaderExpandableAdapter1;
import com.quicker.adapter.PinnedHeaderExpandableAdapter2;
import com.quicker.adapter.PinnedHeaderExpandableAdapter3;
import com.quicker.views.PinnedHeaderExpandableListView;

public class NoticeFragment extends Fragment {
	private static final String ARG_POSITION = "position";
	private int position;
	private View container = null;
	private PinnedHeaderExpandableListView explistview;

	public static String[] oneForm;
	public static String[] formAnswer;// 已完成表格需要获取
	public static String[][] formTitle = new String[3][0];
	public static String[][] informChildrenData = new String[2][0];// 通知
	public static String[][] remindChildrenData = new String[2][6];// 提醒
	public static String informContent;// 通知内容，下一个活动调用
	private String[] groupData;
	private ProgressDialog dialog;// 登录等待窗口
	private PinnedHeaderExpandableAdapter1 adapter1;
	private PinnedHeaderExpandableAdapter2 adapter2;
	private PinnedHeaderExpandableAdapter3 adapter3;

	private int i = 0, j = 0;

	public static NoticeFragment newInstance(int position) {
		NoticeFragment f = new NoticeFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		Log.d("NoticeFragment", "start");
		dialog = new ProgressDialog(getActivity());
	}

	@Override
	public void onDestroy() {// 销毁当前碎片时执行。
		super.onDestroy();
		Log.d("Tag", "onDestroy");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.container = inflater.inflate(R.layout.fragment, container, false);
		initView();
		initData();
		return this.container;
	}

	@Override
	public void onResume() {
		super.onResume();
		switch (position) {
		case 0:
			getForms();// 获取表格名
			break;
		case 1:
			getInforms();// 获取通知
			break;
		case 2:
			getAllStageFormNotice();// 获取提醒
			break;
		}
	}

	private void initData() {
		if (position == 0) {
			Log.d("NoticeFragment", "0");
			groupData = new String[3];
			groupData[0] = "未完成";
			groupData[1] = "已完成";
			groupData[2] = "我的收藏";

			// 将表名展示出来
			explistview.setHeaderView(getActivity().getLayoutInflater()
					.inflate(R.layout.group_head, explistview, false));
			adapter1 = new PinnedHeaderExpandableAdapter1(formTitle, groupData,
					getActivity().getApplicationContext(), explistview);
			explistview.setAdapter(adapter1);
			explistview.setOnChildClickListener(new OnChildClickListener() {// 每张表格点击事件
						@Override
						public boolean onChildClick(ExpandableListView parent,
								View v, int groupPosition, int childPosition,
								long id) {
							dialog.setMessage("获取数据中");
							dialog.show();
							getOneForm(groupPosition, childPosition);// 获取一张表格（组号，表格号）
							return true;
						}
					});
		}

		else if (position == 1) {
			Log.d("NoticeFragment", "1");
			groupData = new String[2];
			groupData[0] = "未读";
			groupData[1] = "已读";
			// 将通知展示出来
			explistview.setHeaderView(getActivity().getLayoutInflater()
					.inflate(R.layout.group_head, explistview, false));
			adapter2 = new PinnedHeaderExpandableAdapter2(informChildrenData,
					groupData, getActivity().getApplicationContext(),
					explistview);
			explistview.setAdapter(adapter2);
			explistview.setOnChildClickListener(new OnChildClickListener() {// 每条通知点击事件
						@Override
						public boolean onChildClick(ExpandableListView parent,
								View v, int groupPosition, int childPosition,
								long id) {
							dialog.setMessage("获取数据中");
							dialog.show();
							getOneInform(groupPosition, childPosition);// 获取一条通知（组号，通知号）
							return true;
						}
					});
		}
	}

	private void getForms() {
		// TODO 获取所有表格的表名
		dialog.setMessage("获取数据中");
		dialog.show();
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getAllStageForms/"
				+ StaticConstant.stuIdConstant;
		StringRequest getForms = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Log.d("TAG", "" + s);
						dealForms(s);
						Log.d("onClick", "3" + s);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Log.d("error", arg0.toString());
					}
				});

		getForms.setTag("POST");
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(getForms);

	}

	protected void dealForms(String s) {
		// TODO 处理所有表格的表名
		Gson gson = new Gson();
		Log.d("onClick", "0" + s);
		Result result = gson.fromJson(s, Result.class);
		Log.d("onClick", "1" + s);
		if (result.isStatus()) {

			Object obj = result.getJsonString();
			List<List<String>> formList = (List<List<String>>) obj;
			for (int i = 0; i < formList.size(); i++) {
				formTitle[i] = new String[formList.get(i).size()];
				for (int j = 0; j < formList.get(i).size(); j++)
					formTitle[i][j] = formList.get(i).get(j);
			}
			dialog.dismiss();

		} else {
			dialog.dismiss();
			Log.d("onClick", "2" + s);
			Toast.makeText(getActivity(), "获取表格失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void getInforms() {
		// TODO 获取所有通知
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getAllStageNotice/"
				+ StaticConstant.stuIdConstant;
		StringRequest getInforms = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Log.d("inform", "" + s);
						dealInforms(s);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.d("infonrm--error", arg0.toString());
					}
				});

		getInforms.setTag("POST");
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(getInforms);
	}

	protected void dealInforms(String s) {
		// TODO 处理所有通知
		Gson gson = new Gson();
		Result result = gson.fromJson(s, Result.class);
		if (result.isStatus()) {
			Log.d("dealInformsr", result.toString());
			Object obj = result.getJsonString();
			List<List<String>> formList = (List<List<String>>) obj;
			for (int i = 0; i < formList.size(); i++) {
				informChildrenData[i] = new String[formList.get(i).size()];
				for (int j = 0; j < formList.get(i).size(); j++) {
					Log.d("dealInformsr---j=", j + "i=" + i);
					informChildrenData[i][j] = formList.get(i).get(j);
				}
			}

		} else {
			Toast.makeText(getActivity(), "获取通知失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void getAllStageFormNotice() {
		// TODO 获取所有提醒

	}

	protected void getOneForm(final int groupPosition, final int childPosition) {
		// TODO 获得一张表格
		String url;
		if (groupPosition == 0) {// 获取未完成的表格
			url = "http://" + StaticConstant.localhostConstant
					+ ":8080/quicker/app/getOneUnfinishedForm/"
					+ formTitle[groupPosition][childPosition];
		} else {// 获取已完成或者收藏的表格
			url = "http://" + StaticConstant.localhostConstant
					+ ":8080/quicker/app/getOneFinishedForm?id="
					+ StaticConstant.stuIdConstant + "&formTitle="
					+ formTitle[groupPosition][childPosition];
		}
		Log.d("NoticeFragment getOneForm() url", url);
		StringRequest loginRequest = new StringRequest(Request.Method.POST,
				url, new Response.Listener<String>() {// 2.new 一个请求
					@Override
					public void onResponse(String s) {
						Log.d("getOneForm()", "" + s);
						dealOneForm(s, groupPosition, childPosition);// 处理服务器返回的未完成的表格数据
						Log.d("getOneForm()", "3" + s);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Log.d("getOneForm()-0", "error");
						dialog.dismiss();
					}
				});
		loginRequest.setTag("POST");
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(loginRequest);
	}

	protected void dealOneForm(String s, int groupPosition, int childPosition) {
		// TODO 处理一张表格
		Gson gson = new Gson();
		Result result = gson.fromJson(s, Result.class);
		if (result.isStatus()) {
			if (groupPosition == 0) {// 处理未完成的表格
				Object obj = result.getJsonString();
				List<String> formList = (List<String>) obj;
				oneForm = new String[formList.size()];
				for (int i = 0; i < formList.size(); i++) {
					oneForm[i] = formList.get(i);
				}
				dialog.dismiss();
				// 收起所有项目，再打开时就会自动刷新子项目
				explistview.collapseGroup(0);
				explistview.collapseGroup(1);
				explistview.collapseGroup(2);
				Intent intent = new Intent(getActivity(), FormActivity.class);
				intent.putExtra("childPosition", childPosition);// 表格编号
				intent.putExtra("groupPosition", groupPosition);// 表格所在组号
				startActivity(intent);
			} else {// 处理已完成或者收藏的表格
				Object obj = result.getJsonString();
				List<List<String>> formList = (List<List<String>>) obj;
				Log.d("NoticeFragment", "formList=" + formList);
				Log.d("NoticeFragment", "formList.get(0).size()="
						+ formList.get(0).size());
				Log.d("NoticeFragment", "formList.get(1).size()="
						+ formList.get(1).size());
				oneForm = new String[formList.get(0).size()];
				formAnswer = new String[formList.get(1).size()];
				for (int i = 0; i < formList.get(0).size(); i++) {
					oneForm[i] = formList.get(0).get(i);
					formAnswer[i] = formList.get(1).get(i);
					Log.d("oneForm", "oneForm" + oneForm);
				}
				dialog.dismiss();
				// 收起所有项目，再打开时就会自动刷新子项目
				explistview.collapseGroup(0);
				explistview.collapseGroup(1);
				explistview.collapseGroup(2);
				Intent intent = new Intent(getActivity(), FormActivity.class);
				intent.putExtra("childPosition", childPosition);// 表格编号
				intent.putExtra("groupPosition", groupPosition);// 表格所在组号
				startActivity(intent);
			}
		} else {
			dialog.dismiss();
			Log.d("onClick", "2" + s);
			Toast.makeText(getActivity(), "获取表格失败", Toast.LENGTH_SHORT).show();
		}
	}

	protected void getOneInform(final int groupPosition, final int childPosition) {
		// TODO 获取一条通知
		String url = "http://" + StaticConstant.localhostConstant
				+ ":8080/quicker/app/getOneCounselorNotice?id="
				+ StaticConstant.stuIdConstant + "&noticeTitle="
				+ informChildrenData[groupPosition][childPosition];
		Log.d("NoticeFragment getOneForm() url", url);
		StringRequest loginRequest = new StringRequest(Request.Method.POST,
				url, new Response.Listener<String>() {
					public void onResponse(String s) {
						dealOneInform(s, groupPosition, childPosition);// 处理服务器返回的通知
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "获取通知失败 code:0",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						Log.e("error", StaticConstant.stuIdConstant);
					}
				});
		loginRequest.setTag("POST");
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(loginRequest);
	}

	protected void dealOneInform(String s, int groupPosition, int childPosition) {
		// TODO 处理服务器返回的通知
		Gson gson = new Gson();
		Result result = gson.fromJson(s, Result.class);
		if (result.isStatus()) {
			informContent = (String) result.getJsonString();
			dialog.dismiss();
			// 收起所有项目，再打开时就会自动刷新子项目
			explistview.collapseGroup(0);
			explistview.collapseGroup(1);
			explistview.collapseGroup(2);
			Intent intent = new Intent(getActivity(), InformActivity.class);
			intent.putExtra("childPosition", childPosition);// 通知编号
			intent.putExtra("groupPosition", groupPosition);// 通知所在组号
			startActivity(intent);
		} else {
			dialog.dismiss();
			Toast.makeText(getActivity(), "获取通知失败", Toast.LENGTH_SHORT).show();
		}

	}

	private void initView() {
		explistview = (PinnedHeaderExpandableListView) container
				.findViewById(R.id.explistview);
	}

	// class GroupClickListener implements OnGroupClickListener {
	// @Override
	// public boolean onGroupClick(ExpandableListView parent, View v,
	// int groupPosition, long id) {
	// if (expandFlag == -1) {
	// explistview.expandGroup(groupPosition);
	// explistview.setSelectedGroup(groupPosition);
	// expandFlag = groupPosition;
	// Log.d("Tag", "expandFlag=-1");
	// } else if (expandFlag == groupPosition) {
	// explistview.collapseGroup(expandFlag);
	// Log.d("Tag", "00expandFlag="+expandFlag);
	// expandFlag = -1;
	// } else {
	// explistview.collapseGroup(expandFlag);
	// explistview.expandGroup(groupPosition);
	// explistview.setSelectedGroup(groupPosition);
	// Log.d("Tag", "000expandFlag="+expandFlag);
	// expandFlag = groupPosition;
	// }
	// return true;
	// }
	// }
	/*
	 * public void DealResponseFromServer(String s) {
	 * 
	 * Gson gson = new Gson(); Log.d("onClick", "0" + s); Result result =
	 * gson.fromJson(s, Result.class); Log.d("onClick", "1" + s);
	 * 
	 * if (result.isStatus()) { Object obj = result.getJsonString();
	 * List<List<String>> formList = (List<List<String>>) obj;
	 * 
	 * formTitle[0] = new String[formList.size()]; formItem = new
	 * String[formList.size()][]; for (int i = 0; i < formList.size(); i++) {
	 * formItem[i] = new String[formList.get(i).size()]; }
	 * 
	 * // Log.d("debug: ","succeed~~~"); for (i = 0; i < formList.size(); i++) {
	 * eachForm = formList.get(i); for (j = 0; j < eachForm.size(); j++) {
	 * String v = eachForm.get(j); formItem[i][j] = v; } formTitle[0][i] =
	 * formItem[i][j - 1]; // formItem[i][j-1]=null; } } else { Log.d("onClick",
	 * "2" + s); Toast.makeText(getActivity(), "获取表格失败",
	 * Toast.LENGTH_SHORT).show(); } }
	 * 
	 * public void LoginToServer() { Log.d("TAG", "" + "000000000"); String url
	 * = "http://192.168.0.104:8080/quicker/app/getForm"; StringRequest
	 * loginRequest = new StringRequest(Request.Method.POST, url, new
	 * Response.Listener<String>() {// 2.new 一个请求
	 * 
	 * @Override public void onResponse(String s) { Log.d("TAG", "" + s);
	 * DealResponseFromServer(s); Log.d("onClick", "3" + s); } }, new
	 * Response.ErrorListener() {
	 * 
	 * @Override public void onErrorResponse(VolleyError arg0) { // TODO
	 * Auto-generated method stub Log.d("Json", "error"); }
	 * 
	 * });
	 * 
	 * loginRequest.setTag("POST"); RequestQueue mQueue =
	 * Volley.newRequestQueue(getActivity()); mQueue.add(loginRequest); }
	 */
}
