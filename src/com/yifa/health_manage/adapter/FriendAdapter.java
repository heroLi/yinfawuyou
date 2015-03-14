package com.yifa.health_manage.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifa.health_manage.R;
import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.SharePrefenceUtils;

public class FriendAdapter extends BaseAdapter {

	private List<UserInfo> mList;

	private Context mContext;

	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

	public FriendAdapter(Context c, List<UserInfo> list) {
		this.mContext = c;
		this.mList = list;
	}

	public void setListData(List<UserInfo> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler hodler = null;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_dialog, null, false);
			hodler.friend1 = (TextView) convertView
					.findViewById(R.id.device_friend_name);
			hodler.deviceType = (TextView) convertView
					.findViewById(R.id.device_type);
			hodler.iamgeCheck = (ImageView) convertView
					.findViewById(R.id.device_check);
			hodler.imagePhoto = (ImageView) convertView
					.findViewById(R.id.photo);
			convertView.setTag(hodler);
		} else
			hodler = (ViewHodler) convertView.getTag();
		switch (position) {
		case 0:
			hodler.imagePhoto.setImageResource(R.drawable.father);
			break;
		case 1:
			hodler.imagePhoto.setImageResource(R.drawable.father);
			break;
		case 2:
			hodler.imagePhoto.setImageResource(R.drawable.father);
			break;
		case 3:
			hodler.imagePhoto.setImageResource(R.drawable.father);
			break;

		default:
			break;
		}
		hodler.friend1.setText(mList.get(position).getName());
		hodler.deviceType.setText("设备号："+mList.get(position).getDevice_sn());
		if (!mList.get(position).getImageUrl().equalsIgnoreCase("")) {
			Log.d("-------------------------", position + "");
			byte[] b = Base64.decode(mList.get(position).getImageUrl()
					.getBytes(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			if (bitmap != null)
				hodler.imagePhoto.setImageBitmap(bitmap);
		} else {
			switch (position) {
			case 0:
				hodler.imagePhoto.setImageResource(R.drawable.father);
				break;
			case 1:
				hodler.imagePhoto.setImageResource(R.drawable.father);
				break;
			case 2:
				hodler.imagePhoto.setImageResource(R.drawable.father);
				break;
			case 3:
				hodler.imagePhoto.setImageResource(R.drawable.father);
				break;

			default:
				break;
			}
		}
		map.put(position, false);
		if (mList.get(position).getType().equalsIgnoreCase("blood_glucose")) {
			if (mList
					.get(position)
					.getFriend_id()
					.equalsIgnoreCase(
							SharePrefenceUtils.getSugarFriendId(mContext)
									.getId())
					&& mList.get(position)
							.getDevice_sn()
							.equalsIgnoreCase(
									SharePrefenceUtils.getSugarFriendId(
											mContext).getDevice_sn())) {
				map.put(position, true);
			}

		} else {

			if (mList
					.get(position)
					.getFriend_id()
					.equalsIgnoreCase(
							SharePrefenceUtils.getPressureFriendId(mContext)
									.getId())
					&& mList.get(position)
							.getDevice_sn()
							.equalsIgnoreCase(
									SharePrefenceUtils.getPressureFriendId(
											mContext).getDevice_sn())) {
				Log.d("yifawuyou", "------friend " + position);

				map.put(position, true);
			}
		}

		if (map.get(position)) {
			hodler.iamgeCheck.setBackgroundResource(R.drawable.click_ok);
		} else {
			hodler.iamgeCheck.setBackgroundResource(R.drawable.click_no);
		}
		return convertView;
	}

	class ViewHodler {
		TextView id, friend1, friend2, deviceType;
		ImageView iamgeCheck, imagePhoto;
	}

}
