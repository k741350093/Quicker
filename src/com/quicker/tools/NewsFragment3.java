package com.quicker.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.quicker.R;
import com.quicker.R.layout;
import com.quicker.adapter.PinnedHeaderExpandableAdapter1;
import com.quicker.views.PinnedHeaderExpandableListView;

public class NewsFragment3 extends Fragment {

	private static final String ARG_POSITION = "position";

	private int position;
	private Fragment fragment1;

	private PinnedHeaderExpandableListView explistview;
	private String[][] childrenData = new String[4][10];
	private String[] groupData = new String[3];
	private int expandFlag = -1;
	private PinnedHeaderExpandableAdapter1 adapter;
	
	private View container = null;
	

	public static NewsFragment3 newInstance(int position) {
		NewsFragment3 f = new NewsFragment3();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		
		
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.container = inflater.inflate(R.layout.fragment, container, false);
		
		initView(); 
		initData(); 
		
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//		FrameLayout fl = new FrameLayout(getActivity());
//		fl.setLayoutParams(params);
//
//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
//				.getDisplayMetrics());
//		View view = View.inflate(getActivity(),layout.fragment, null);
		
		return this.container;
		

		/*TextView v = new TextView(getActivity());
		params.setMargins(margin, margin, margin, margin);
		
		
		
		v.setLayoutParams(params);
		v.setGravity(Gravity.CENTER);
		v.setText("PAGE " + (position + 1));

		fl.addView(v);*/
		//return fl;
	}


	private void initData() {
		groupData[0] = "未完成";
		groupData[1] = "已完成";
		groupData[2] = "我的收藏";
		
	
		for(int i=0;i<4;i++){
			for(int j=0;j<10;j++){
				childrenData[i][j] = "木叶飞舞的地方，火就会燃烧，火之意志就会传承。"+i+"-"+j;
			}
		}
		childrenData[0][4]="2016年寒假留校学生登记表";
		childrenData[0][5]="2016年寒校学生返校时间登记表";
		childrenData[0][6]="2016年寒假留校学生登记表";
		childrenData[0][7]="2016年寒假离校学生离校时间与离校方式登记表";
		childrenData[0][8]="2016年春季信软学院教材征订登记表";
		childrenData[0][9]="2015年第4届电子科技大学“长虹杯”软件设计竞赛报名表";
		
		explistview.setHeaderView(getActivity().getLayoutInflater().inflate(R.layout.group_head,
				explistview, false));
		adapter = new PinnedHeaderExpandableAdapter1(childrenData, groupData, getActivity().getApplicationContext(),explistview);
		explistview.setAdapter(adapter);
		
		//璁剧疆鍗曚釜鍒嗙粍灞曞紑
		//explistview.setOnGroupClickListener(new GroupClickListener());
	}


	private void initView() {
		explistview = (PinnedHeaderExpandableListView)container.findViewById(R.id.explistview);
	}

	class GroupClickListener implements OnGroupClickListener{
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			if (expandFlag == -1) {
				// 灞曞紑琚�夌殑group
				explistview.expandGroup(groupPosition);
				// 璁剧疆琚�変腑鐨刧roup缃簬椤剁
				explistview.setSelectedGroup(groupPosition);
				expandFlag = groupPosition;
			} else if (expandFlag == groupPosition) {
				explistview.collapseGroup(expandFlag);
				expandFlag = -1;
			} else {
				explistview.collapseGroup(expandFlag);
				// 灞曞紑琚�夌殑group
				explistview.expandGroup(groupPosition);
				// 璁剧疆琚�変腑鐨刧roup缃簬椤剁
				explistview.setSelectedGroup(groupPosition);
				expandFlag = groupPosition;
			}
			return true;
		}
	}
}