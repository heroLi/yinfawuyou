package com.yifa.health_manage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.yifa.health_manage.R;

public class MyLinearLayout extends View implements Runnable {

	private float angel = 0;
	private Matrix matrix;
	private Bitmap needleBm;
	private int valuse = 0;
	private Bitmap oilBm;
	
	private Rect mImageRect;

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		matrix = new Matrix();
		mImageRect = new Rect();
		needleBm = BitmapFactory.decodeResource(getResources(), R.drawable.pan);
		oilBm = BitmapFactory.decodeResource(getResources(),
				R.drawable.xinlv_group);
		// new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		matrix.reset();
		canvas.drawBitmap(oilBm, 0, 0, null);

		matrix.preTranslate(0, oilBm.getHeight() - needleBm.getHeight() - 3);
		matrix.preRotate(angel, oilBm.getWidth() / 2-20, needleBm.getHeight()-10);
//		matrix.preRotate(angel, oilBm.getWidth() / 2, needleBm.getHeight());
		
//		matrix.preTranslate( oilBm.getWidth() / 2-1,
//				oilBm.getHeight() / 2 - 2);
//		matrix.preRotate(angel, needleBm.getWidth()/2, needleBm.getHeight()/6);

		canvas.drawBitmap(needleBm, matrix, null);
	}

	public void setBackImage(int id) {
		oilBm = BitmapFactory.decodeResource(getResources(), id);
	}

	public void setReflush(int value) {
		valuse = value;
		new Thread(this).start();

	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		/** 
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式 
         */ 
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);  
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec); 
        //获得bitmap的宽高
        int bitmapWidth=0;
        int bitmapHeight=0;
		if(oilBm!=null){
			bitmapWidth = oilBm.getWidth();
			bitmapHeight = oilBm.getHeight();
		}
		if(bitmapWidth!=0&&bitmapHeight!=0)
			setMeasuredDimension(bitmapWidth, bitmapHeight);
		else{
			setMeasuredDimension(sizeWidth, sizeHeight);
		}
	}

	@Override
	public void run() {
		angel = valuse;
		postInvalidate();

	}

}
