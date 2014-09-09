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

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 注册
 * */
public class RegisterActivity extends Activity implements OnClickListener {

	private Button buttonOk, buttonNo;

	private EditText nameEdit, emailEdit, passEdit, passTwoEdit;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WebServiceParmas.REGISTER:
				if (msg.obj.toString().equalsIgnoreCase("")) {
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					ResultResponse response = gson.fromJson(
							jsonObject.toString(), ResultResponse.class);
					if (response.isResult()) {
						startActivity(new Intent(RegisterActivity.this,
								RegisterMessageActivity.class));
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
		setContentView(R.layout.activity_register_layout);
		initView();
		initLisenter();
	}

	private void initView() {

		buttonOk = (Button) findViewById(R.id.login_btn_next);
		buttonNo = (Button) findViewById(R.id.login_btn_no);
		nameEdit = (EditText) findViewById(R.id.login_edit_name);
		emailEdit = (EditText) findViewById(R.id.login_edit_email);
		passEdit = (EditText) findViewById(R.id.login_edit_pass);
		passTwoEdit = (EditText) findViewById(R.id.login_edit_password_two);
	}

	private void initLisenter() {
		buttonOk.setOnClickListener(this);
		buttonNo.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_next:

			if (isVerify()) {
				new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
						nameEdit.getText().toString().trim(),
						passEdit.getText().toString().trim(),
						emailEdit.getText().toString().trim() },
						WebServiceParmas.REGISTER, WebServiceParmas.HTTP_POST);
			}
			break;
		case R.id.login_btn_no:
			startActivity(new Intent(RegisterActivity.this,
					RegisterMessageActivity.class));
			finish();
			break;

		default:
			break;
		}

	}

	private boolean isVerify() {
		if (nameEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "用户名不能为空");
			return false;
		}
		if (emailEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "邮箱不能为空");
			return false;
		}
		if (!AndroidUtils.isEmail(emailEdit.getText().toString().trim())) {
			AndroidUtils.showToast(this, "邮箱格式不正确");
			return false;
		}
		if (passEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "密码不能为空");
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
