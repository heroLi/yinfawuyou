package com.yifa.health_manage.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.yifa.health_manage.R;
import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.SharePrefenceUtils;

/**
 * 我的界面
 * */
public class UserFragment extends Fragment {

	private EditText nameEdit, briEdit, heightEdit, weightEdit, yaoEdit;

	private ImageView userPhoto;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		nameEdit = (EditText) view.findViewById(R.id.user_name);
		briEdit = (EditText) view.findViewById(R.id.user_bri);
		heightEdit = (EditText) view.findViewById(R.id.user_height);
		weightEdit = (EditText) view.findViewById(R.id.user_weight);
		yaoEdit = (EditText) view.findViewById(R.id.user_yao);
		userPhoto = (ImageView) view.findViewById(R.id.user_photo);

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
	}
}
