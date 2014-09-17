package com.yifa.health_manage;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yifa.health_manage.adapter.BloodListAdapter;
import com.yifa.health_manage.fragment.ChartFragment;
import com.yifa.health_manage.model.BloodValuesInfo;
import com.yifa.health_manage.model.DeviceFriendName;
import com.yifa.health_manage.util.SharePrefenceUtils;

/**
 * 这线图
 * */
public class ChartActivity extends FragmentActivity implements OnClickListener {

	private ListView listView;
	public List<BloodValuesInfo> mCurrentList = new ArrayList<BloodValuesInfo>();
	public BloodListAdapter adapter;

	private LinearLayout listLayout;

	private boolean isList = true;

	private String deviceType = "";// 设备类型

	private ImageButton menuList, menufriend;

	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_layout);
		deviceType = getIntent().getStringExtra("device_type");
		initView();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.chart_layout, new ChartFragment(deviceType,0)).commit();
		SharePrefenceUtils.saveSugarFriendId(this, "12345");
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.myList);
		menuList = (ImageButton) findViewById(R.id.activity_top_menu1);
		menufriend = (ImageButton) findViewById(R.id.activity_top_menu2);
		title = (TextView) findViewById(R.id.activity_top_title);
		menuList.setVisibility(View.VISIBLE);
		menufriend.setVisibility(View.VISIBLE);

		listLayout = (LinearLayout) findViewById(R.id.listLayuot);

		for (int i = 0; i < 40; i++) {
			mCurrentList.add(new BloodValuesInfo());
		}

		menuList.setOnClickListener(this);
		adapter = new BloodListAdapter(this, mCurrentList);
		listView.setAdapter(adapter);
		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			title.setText("血糖");
		} else if (deviceType.equalsIgnoreCase("blood_presure")) {
			title.setText("血压");
		} else {
			title.setText("心率");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_top_menu1:
			if (isList) {
				isList = false;
				menuList.setBackgroundResource(R.drawable.top_menu_1_chart);
				listLayout.setVisibility(View.VISIBLE);
			} else {
				isList = true;
				menuList.setBackgroundResource(R.drawable.top_menu_1);
				listLayout.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	public void setListData(List<BloodValuesInfo> mList) {
		adapter.setData(mList);
	}

	private Dialog dialog = null;

	private void showDialog() {
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,
				null);
		ListView listView = (ListView) view.findViewById(R.id.myList);
		dialog.setContentView(view);
		dialog.show();

	}

	class FriendAdapter extends BaseAdapter {

		private List<DeviceFriendName> mList;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ChartActivity.this).inflate(
						R.layout.item_dialog, null);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView name;
		ImageView photo, click;
	}
}
