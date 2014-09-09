package com.yifa.health_manage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yifa.health_manage.fragment.ProductFragment;
import com.yifa.health_manage.fragment.StartFragment;
import com.yifa.health_manage.fragment.UserFragment;
import com.yifa.health_manage.fragment.UtilsFragment;
import com.yifa.health_manage.util.AndroidUtils;

public class Main_board_Activity extends FragmentActivity implements
		OnCheckedChangeListener {

	private RadioGroup radioGroup;

	private Fragment startFragment, productFragment, utilsFragment,
			userFragment, nowFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_board_layout);
		initView();
		initListener();
		startFragment = new StartFragment();
		nowFragment = startFragment;
		getSupportFragmentManager().beginTransaction()
				.add(R.id.main_content_layout, startFragment)
				.commitAllowingStateLoss();
	}

	private void initView() {
		radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);

	}

	private void initListener() {
		radioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.main_start:
			if (startFragment == null) {
				startFragment = new StartFragment();
			}
			showFragment(nowFragment, startFragment);
			break;
		case R.id.main_product:
			if (productFragment == null) {
				productFragment = new ProductFragment();
			}
			showFragment(nowFragment, productFragment);
			break;
		case R.id.main_utils:
			if (utilsFragment == null) {
				utilsFragment = new UtilsFragment();
			}
			showFragment(nowFragment, utilsFragment);
			break;
		case R.id.main_user:
			if (userFragment == null) {
				userFragment = new UserFragment();
			}
			showFragment(nowFragment, userFragment);
			break;

		default:
			break;
		}
	}

	private boolean isOut = false;

	@Override
	public void onBackPressed() {
		if (!isOut) {
			AndroidUtils.showToast(this, "再按一次退出程序");
			isOut = true;

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					isOut = false;

				}
			}, 2000);
		} else {
			finish();
		}
	}

	private void showFragment(Fragment from, Fragment to) {

		if (nowFragment == to) {
			return;
		}
		nowFragment = to;
		if (to.isAdded()) {
			getSupportFragmentManager().beginTransaction().hide(from).show(to)
					.commitAllowingStateLoss();
		} else {
			getSupportFragmentManager().beginTransaction().hide(from)
					.add(R.id.main_content_layout, to)
					.commitAllowingStateLoss();
		}
	}
}