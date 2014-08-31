package com.yifa.health_manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 设备绑定界面
 * **/
public class BindDeviceActivity extends Activity implements OnClickListener{

	private Button nextButton,topButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_layout);
		initView();
		initLisenter();
	}
	private void initView() {

		nextButton = (Button)findViewById(R.id.register_btn_next);
		topButton = (Button)findViewById(R.id.register_btn_no);
	}
	
	private void initLisenter() {
		nextButton.setOnClickListener(this);
		topButton.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn_next:
			startActivity(new Intent(this,Main_board_Activity.class));
			break;
		case R.id.register_btn_no:
			finish();
			break;

		default:
			break;
		}
		
	}
}
