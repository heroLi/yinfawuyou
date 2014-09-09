package com.yifa.health_manage.util;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AChartEngineDemo extends Activity {

	private LinearLayout chartLayout;

	private XYMultipleSeriesRenderer renderer;

	private XYMultipleSeriesDataset mDataset;

	double[] xdata = new double[] { 11, 22, 33, 44, 55, 66, 77, 88, 99, 110 };
	double[] ydata = new double[] { 11, 11, 22, 33, 44, 55, 66, 77, 88, 99 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.achar_activity);
//		chartLayout = (LinearLayout) findViewById(R.id.mychart);
		initChart();
	}

	private void initChart() {
		final GraphicalView view = ChartFactory.getLineChartView(this,
				getDataset(), getRenderer());
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SeriesSelection selection = view.getCurrentSeriesAndPoint();
				Toast.makeText(
						AChartEngineDemo.this,
						selection.getPointIndex() + "------x"
								+ selection.getXValue() + "--------------y"
								+ selection.getValue(), 3000).show();
			}
		});
		chartLayout.addView(view);

	}

	// 得到主渲染器，并对其各项属性进行设置
	private XYMultipleSeriesRenderer getRenderer() {
		renderer = new XYMultipleSeriesRenderer();
		// 设置背景色是否启用
		renderer.setApplyBackgroundColor(true);
		// 设置背景色
		renderer.setBackgroundColor(Color.WHITE);
		// 设置x y轴标题字体大小
		renderer.setAxisTitleTextSize(18f);
		
		// 设置表格标题字体大小
		renderer.setChartTitleTextSize(25f);
		//设置表格标题
		renderer.setChartTitle("血压");
		// 设置标签字体大小
		renderer.setLabelsTextSize(20f);
		// 设置图例字体大小
		renderer.setLegendTextSize(15f);
		
		// 设置是否显示放大缩小按钮
		renderer.setZoomButtonsVisible(true);
		// 设置图表上显示点的大小
		renderer.setPointSize(5f);
		// 设置边距
		renderer.setMargins(new int[] { 20, 20, 20, 20 });
		// 设置边距的颜色
		renderer.setMarginsColor(Color.BLUE);
		

		// create a new renderer for the new series创建一个新的渲染器的新系列，每一条的对象
		XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();

		// 设置新系列的属性
		seriesRenderer.setPointStyle(PointStyle.CIRCLE);
		seriesRenderer.setFillPoints(true);
		seriesRenderer.setDisplayChartValues(true);
		seriesRenderer.setDisplayChartValuesDistance(100);
		seriesRenderer.setColor(Color.RED);
		renderer.addSeriesRenderer(seriesRenderer);

		return renderer;
	}

	/**
	 * 得到住渲染器，并对其各项属性进行设置
	 * 
	 * @return
	 */
	public XYMultipleSeriesDataset getDataset() {
		mDataset = new XYMultipleSeriesDataset();
		String seriesTitle = "健康数据";
		XYSeries mSeries = new XYSeries(seriesTitle);
		for (int i = 0; i < 10; i++) {
			double x = xdata[i];
			double y = ydata[i];
			// 把坐标添加到当前序列中去
			mSeries.add(x, y);
		}
		mDataset.addSeries(mSeries);
		return mDataset;
	}
	
}
