package com.yifa.health_manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * 注册
 * */
public class RegisterActivity extends Activity implements OnClickListener{


	private Button buttonOk,buttonNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_layout);
		initView();
		initLisenter();
	}
	private void initView() {

		buttonOk = (Button)findViewById(R.id.register_btn_next);
		buttonNo = (Button)findViewById(R.id.register_btn_no);
	}
	
	private void initLisenter() {
		buttonOk.setOnClickListener(this);
		buttonNo.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn_next:
			startActivity(new Intent(this,RegisterMessageActivity.class));
			break;
		case R.id.register_btn_no:
			finish();
			break;

		default:
			break;
		}
		
	}
}
