package com.yifa.health_manage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.adapter.BloodListAdapter;
import com.yifa.health_manage.adapter.FriendAdapter;
import com.yifa.health_manage.db.DBManager;
import com.yifa.health_manage.fragment.ChartFragment;
import com.yifa.health_manage.model.BloodValuesInfo;
import com.yifa.health_manage.model.DeviceFriendName;
import com.yifa.health_manage.model.DeviceInfo;
import com.yifa.health_manage.model.DevicesListInfo;
import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.MyLoger;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 这线图
 * */
public class ChartActivity extends FragmentActivity implements OnClickListener,
		OnCheckedChangeListener {

	private MyLoger loger = MyLoger.getInstence("ChartActivity");
	private ListView listView;
	public List<BloodValuesInfo> mCurrentList = new ArrayList<BloodValuesInfo>();
	public BloodListAdapter adapter;

	private LinearLayout listLayout;

	private boolean isList = true;

	private String deviceType = "";// 设备类型

	private ImageButton menuList, menufriend;

	private TextView title;

	private DBManager dbManager = null;

	private RadioGroup radioGroup;

	private DevicesListInfo listnew = null;

	private ChartFragment chartFragment = null;

	private RadioButton button1, button2, button3;

	private int type = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WebServiceParmas.GET_DEVICE_FRIEND:
				loger.d(msg.obj.toString());
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					Type type = new TypeToken<DevicesListInfo>() {
					}.getType();
					listnew = gson.fromJson(jsonObject.toString(),
							DevicesListInfo.class);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (chartFragment == null) {
					chartFragment = new ChartFragment(deviceType, 0);
				}
				getSupportFragmentManager().beginTransaction()
						.add(R.id.chart_layout, chartFragment).commit();
				if (listnew == null || listnew.getData().size() <= 0) {
					// AndroidUtils.showToast(ChartActivity.this, "请添加用户设备");
					return;
				}
				if (listnew.getData().get(0).getRelative() == null
						|| listnew.getData().get(0).getRelative().size() <= 0) {
					return;
				}
				if (deviceType.equalsIgnoreCase("blood_glucose")) {
					if (SharePrefenceUtils.getSugarFriendId(ChartActivity.this)
							.getId().equalsIgnoreCase("")) {
						listnew.getData()
								.get(0)
								.getRelative()
								.get(0)
								.setDevice_sn(
										listnew.getData().get(0).getDevice_sn());
						SharePrefenceUtils.saveSugarFriendId(
								ChartActivity.this, listnew.getData().get(0)
										.getRelative().get(0));
					}
				} else {
					if (SharePrefenceUtils
							.getPressureFriendId(ChartActivity.this).getId()
							.equalsIgnoreCase("")) {
						listnew.getData()
								.get(0)
								.getRelative()
								.get(0)
								.setDevice_sn(
										listnew.getData().get(0).getDevice_sn());
						SharePrefenceUtils.savePressureFriendId(
								ChartActivity.this, listnew.getData().get(0)
										.getRelative().get(0));
					}
				}
				initDataBase();
				break;

			default:
				break;
			}

		};
	};
	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_layout);
		deviceType = getIntent().getStringExtra("device_type");
		dbManager = new DBManager(this);
		initView();
		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			layout.setBackgroundColor(Color.parseColor("#1f65c4"));

		} else if (deviceType.equalsIgnoreCase("blood_presure")) {
			layout.setBackgroundColor(Color.parseColor("#ff5c3d"));
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			layout.setBackgroundColor(Color.parseColor("#e63a6c"));
		}
		if (deviceType.equalsIgnoreCase("heart_rate")) {
			new WebServiceUtils(this, mHandler).sendExecute(new String[] {
					SharePrefenceUtils.getAccount(this), "blood_presure" },
					WebServiceParmas.GET_DEVICE_FRIEND,
					WebServiceParmas.HTTP_POST, "加载中...");
		} else
			new WebServiceUtils(this, mHandler).sendExecute(new String[] {
					SharePrefenceUtils.getAccount(this), deviceType },
					WebServiceParmas.GET_DEVICE_FRIEND,
					WebServiceParmas.HTTP_POST, "加载中...");
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.myList);
		menuList = (ImageButton) findViewById(R.id.activity_top_menu1);
		menufriend = (ImageButton) findViewById(R.id.activity_top_menu2);
		title = (TextView) findViewById(R.id.activity_top_title);
		radioGroup = (RadioGroup) findViewById(R.id.chart_group);
		button1 = (RadioButton) findViewById(R.id.chart_week);
		button2 = (RadioButton) findViewById(R.id.chart_month);
		button3 = (RadioButton) findViewById(R.id.chart_year);
		layout = (RelativeLayout) findViewById(R.id.main_layout);
		menuList.setVisibility(View.VISIBLE);
		menufriend.setVisibility(View.VISIBLE);

		listLayout = (LinearLayout) findViewById(R.id.listLayuot);

		for (int i = 0; i < 40; i++) {
			mCurrentList.add(new BloodValuesInfo());
		}
		radioGroup.setOnCheckedChangeListener(this);
		menuList.setOnClickListener(this);
		menufriend.setOnClickListener(this);

		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			title.setText("血糖");
			type = 1;
			ColorStateList colorStateList = getResources().getColorStateList(
					R.drawable.text_selelct_xuetang);
			button1.setTextColor(colorStateList);
			button2.setTextColor(colorStateList);
			button3.setTextColor(colorStateList);

		} else if (deviceType.equalsIgnoreCase("blood_presure")) {
			title.setText("血压");
			ColorStateList colorStateList = getResources().getColorStateList(
					R.drawable.text_select_xueya);
			button1.setTextColor(colorStateList);
			button2.setTextColor(colorStateList);
			button3.setTextColor(colorStateList);
			type = 0;
		} else {
			title.setText("心率");
			ColorStateList colorStateList = getResources().getColorStateList(
					R.drawable.text_select_xinlv);
			button1.setTextColor(colorStateList);
			button2.setTextColor(colorStateList);
			button3.setTextColor(colorStateList);
			type = 3;
		}
		adapter = new BloodListAdapter(this, mCurrentList, type);
		listView.setAdapter(adapter);

		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
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
		case R.id.activity_top_menu2:
			showDialog(deviceType);
			break;

		default:
			break;
		}
	}

	public void setListData(List<BloodValuesInfo> mList) {
		adapter.setData(mList);
	}

	private Dialog dialog = null;

	@SuppressLint("InflateParams")
	private void showDialog(String type) {
		if (type.equalsIgnoreCase("heart_rate")) {
			type = "blood_presure";
		}
		List<UserInfo> agoList = dbManager.quaryAll(type);
		if (agoList == null || agoList.size() <= 0) {
			AndroidUtils.showToast(ChartActivity.this, "请添加用户设备");
			return;
		}
		loger.d("showDialog  " + agoList.size());
		dialog = new Dialog(this, R.style.ThemeDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,
				null);
		ListView listView = (ListView) view.findViewById(R.id.myList);

		final FriendAdapter adapter = new FriendAdapter(ChartActivity.this,
				agoList);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserInfo info = (UserInfo) adapter.getItem(position);
				DeviceFriendName dfName = new DeviceFriendName();
				dfName.setDevice_sn(info.getDevice_sn());
				dfName.setId(info.getFriend_id());
				dfName.setName(info.getName());
				if (deviceType.equalsIgnoreCase("blood_glucose")) {
					SharePrefenceUtils.saveSugarFriendId(ChartActivity.this,
							dfName);
				} else {
					SharePrefenceUtils.savePressureFriendId(ChartActivity.this,
							dfName);
				}
				dialog.dismiss();
				chartFragment.loadData();
			}
		});
		listView.setAdapter(adapter);
		dialog.setContentView(view);
		dialog.show();

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.chart_week:
			if (chartFragment != null) {
				chartFragment.reflashData(0);
			}
			break;
		case R.id.chart_month:
			if (chartFragment != null) {
				chartFragment.reflashData(1);
			}
			break;
		case R.id.chart_year:
			if (chartFragment != null) {
				chartFragment.reflashData(2);
			}
			break;

		default:
			break;
		}
	}

	private void initDataBase() {

		List<UserInfo> mList = new ArrayList<UserInfo>();
		int i = 0;
		for (DeviceInfo info : listnew.getData()) {
			if (i != 0)
				i = 2;
			for (DeviceFriendName name : info.getRelative()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setDevice_sn(info.getDevice_sn());
				userInfo.setFriend_id(name.getId());
				userInfo.setName(name.getName());
				userInfo.setType(deviceType);
				userInfo.setLayoutId(i + "");
				i++;
				mList.add(userInfo);
			}
		}

		List<UserInfo> agoList = dbManager.quaryAll(deviceType);
		if (agoList.size() <= 0) {
			dbManager.insertAll(mList);
		} else {
			for (UserInfo userInfo : mList) {
				if (!userInfo.getDevice_sn().equalsIgnoreCase("")
						&& !userInfo.getFriend_id().equalsIgnoreCase("")) {
					if (dbManager.quaryId(userInfo.getDevice_sn(),deviceType,
							userInfo.getFriend_id()) == null) {
						dbManager.insert(deviceType, userInfo.getDevice_sn(),
								userInfo.getFriend_id(), userInfo);
					} else {
						if (!dbManager
								.quaryId(userInfo.getDevice_sn(),deviceType,
										userInfo.getFriend_id()).getLayoutId()
								.equalsIgnoreCase(userInfo.getLayoutId())) {
							dbManager.updateType(userInfo);

						}
					}
				}
			}

		}
		if (listnew.getData().size() > 0) {
			dbManager.deleteOther(listnew.getData());
		} else {
			dbManager.deleteDeviceType(deviceType);
		}

		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			if (!SharePrefenceUtils.getSugarFriendId(ChartActivity.this)
					.getId().equalsIgnoreCase("")) {
				if (dbManager.quaryFriendId(deviceType, SharePrefenceUtils
						.getPressureFriendId(ChartActivity.this).getId()) == null) {
					listnew.getData()
							.get(0)
							.getRelative()
							.get(0)
							.setDevice_sn(
									listnew.getData().get(0).getDevice_sn());
					SharePrefenceUtils.saveSugarFriendId(ChartActivity.this,
							listnew.getData().get(0).getRelative().get(0));
				}

			}
		} else {
			if (!SharePrefenceUtils.getPressureFriendId(ChartActivity.this)
					.getId().equalsIgnoreCase("")) {
				if (dbManager.quaryFriendId(deviceType, SharePrefenceUtils
						.getPressureFriendId(ChartActivity.this).getId()) == null) {
					listnew.getData()
							.get(0)
							.getRelative()
							.get(0)
							.setDevice_sn(
									listnew.getData().get(0).getDevice_sn());
					SharePrefenceUtils.savePressureFriendId(ChartActivity.this,
							listnew.getData().get(0).getRelative().get(0));
				}

			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbManager.close();
	}
}
