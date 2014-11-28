package com.yifa.health_manage;

import net.tsz.afinal.FinalBitmap;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yifa.health_manage.fragment.ProductFragment;
import com.yifa.health_manage.fragment.StartFragment;
import com.yifa.health_manage.fragment.UserFragment;
import com.yifa.health_manage.fragment.UtilsFragment;

public class Main_board_Activity extends FragmentActivity implements
		OnCheckedChangeListener {

	private RadioGroup radioGroup;

	private Fragment startFragment, productFragment, utilsFragment,
			userFragment, nowFragment;
	public  FinalBitmap bitmap=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_board_layout);
		initView();
		initListener();
		bitmap = FinalBitmap.create(this);
		bitmap.configDiskCacheSize(1024);
		startFragment = new StartFragment(bitmap);
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
				if(bitmap==null)
					bitmap = FinalBitmap.create(Main_board_Activity.this);
				startFragment = new StartFragment(bitmap);
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
//		if (!isOut) {
//			AndroidUtils.showToast(this, "再按一次退出程序");
//			isOut = true;
//
//			new Handler().postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					isOut = false;
//
//				}
//			}, 2000);
//		} else {
//			finish();
//		}
		
		new AlertDialog.Builder(this)
		.setTitle("温馨提示：您确定退出此程序吗？")
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
						Main_board_Activity.this.finish();
					}
				})
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				}).show();
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