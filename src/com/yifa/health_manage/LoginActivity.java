package com.yifa.health_manage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yifa.health_manage.db.DBManager;
import com.yifa.health_manage.model.DeviceFriendName;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/*
 * 登录
 * **/
public class LoginActivity extends Activity implements OnClickListener {

	private TextView noLogin, forgetPass, title;
	private Button loginButton;
	private EditText nameEdit, passEdit;
	private DBManager dbManager = null;

	private CheckBox isLogin;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WebServiceParmas.LOGIN:
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
					ResultResponse response = gson.fromJson(
							jsonObject.toString(), ResultResponse.class);
					if (response.isResult()) {
						if (!SharePrefenceUtils.getAccount(LoginActivity.this)
								.equalsIgnoreCase(
										nameEdit.getText().toString().trim())) {
							dbManager.deleteAll();
							SharePrefenceUtils.saveSugarFriendId(
									LoginActivity.this, new DeviceFriendName());
							SharePrefenceUtils.savePressureFriendId(
									LoginActivity.this, new DeviceFriendName());
						}
						SharePrefenceUtils.saveAccount(LoginActivity.this,
								nameEdit.getText().toString().trim());
						SharePrefenceUtils.savePassword(LoginActivity.this,
								passEdit.getText().toString().trim());
						startActivity(new Intent(LoginActivity.this,
								Main_board_Activity.class));
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case -1:

				// AndroidUtils.showToast(LoginActivity.this, "请保持网络连接");
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_layout);
		initView();
		dbManager = new DBManager(this);
		initListener();

	}

	private void initView() {
		noLogin = (TextView) findViewById(R.id.no_login_name);
		forgetPass = (TextView) findViewById(R.id.login_forget_pass);
		title = (TextView) findViewById(R.id.activity_top_title);
		loginButton = (Button) findViewById(R.id.login_button);
		nameEdit = (EditText) findViewById(R.id.login_edit_name);
		passEdit = (EditText) findViewById(R.id.login_edit_password);
		isLogin = (CheckBox) findViewById(R.id.login_click);

		if (!SharePrefenceUtils.getAccount(this).equalsIgnoreCase("")) {
			nameEdit.setText(SharePrefenceUtils.getAccount(this));
			CharSequence text = nameEdit.getText().toString().trim();
			nameEdit.setSelection(text.length());
		} 
		if (!SharePrefenceUtils.getPassword(this).equalsIgnoreCase("")) {
			passEdit.setText(SharePrefenceUtils.getPassword(this));
		} 
		title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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
	if(requestCode==1&&resultCode==0){
		finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.no_login_name:
			startActivityForResult(new Intent(this, RegisterActivity.class),1);

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
				new WebServiceUtils(this, mHandler).sendExecute(new String[] {
						nameEdit.getText().toString().trim(),
						passEdit.getText().toString().trim() },
						WebServiceParmas.LOGIN, WebServiceParmas.HTTP_POST,
						"登录中...");
			}

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
