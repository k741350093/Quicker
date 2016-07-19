package com.quicker.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.quicker.R;

public class ActivityAdapter extends ArrayAdapter<String> {
	private int resourceId;

	public ActivityAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String title = getItem(position); // 获取当前项的Activity实例
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView activityTitle = (TextView) view
				.findViewById(R.id.activity_title);
		activityTitle.setText(title);
		return view;
	}
}
