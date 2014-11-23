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
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;
import com.yifa.health_manage.util.YhyyUtil;

/**
 * 注册
 * */
public class RegisterActivity extends Activity implements OnClickListener {

	private Button buttonOk, buttonNo;

	private EditText nameEdit, emailEdit, passEdit, passTwoEdit;

	private TextView title;

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
								BindDeviceActivity.class));
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
		title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	private void initView() {

		buttonOk = (Button) findViewById(R.id.login_btn_next);
		buttonNo = (Button) findViewById(R.id.login_btn_no);
		nameEdit = (EditText) findViewById(R.id.login_edit_name);
		emailEdit = (EditText) findViewById(R.id.login_edit_email);
		passEdit = (EditText) findViewById(R.id.login_edit_pass);
		passTwoEdit = (EditText) findViewById(R.id.login_edit_password_two);
		title = (TextView) findViewById(R.id.activity_top_title);
		title.setText("注册");
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
				Intent intent = new Intent(RegisterActivity.this,
						BindDeviceActivity.class);
				intent.putExtra("name", nameEdit.getText().toString().trim());
				intent.putExtra("password", passEdit.getText().toString()
						.trim());
				intent.putExtra("email", emailEdit.getText().toString().trim());
				startActivity(intent);
			}
			break;
		case R.id.login_btn_no:
			// startActivity(new Intent(RegisterActivity.this,
			// RegisterMessageActivity.class));
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
		if (passEdit.getText().toString().trim().length()<6||passEdit.getText().toString().trim().length()>30) {
			AndroidUtils.showToast(this, "密码必须为6-30位");
			return false;
		}
		if (!passEdit.getText().toString().trim()
				.equalsIgnoreCase(passTwoEdit.getText().toString().trim())) {
			AndroidUtils.showToast(this, "两次密码不一致");
			return false;
		}
		if (!YhyyUtil.isMobileNo(nameEdit.getText().toString().trim())) {
			AndroidUtils.showToast(this, "请输入正确的手机号");
			return false;
		}
		return true;
	}
}
