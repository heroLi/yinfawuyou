package com.yifa.health_manage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yifa.health_manage.adapter.BloodListAdapter;
import com.yifa.health_manage.fragment.ChartFragment;
import com.yifa.health_manage.model.BloodValuesInfo;

/**
 * 这线图
 * */
public class ChartActivity extends FragmentActivity implements OnClickListener {

	private ListView listView;
	public List<BloodValuesInfo> mCurrentList = new ArrayList<BloodValuesInfo>();
	public BloodListAdapter adapter;

	private ImageButton menuList;
	
	private  LinearLayout listLayout;
	
	private boolean isList = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_layout);
		initView();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.chart_layout, new ChartFragment()).commit();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.myList);
		menuList = (ImageButton) findViewById(R.id.activity_top_menu1);
		listLayout = (LinearLayout) findViewById(R.id.listLayuot);

		for (int i = 0; i < 40; i++) {
			mCurrentList.add(new BloodValuesInfo());
		}

		menuList.setOnClickListener(this);
		adapter = new BloodListAdapter(this, mCurrentList);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_top_menu1:
			if(isList){
				isList = false;
				listLayout.setVisibility(View.VISIBLE);
			}else{
				isList = true;
				listLayout.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}
	
	public void setListData(List<BloodValuesInfo> mList)
	{
		adapter.setData(mList);
	}
}
