package com.yifa.health_manage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yifa.health_manage.ChangePasswordActivity;
import com.yifa.health_manage.DeviceListActivity;
import com.yifa.health_manage.LoginActivity;
import com.yifa.health_manage.R;
import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.SharePrefenceUtils;

/**
 * 我的界面
 * */
public class UserFragment extends Fragment implements OnClickListener {

	private EditText nameEdit, briEdit, heightEdit, weightEdit, yaoEdit;

	private ImageView userPhoto;

	private LinearLayout blood_p;

	private RelativeLayout changePass;

	private TextView userAccount;

	private Button outButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		initView(view);
		initListener();
		return view;
	}

	private void initView(View view) {
		userPhoto = (ImageView) view.findViewById(R.id.user_photo);
		blood_p = (LinearLayout) view.findViewById(R.id.user_blood_p);
		userAccount = (TextView) view.findViewById(R.id.user_name_text);
		changePass = (RelativeLayout) view.findViewById(R.id.user_account_pass);
		outButton = (Button) view.findViewById(R.id.user_out_button);

	}

	private void initListener() {
		blood_p.setOnClickListener(this);
		changePass.setOnClickListener(this);
		outButton.setOnClickListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		UserInfo info = SharePrefenceUtils.getUsetInfo(getActivity());
		if (info != null) {

			nameEdit.setText(info.getName());
			briEdit.setText(info.getBirsday());
			heightEdit.setText(info.getHight());
			weightEdit.setText(info.getWight());
			yaoEdit.setText(info.getYaowei());
			if (!info.getImageUrl().equalsIgnoreCase("")) {
				String url = info.getImageUrl();
				byte[] b = Base64.decode(url.getBytes(), Base64.DEFAULT);
				Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				if (bitmap != null)
					userPhoto.setImageBitmap(bitmap);
			}
		}
		userAccount.setText(SharePrefenceUtils.getAccount(getActivity()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_blood_p:
			startActivity(new Intent(getActivity(), DeviceListActivity.class));
			break;
		case R.id.user_account_pass:
			startActivity(new Intent(getActivity(),
					ChangePasswordActivity.class));
			break;
		case R.id.user_out_button:
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;

		default:
			break;
		}

	}
}
