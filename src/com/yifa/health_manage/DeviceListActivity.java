package com.yifa.health_manage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.adapter.DevicesAdapter;
import com.yifa.health_manage.model.DeviceInfo;
import com.yifa.health_manage.model.DevicesListInfo;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.MyLoger;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 设备列表
 * */
public class DeviceListActivity extends Activity implements
		OnLongClickListener, OnClickListener {

	private String deviceType = "blood_presure";

	private ListView myList;

	private DevicesAdapter adapter;

	private LinearLayout layout1, layout2;

	private TextView deviceId1, deviceId2, friend1, friend2, friend11,
			friend12, title;

	private CheckBox deviceCheck1, deviceCheck2;

	private Button delete;

	private int isSum = 0;

	private String device_id;

	private DevicesListInfo listnew;

	private MyLoger loger = MyLoger.getInstence("DeviceListActivity");

	private List<DeviceInfo> list = new ArrayList<DeviceInfo>();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.obj.toString().equalsIgnoreCase("")) {
				return;
			}
			loger.d(msg.obj.toString());
			switch (msg.what) {
			case WebServiceParmas.GET_DEVICE_FRIEND:
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					Type type = new TypeToken<DevicesListInfo>() {
					}.getType();
					listnew = gson.fromJson(jsonObject.toString(),
							DevicesListInfo.class);

					if (listnew == null) {
						layout1.setVisibility(View.GONE);
						layout2.setVisibility(View.GONE);
						isSum = 0;
					} else {

						if (listnew.getData().size() == 2) {
							if (listnew.getData().get(0).getRelative().size() <= 0) {
								return;
							}
							deviceId1.setText(listnew.getData().get(0)
									.getDevice_sn());
							friend1.setText(listnew.getData().get(0)
									.getRelative().get(0).getName());
							friend2.setText(listnew.getData().get(0)
									.getRelative().get(1).getName());

							deviceId2.setText(listnew.getData().get(1)
									.getDevice_sn());
							friend11.setText(listnew.getData().get(1)
									.getRelative().get(0).getName());
							friend12.setText(listnew.getData().get(1)
									.getRelative().get(1).getName());
							isSum = 2;
							layout1.setOnLongClickListener(DeviceListActivity.this);
							layout2.setOnLongClickListener(DeviceListActivity.this);

						} else if (listnew.getData().size() == 1) {
							if (listnew.getData().get(0).getRelative().size() <= 0) {
								return;
							}
							layout2.setVisibility(View.INVISIBLE);
							isSum = 1;
							deviceId1.setText(listnew.getData().get(0)
									.getDevice_sn());
							friend1.setText(listnew.getData().get(0)
									.getRelative().get(0).getName());
							friend2.setText(listnew.getData().get(0)
									.getRelative().get(1).getName());
							layout1.setOnLongClickListener(DeviceListActivity.this);
						} else {
							isSum = 1;
							layout1.setVisibility(View.GONE);
							layout2.setVisibility(View.GONE);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case WebServiceParmas.DELETE_DEVICE:
				try {
					JSONObject jsonObject2 = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					ResultResponse response = gson.fromJson(
							jsonObject2.toString(), ResultResponse.class);
					if (response.isResult()) {
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devices_layout_new);
		initView();
		new WebServiceUtils(DeviceListActivity.this, mHandler).sendExecuteNo(
				new String[] {
						SharePrefenceUtils.getAccount(DeviceListActivity.this),
						deviceType }, WebServiceParmas.GET_DEVICE_FRIEND,
				WebServiceParmas.HTTP_POST);

	}

	private void initView() {
		// myList = (ListView) findViewById(R.id.myList);
		// adapter = new DevicesAdapter(this, list);
		// myList.setAdapter(adapter);
		// myList.setOnItemLongClickListener(this);

		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		title = (TextView) findViewById(R.id.activity_top_title);
		deviceId1 = (TextView) findViewById(R.id.device_id);
		deviceId2 = (TextView) findViewById(R.id.device_id2);
		friend1 = (TextView) findViewById(R.id.device_friend1_name);
		friend2 = (TextView) findViewById(R.id.device_friend2_name);
		friend11 = (TextView) findViewById(R.id.device1_friend1_name);
		friend12 = (TextView) findViewById(R.id.device2_friend2_name);
		delete = (Button) findViewById(R.id.commit);
		deviceCheck1 = (CheckBox) findViewById(R.id.device_check);
		deviceCheck2 = (CheckBox) findViewById(R.id.device2_check2);
		title.setText("血压仪");
	}

	private void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onLongClick(View v) {
		if (isSum == 1) {
			delete.setVisibility(View.VISIBLE);
			deviceCheck1.setVisibility(View.VISIBLE);
		} else {
			delete.setVisibility(View.VISIBLE);
			deviceCheck1.setVisibility(View.VISIBLE);
			deviceCheck2.setVisibility(View.VISIBLE);

		}
		deviceCheck1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					device_id = listnew.getData().get(0).getDevice_sn();
					deviceCheck2.setChecked(false);
				}

			}
		});
		deviceCheck2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					device_id = listnew.getData().get(1).getDevice_sn();
					deviceCheck1.setChecked(false);
				}
			}
		});
		delete.setOnClickListener(this);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			new WebServiceUtils(DeviceListActivity.this, mHandler)
					.sendExecuteNo(
							new String[] {
									SharePrefenceUtils
											.getAccount(DeviceListActivity.this),
									deviceType, device_id },
							WebServiceParmas.DELETE_DEVICE,
							WebServiceParmas.HTTP_POST);

			break;

		default:
			break;
		}

	}
}
