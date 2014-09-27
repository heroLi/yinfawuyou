package com.yifa.health_manage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yifa.health_manage.R;
import com.yifa.health_manage.UtilsAllActivity;

/**
 * 健康工具
 * */
public class UtilsFragment extends Fragment implements OnClickListener {

	private LinearLayout allUtils;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_utils, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		allUtils = (LinearLayout) view.findViewById(R.id.utils_all);
		allUtils.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(getActivity(), UtilsAllActivity.class));
	}
}
