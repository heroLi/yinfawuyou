package com.yifa.health_manage.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yifa.health_manage.R;
import com.yifa.health_manage.model.BloodValuesInfo;
import com.yifa.health_manage.util.AndroidUtils;

public class BloodListAdapter extends BaseAdapter {

	private Context mContext;

	private List<BloodValuesInfo> mList;

	private int type = 0;

	public BloodListAdapter(Context mContext, List<BloodValuesInfo> mList,
			int type) {
		this.mContext = mContext;
		this.mList = mList;
		this.type = type;

	}

	public void setData(List<BloodValuesInfo> mList) {
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
		int level = 0;
		if (type == 0) {
			level = AndroidUtils.getBloodLevel(type,
					Integer.valueOf(mList.get(position).getHigh_value()));
			holder.data.setText(mList.get(position).getHigh_value() + "/"
					+ mList.get(position).getLow_value() + "mmHg");
		} else if (type == 1) {
			level = AndroidUtils.getBloodLevel(type,
					Integer.valueOf(mList.get(position).getValue()));
			holder.data.setText(mList.get(position).getValue() + "mmol/L");
		} else {
			level = AndroidUtils.getBloodLevel(type,
					Integer.valueOf(mList.get(position).getValue()));
			holder.data.setText(mList.get(position).getValue() + "BPM");
		}
		holder.time.setText(mList.get(position).getData());
		Drawable left = mContext.getResources().getDrawable(
				R.drawable.chart_list_ok);
		switch (level) {
		case 0:
			holder.level.setCompoundDrawablesWithIntrinsicBounds(left, null,
					null, null);
			break;
		case 1:
			holder.level_h.setCompoundDrawablesWithIntrinsicBounds(left, null,
					null, null);
			break;
		case 2:
			holder.level_l.setCompoundDrawablesWithIntrinsicBounds(left, null,
					null, null);
			break;
		case 3:

			break;
		case 4:

			break;
		case 5:

			break;

		default:
			break;
		}

		return convertView;
	}

	class ViewHolder {
		TextView data, time, level, level_h, level_l, level_1, level_3,
				level_2, level_4, level_5, level_6;
	}
}
