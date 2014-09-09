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

	private RadioGroup group;

	private String type = "blood_presure";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.obj.toString().equalsIgnoreCase("")) {
				return;
			}
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(msg.obj.toString());

				Gson gson = new Gson();
				ResultResponse response = gson.fromJson(jsonObject.toString(),
						ResultResponse.class);
				if (response.isResult()) {
					startActivity(new Intent(BindDeviceActivity.this,
							Main_board_Activity.class));
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_layout);
		initView();
		initLisenter();
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
			if (!deviceName.getText().toString().trim().equalsIgnoreCase("")) {
				new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
						SharePrefenceUtils.getAccount(BindDeviceActivity.this),type,
						deviceName.getText().toString().trim() },
						WebServiceParmas.BIND_DEVICE,
						WebServiceParmas.HTTP_POST);
			} else {
				startActivity(new Intent(this, Main_board_Activity.class));
			}

			break;
		case R.id.register_btn_no:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bind_blood_glucose:
			type = "blood_glucose";
			break;
		case R.id.bind_blood_presure:
			type = "blood_presure";
			break;

		default:
			break;
		}

	}
}
