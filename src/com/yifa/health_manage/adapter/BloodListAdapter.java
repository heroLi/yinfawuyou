package com.yifa.health_manage.adapter;

import java.util.List;

import com.yifa.health_manage.R;
import com.yifa.health_manage.model.BloodValuesInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BloodListAdapter extends BaseAdapter {

	private Context mContext;

	private List<BloodValuesInfo> mList;

	public BloodListAdapter(Context mContext, List<BloodValuesInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}

	
	public void setData(List<BloodValuesInfo> mList){
		this.mList = mList;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_blood, parent, false);
			holder.data = (TextView) convertView.findViewById(R.id.data);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.level = (TextView) convertView.findViewById(R.id.level);
			holder.level_h = (TextView) convertView
					.findViewById(R.id.level_high);
			holder.level_l = (TextView) convertView
					.findViewById(R.id.level_low);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder {
		TextView data, time, level, level_h, level_l;
	}
}
