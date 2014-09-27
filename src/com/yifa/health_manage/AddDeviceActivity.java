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
import android.widget.TextView;

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 添加设备
 * */
public class AddDeviceActivity extends Activity implements OnClickListener {

	private TextView title;

	private Button addButton;

	private EditText deviceName;

	private String type;

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
					setResult(11);
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
		setContentView(R.layout.activity_add_device);
		initView();
		type = getIntent().getStringExtra("deviceType");
	}

	private void initView() {

		addButton = (Button) findViewById(R.id.add_button);
		deviceName = (EditText) findViewById(R.id.bind_devices_name);
		title = (TextView) findViewById(R.id.activity_top_title);

		if (type.equalsIgnoreCase("blood_presure")) {
			title.setText("添加血压计");
		} else
			title.setText("添加血糖仪");
		initLisenter();
	}

	private void initLisenter() {
		addButton.setOnClickListener(this);

	}

	// f($device_type === 'blood_presure') else if ($device_type ===
	// 'blood_glucose')
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn_next:
			if (!deviceName.getText().toString().trim().equalsIgnoreCase("")) {
				new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
						SharePrefenceUtils.getAccount(AddDeviceActivity.this),
						type, deviceName.getText().toString().trim() },
						WebServiceParmas.BIND_DEVICE,
						WebServiceParmas.HTTP_POST);
			} else {
				AndroidUtils.showToast(this, "请填写设备编码");
			}

			break;
		case R.id.activity_top_title:
			finish();
			break;

		default:
			break;
		}

	}

}
