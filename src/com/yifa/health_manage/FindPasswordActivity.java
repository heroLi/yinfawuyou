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
 * 找回密码
 * **/
public class FindPasswordActivity extends Activity {

	private TextView title;
	private EditText nameEdit;
	private Button nextButton;
	private String ver = "";

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
					// startActivityForResult(new
					// Intent(FindPasswordActivity.this,
					// SetNewPasswordActivity.class),0);
					// SharePrefenceUtils.saveVer(FindPasswordActivity.this,
					// ver);
					AndroidUtils.showToast(FindPasswordActivity.this,
							"密码已发送到注册邮箱，请注意查收");
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
		setContentView(R.layout.activity_find_password);
		initView();
		setListener();
	}

	private void initView() {
		nameEdit = (EditText) findViewById(R.id.login_edit_name);
		nextButton = (Button) findViewById(R.id.register_btn_no);
		title = (TextView) findViewById(R.id.activity_top_title);
		title.setText("找回密码");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		setResult(0);
		finish();
	}

	private void setListener() {
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (nameEdit.getText().toString().trim().equalsIgnoreCase("")) {
					AndroidUtils.showToast(FindPasswordActivity.this, "账号不能为空");
					return;
				}
				SharePrefenceUtils.saveAccount(FindPasswordActivity.this,
						nameEdit.getText().toString().trim());
				ver = AndroidUtils.getVerification();
				new WebServiceUtils(FindPasswordActivity.this, mHandler)
						.sendExecuteNo(new String[] {
								nameEdit.getText().toString().trim()},
								WebServiceParmas.FIND_PASS,
								WebServiceParmas.HTTP_POST);
			}
		});

	}
}
