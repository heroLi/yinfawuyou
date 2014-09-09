package com.yifa.health_manage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/*
 * 登录
 * **/
public class LoginActivity extends Activity implements OnClickListener {

	private TextView noLogin, forgetPass;
	private Button loginButton;
	private EditText nameEdit, passEdit;

	private CheckBox isLogin;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.d("--------", msg.obj.toString());

			if (msg.obj.toString().equalsIgnoreCase("")) {
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(msg.obj.toString());
				Gson gson = new Gson();
				// Type type = new TypeToken<ResultResponse>() {
				// }.getType();
				// List<ResultResponse> response = gson.fromJson(
				// jsonObject.toString(), type);
				ResultResponse response = gson.fromJson(jsonObject.toString(),
						ResultResponse.class);
				if (response.isResult()) {
					SharePrefenceUtils.saveAccount(LoginActivity.this, nameEdit
							.getText().toString().trim());
					SharePrefenceUtils.savePassword(LoginActivity.this,
							passEdit.getText().toString().trim());
					startActivity(new Intent(LoginActivity.this,
							Main_board_Activity.class));
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_layout);
		initView();
		initListener();

	}

	private void initView() {
		noLogin = (TextView) findViewById(R.id.no_login_name);
		forgetPass = (TextView) findViewById(R.id.login_forget_pass);
		loginButton = (Button) findViewById(R.id.login_button);
		nameEdit = (EditText) findViewById(R.id.login_edit_name);
		passEdit = (EditText) findViewById(R.id.login_edit_password);
		isLogin = (CheckBox) findViewById(R.id.login_click);

		if (!SharePrefenceUtils.getAccount(this).equalsIgnoreCase("")) {
			nameEdit.setText(SharePrefenceUtils.getAccount(this));
		}
		if (!SharePrefenceUtils.getPassword(this).equalsIgnoreCase("")) {
			passEdit.setText(SharePrefenceUtils.getPassword(this));
		}
	}

	private void initListener() {
		noLogin.setOnClickListener(this);
		forgetPass.setOnClickListener(this);
		loginButton.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.no_login_name:
			startActivity(new Intent(this, RegisterActivity.class));

			break;
		case R.id.login_forget_pass:
			startActivityForResult(
					new Intent(this, FindPasswordActivity.class), 0);
			break;
		case R.id.login_button:
			if (isVerify()) {
				if (isLogin.isChecked()) {
					SharePrefenceUtils.saveAutoLogin(this, true);
				} else {
					SharePrefenceUtils.saveAutoLogin(this, false);
				}
				new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
						nameEdit.getText().toString().trim(),
						passEdit.getText().toString().trim() },
						WebServiceParmas.LOGIN, WebServiceParmas.HTTP_POST);
			}

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
		if (passEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "密码不能为空");
			return false;
		}
		return true;
	}
}
