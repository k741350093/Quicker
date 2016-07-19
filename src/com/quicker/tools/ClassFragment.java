package com.quicker.tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quicker.R;
import com.quicker.adapter.PinnedHeaderExpandableAdapter1;
import com.quicker.views.PinnedHeaderExpandableListView;

public class ClassFragment extends Fragment {

	private static final String ARG_POSITION = "position";

	private int position;


	
	
	private View container = null;
	

	public static ClassFragment newInstance(int position) {
		ClassFragment f = new ClassFragment();
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

		this.container = inflater.inflate(R.layout.class_fragment, container, false);
		
		
		
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

}
