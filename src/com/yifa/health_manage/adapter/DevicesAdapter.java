package com.yifa.health_manage.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifa.health_manage.R;
import com.yifa.health_manage.model.DeviceInfo;

public class DevicesAdapter extends BaseAdapter {

	private List<DeviceInfo> mList;

	private Context mContext;

	public DevicesAdapter(Context c, List<DeviceInfo> list) {
		this.mContext = c;
		this.mList = list;
	}

	
	public void setListData(List<DeviceInfo> list){
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
					R.layout.item_devices_layout, parent, false);
			hodler.friend1 = (TextView) convertView
					.findViewById(R.id.device_friend1_name);
			hodler.friend2 = (TextView) convertView
					.findViewById(R.id.device_friend2_name);
			hodler.id = (TextView) convertView.findViewById(R.id.device_id);
			hodler.checkBox = (CheckBox) convertView.findViewById(R.id.device_check);
		} else
			hodler = (ViewHodler) convertView.getTag();

		hodler.friend1.setText(mList.get(position).getRelative().get(0)
				.getName());
		hodler.friend2.setText(mList.get(position).getRelative().get(1)
				.getName());
		hodler.id.setText(mList.get(position).getDevice_sn());

		return convertView;
	}

	class ViewHodler {
		TextView id, friend1, friend2;
		ImageView iamgeCheck;
		CheckBox checkBox;
	}
}
