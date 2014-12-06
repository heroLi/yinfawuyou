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
 * 设置新密码
 * **/
public class SetNewPasswordActivity extends Activity {

	private TextView title;
	private EditText nameEdit, passEdit, passTwoEdit;
	private Button nextButton;

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
					startActivity(new Intent(SetNewPasswordActivity.this,
							Main_board_Activity.class));
					setResult(0);
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
		setContentView(R.layout.activity_set_pass);
		initView();
		setListener();
	}

	private void initView() {
		nameEdit = (EditText) findViewById(R.id.login_edit_name);
		title = (TextView) findViewById(R.id.activity_top_title);
		nextButton = (Button) findViewById(R.id.register_btn_no);
		title.setText("设置新密码");
	}

	private void setListener() {
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isVerify()) {

					new WebServiceUtils(SetNewPasswordActivity.this, mHandler).sendExecuteNo(
							new String[] {
									SharePrefenceUtils
											.getAccount(SetNewPasswordActivity.this),
									passEdit.getText().toString().trim(),
									passEdit.getText().toString().trim() },
							WebServiceParmas.SET_NEW_PASS,
							WebServiceParmas.HTTP_POST);
				}
			}
		});

	}

	private boolean isVerify() {
		if (nameEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "验证码不能为空");
			return false;
		}
		if (!nameEdit.getText().toString().trim()
				.equalsIgnoreCase(SharePrefenceUtils.getVer(this))) {
			AndroidUtils.showToast(this, "验证码不正确");
			return false;
		}
		if (passEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "密码不能为空");
			return false;
		}
		if (passEdit.getText().toString().trim().length() < 6
				|| passEdit.getText().toString().trim().length() > 30) {
			AndroidUtils.showToast(this, "密码必须为6-30位");
			return false;
		}
		if (!passEdit.getText().toString().trim()
				.equalsIgnoreCase(passTwoEdit.getText().toString().trim())) {
			AndroidUtils.showToast(this, "两次密码不一致");
			return false;
		}
		return true;
	}
}
