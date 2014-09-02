package com.yifa.health_manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/*
 * 登录
 * **/
public class LoginActivity extends Activity implements OnClickListener {

	private TextView noLogin, forgetPass;
	private Button loginButton;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.d("--------", msg.obj.toString());

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
		new WebServiceUtils(this, mHandler).sendExecuteNo(new String[]{},
				WebServiceParmas.TEST, WebServiceParmas.HTTP_POST);
	}

	private void initView() {
		noLogin = (TextView) findViewById(R.id.no_login_name);
		forgetPass = (TextView) findViewById(R.id.login_forget_pass);
		loginButton = (Button) findViewById(R.id.login_button);
	}

	private void initListener() {
		noLogin.setOnClickListener(this);
		forgetPass.setOnClickListener(this);
		loginButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.no_login_name:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.login_forget_pass:
			startActivity(new Intent(this, FindPasswordActivity.class));
			break;
		case R.id.login_button:
			startActivity(new Intent(this, Main_board_Activity.class));
			break;

		default:
			break;
		}

	}
}
