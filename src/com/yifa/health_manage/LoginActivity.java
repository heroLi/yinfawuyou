package com.yifa.health_manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/*
 * 登录
 * **/
public class LoginActivity extends Activity implements OnClickListener {

	private TextView noLogin,forgetPass;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		initView();
		initListener();
	}

	private void initView() {
		noLogin = (TextView) findViewById(R.id.no_login_name);
		forgetPass = (TextView) findViewById(R.id.login_forget_pass);
	}

	private void initListener() {
		noLogin.setOnClickListener(this);
		forgetPass.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.no_login_name:
			startActivity(new Intent(this,RegisterActivity.class));
			break;
		case R.id.login_forget_pass:
			startActivity(new Intent(this,FindPasswordActivity.class));
			break;

		default:
			break;
		}

	}
}
