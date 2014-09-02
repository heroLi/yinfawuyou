package com.yifa.health_manage.fragment;

import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.yifa.health_manage.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 图表界面
 * **/
public class ChartFragment extends Fragment {

	private String title = "线性统计图示例";
	private XYSeries series_high;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_low;// XY数据点
	private XYMultipleSeriesDataset mDataset;// XY轴数据集存放XYSeries
	private GraphicalView mViewChart;// 用于显示现行统计图
	private XYMultipleSeriesRenderer mXYRenderer;// 线性统计图主描绘器
	private LinearLayout mLayout;
	private int X = 10;// X数据集大小
	private int Y = 200;//

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mLayout = (LinearLayout) view.findViewById(R.id.chart_layout);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		series_high = new XYSeries("高压");
		for (int i = 0; i < 30; i++) {
			series_high.add(10 * 1.5, 70 + 3.0);
		}
		series_low = new XYSeries("低压");
		for (int i = 0; i < 30; i++) {
			series_low.add(10 * 1.5, 70 + 3.0);
		}

		mDataset = new XYMultipleSeriesDataset();
		mDataset.addSeries(series_high);
		mDataset.addSeries(series_low);

		// 曲线图的格式，包括颜色，值的范围，点和线的形状等等 都封装在
		// XYSeriesRender对象中，再将XYSeriesRender对象封装在 XYMultipleSeriesRenderer 对象中
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.GREEN);
//		r.setPointStyle(true);
		r.setFillPoints(true);
		mXYRenderer = new XYMultipleSeriesRenderer();
		mXYRenderer.addSeriesRenderer(r);
	}
}
