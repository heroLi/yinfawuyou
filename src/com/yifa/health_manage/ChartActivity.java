package com.yifa.health_manage;

import com.yifa.health_manage.fragment.ChartFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/**
 * 这线图
 * */
public class ChartActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_layout);

		getSupportFragmentManager().beginTransaction()
				.add(R.id.chart_layout, new ChartFragment()).commit();
	}
}
