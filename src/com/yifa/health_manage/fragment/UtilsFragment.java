package com.yifa.health_manage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yifa.health_manage.R;
import com.yifa.health_manage.UtilsAllActivity;

/**
 * 健康工具
 * */
public class UtilsFragment extends Fragment implements OnClickListener {

	private LinearLayout allUtils, layout0, layout1, layout2, layout3, layout4,
			layout5;

	private TextView text1, text2, text3, text4, text5, text6;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_utils, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		allUtils = (LinearLayout) view.findViewById(R.id.utils_all);
		layout0 = (LinearLayout) view.findViewById(R.id.layout0);
		layout1 = (LinearLayout) view.findViewById(R.id.layout1);
		layout2 = (LinearLayout) view.findViewById(R.id.layout2);
		layout3 = (LinearLayout) view.findViewById(R.id.layout3);
		layout4 = (LinearLayout) view.findViewById(R.id.layout4);
		layout5 = (LinearLayout) view.findViewById(R.id.layout5);
		text1 = (TextView) view.findViewById(R.id.text1);
		text2 = (TextView) view.findViewById(R.id.text2);
		text3 = (TextView) view.findViewById(R.id.text3);
		text4 = (TextView) view.findViewById(R.id.text4);
		text5 = (TextView) view.findViewById(R.id.text5);
		text6 = (TextView) view.findViewById(R.id.text6);
		allUtils.setOnClickListener(this);
//		layout0.setOnClickListener(this);
//		layout1.setOnClickListener(this);
//		layout2.setOnClickListener(this);
//		layout3.setOnClickListener(this);
//		layout4.setOnClickListener(this);
//		layout5.setOnClickListener(this);
		text1.setOnClickListener(this);
		text2.setOnClickListener(this);
		text3.setOnClickListener(this);
		text4.setOnClickListener(this);
		text5.setOnClickListener(this);
		text6.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 10 && resultCode == 100) {
			String[] ss = data.getStringArrayExtra("data");
			text1.setText(ss[0]);
			text2.setText(ss[1]);
			text3.setText(ss[2]);
			text4.setText(ss[3]);
			text5.setText(ss[4]);
			text6.setText(ss[5]);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), UtilsAllActivity.class);
		switch (v.getId()) {
		case R.id.text1:
			intent.putExtra("type", 0);
			break;
		case R.id.text2:
			intent.putExtra("type", 1);
			break;
		case R.id.text3:
			intent.putExtra("type", 2);
			break;
		case R.id.text4:
			intent.putExtra("type", 3);
			break;
		case R.id.text5:
			intent.putExtra("type", 4);
			break;
		case R.id.text6:
			intent.putExtra("type", 5);
			break;
		case R.id.utils_all:
			intent.putExtra("type", 6);
			break;

		default:
			break;
		}
		startActivity(intent);
	}
}
