package com.yifa.health_manage.adapter;

import java.text.DecimalFormat;
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
			if (type == 0) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_list_blood, parent, false);
				holder.data = (TextView) convertView.findViewById(R.id.data);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.level = (TextView) convertView.findViewById(R.id.level);
				holder.level_h = (TextView) convertView
						.findViewById(R.id.level2);
				holder.level_l = (TextView) convertView
						.findViewById(R.id.level_high);
				holder.level_1 = (TextView) convertView
						.findViewById(R.id.level_high1);
				holder.level_2 = (TextView) convertView
						.findViewById(R.id.level_high2);
				holder.level_3 = (TextView) convertView
						.findViewById(R.id.level_high3);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_blood, parent, false);
				holder.data = (TextView) convertView.findViewById(R.id.data);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.level = (TextView) convertView.findViewById(R.id.level);
				holder.level_h = (TextView) convertView
						.findViewById(R.id.level_high);
				holder.level_l = (TextView) convertView
						.findViewById(R.id.level_low);
			}
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
			holder.level_1.setBackgroundResource(R.drawable.newdata_text_bg_no);;
			holder.level_2.setBackgroundResource(R.drawable.newdata_text_bg_no);
			holder.level_3.setBackgroundResource(R.drawable.newdata_text_bg_no);
		} else if (type == 1) {
			level = AndroidUtils.getBloodLevel(type,
					Integer.valueOf(mList.get(position).getValue())/18);
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			
			holder.data.setText(decimalFormat.format((Double.valueOf(mList.get(position).getValue())/18)) + "mmol/L");
			holder.level_h.setText("低血糖");
			holder.level_l.setText("高血糖");
		} else {
			level = AndroidUtils.getBloodLevel(type,
					Integer.valueOf(mList.get(position).getValue()));
			holder.data.setText(mList.get(position).getValue() + "BPM");
			holder.level_h.setText("偏快");
			holder.level_l.setText("偏慢");
		}
		holder.time.setText(mList.get(position).getDatetime());
		Drawable left = mContext.getResources().getDrawable(
				R.drawable.chart_list_ok);
		holder.level.setBackgroundResource(R.drawable.newdata_text_bg_no);
		holder.level_h.setBackgroundResource(R.drawable.newdata_text_bg_no);
		holder.level_l.setBackgroundResource(R.drawable.newdata_text_bg_no);
		
		if(type == 0){
			switch (level) {
			case 0:
				holder.level.setBackgroundResource(R.drawable.newdata_201);
				break;
			case 1:
				holder.level_h.setBackgroundResource(R.drawable.newdata_202);
				break;
			case 2:
				holder.level_l.setBackgroundResource(R.drawable.newdata_203);
				break;
			case 3:
				holder.level_1.setBackgroundResource(R.drawable.newdata_204);
				break;
			case 4:
				holder.level_2.setBackgroundResource(R.drawable.newdata_205);
				break;
			case 5:
				holder.level_3.setBackgroundResource(R.drawable.newdata_206);
				break;
			default:
				break;
			}
		}else{
			switch (level) {
			case 0:
				holder.level.setBackgroundResource(R.drawable.newdata_301);
				break;
			case 1:
				holder.level_h.setBackgroundResource(R.drawable.newdata_302);
				break;
			case 2:
				holder.level_l.setBackgroundResource(R.drawable.newdata_303);
				break;
			default:
				break;
			}
		}
		

		return convertView;
	}

	class ViewHolder {
		TextView data, time, level, level_h, level_l, level_1, level_3,
				level_2, level_4, level_5, level_6;
	}
}
