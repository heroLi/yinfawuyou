 package com.yifa.health_manage.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.gson.JsonObject;
import com.yifa.health_manage.ChangePasswordActivity;
import com.yifa.health_manage.DeviceListActivity;
import com.yifa.health_manage.FrontPagerWebactivity;
import com.yifa.health_manage.LoginActivity;
import com.yifa.health_manage.R;
import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 我的界面
 * */
public class UserFragment extends Fragment implements OnClickListener {

	private EditText nameEdit, briEdit, heightEdit, weightEdit, yaoEdit;

	private ImageView userPhoto;

	private LinearLayout blood_p, blood_s, changeAccount, feedBack, verGengxin;

	private RelativeLayout changePass;

	private TextView userAccount;

	private Button outButton;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				JSONObject jsonObject = new JSONObject(msg.obj.toString());
				String vercode = jsonObject.getString("version");

				if (vercode.equalsIgnoreCase(getResources().getString(
						R.string.app_ver))) {
					AndroidUtils.showToast(getActivity(), "已是最新版本");
				} else {
					Intent intent3 = new Intent(getActivity(),
							FrontPagerWebactivity.class);
					intent3.putExtra("url",
							"http://121.40.172.222/health2/site/app");
					startActivity(intent3);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	};

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
		feedBack = (LinearLayout) view.findViewById(R.id.feedBack);
		verGengxin = (LinearLayout) view.findViewById(R.id.ver_gengxin);
		changeAccount = (LinearLayout) view.findViewById(R.id.change_acconut);
		blood_s = (LinearLayout) view.findViewById(R.id.user_blood_s);
		userAccount = (TextView) view.findViewById(R.id.user_name_text);
		changePass = (RelativeLayout) view.findViewById(R.id.user_account_pass);
		outButton = (Button) view.findViewById(R.id.user_out_button);

	}

	private void initListener() {
		blood_p.setOnClickListener(this);
		feedBack.setOnClickListener(this);
		verGengxin.setOnClickListener(this);
		changePass.setOnClickListener(this);
		changeAccount.setOnClickListener(this);
		outButton.setOnClickListener(this);
		blood_s.setOnClickListener(this);

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
			Intent intent = new Intent(getActivity(), DeviceListActivity.class);
			intent.putExtra("type", "blood_presure");
			startActivity(intent);
			break;
		case R.id.user_blood_s:
			Intent intent2 = new Intent(getActivity(), DeviceListActivity.class);
			intent2.putExtra("type", "blood_glucose");
			startActivity(intent2);
			break;
		case R.id.user_account_pass:
			startActivity(new Intent(getActivity(),
					ChangePasswordActivity.class));
			break;
		case R.id.change_acconut:
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.user_out_button:
			getActivity().finish();
			break;
		case R.id.feedBack:
			Intent intent1 = new Intent(getActivity(),
					FrontPagerWebactivity.class);
			intent1.putExtra("url",
					"http://121.40.172.222/health2/site/feedback");
			startActivity(intent1);
			break;
		case R.id.ver_gengxin:

			new WebServiceUtils(getActivity(), handler).sendExecute(
					new String[] {}, WebServiceParmas.VEN_CODE,
					WebServiceParmas.HTTP_POST, "加载中...");

			break;

		default:
			break;
		}

	}
}
