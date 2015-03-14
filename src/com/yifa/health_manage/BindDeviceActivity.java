package com.yifa.health_manage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yifa.health_manage.db.DBManager;
import com.yifa.health_manage.model.DeviceFriendName;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 设备绑定界面
 * **/
public class BindDeviceActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private Button nextButton, topButton;

	private EditText deviceName;

	private TextView title;

	private boolean isBind = false;

	private RadioGroup group;
	private DBManager dbManager = null;
	private String name, pass, email;

	private String type = "blood_presure";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WebServiceParmas.BIND_DEVICE:
				if (msg.obj.toString().equalsIgnoreCase("")) {
					return;
				}
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());

					Gson gson = new Gson();
					ResultResponse response = gson.fromJson(
							jsonObject.toString(), ResultResponse.class);
					if (response.isResult()) {
						startActivity(new Intent(BindDeviceActivity.this,
								Main_board_Activity.class));
						setResult(0);
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case WebServiceParmas.REGISTER:
				if (msg.obj.toString().equalsIgnoreCase("")) {
					return;
				}
				try {
					JSONObject jsonObject2 = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					ResultResponse response = gson.fromJson(
							jsonObject2.toString(), ResultResponse.class);
					if (response.isResult()) {
						if (!SharePrefenceUtils.getAccount(
								BindDeviceActivity.this).equalsIgnoreCase(name)) {
							dbManager.deleteAll();
							SharePrefenceUtils.saveSugarFriendId(
									BindDeviceActivity.this,
									new DeviceFriendName());
							SharePrefenceUtils.savePressureFriendId(
									BindDeviceActivity.this,
									new DeviceFriendName());
						}
						SharePrefenceUtils.saveAccount(BindDeviceActivity.this,
								name);
						SharePrefenceUtils.savePassword(
								BindDeviceActivity.this, pass);
						if (isBind) {
							if (!deviceName.getText().toString().trim()
									.equalsIgnoreCase("")) {
								new WebServiceUtils(BindDeviceActivity.this,
										mHandler)
										.sendExecuteNo(
												new String[] {
														SharePrefenceUtils
																.getAccount(BindDeviceActivity.this),
														type,
														deviceName.getText()
																.toString()
																.trim() },
												WebServiceParmas.BIND_DEVICE,
												WebServiceParmas.HTTP_POST);
							} else {
								startActivity(new Intent(
										BindDeviceActivity.this,
										Main_board_Activity.class));
								setResult(0);
								finish();
							}
						} else {
							startActivity(new Intent(BindDeviceActivity.this,
									Main_board_Activity.class));
							setResult(0);
							finish();
						}
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_layout);
		dbManager = new DBManager(this);
		initView();
		initIntent();
		initLisenter();

	}

	private void initIntent() {
		name = getIntent().getStringExtra("name");
		pass = getIntent().getStringExtra("password");
		email = getIntent().getStringExtra("email");

	}

	private void initView() {

		nextButton = (Button) findViewById(R.id.register_btn_next);
		topButton = (Button) findViewById(R.id.register_btn_no);
		deviceName = (EditText) findViewById(R.id.bind_devices_name);
		title = (TextView) findViewById(R.id.activity_top_title);
		title.setText("绑定设备");
		group = (RadioGroup) findViewById(R.id.bind_group);
	}

	private void initLisenter() {
		nextButton.setOnClickListener(this);
		topButton.setOnClickListener(this);
		group.setOnCheckedChangeListener(this);

	}

	// f($device_type === 'blood_presure') else if ($device_type ===
	// 'blood_glucose')
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn_next:
			new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
					name.trim(), pass.trim(), email.trim() },
					WebServiceParmas.REGISTER, WebServiceParmas.HTTP_POST);
			break;
		case R.id.register_btn_no:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0) {
			setResult(0);
			finish();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bind_blood_glucose:
			type = "blood_glucose";
			isBind = true;
			deviceName.setEnabled(true);
			break;
		case R.id.bind_blood_presure:
			type = "blood_presure";
			isBind = true;
			deviceName.setEnabled(true);
			break;
		case R.id.bind_no:
			deviceName.setEnabled(false);
			isBind = false;
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbManager.close();
	}
}
