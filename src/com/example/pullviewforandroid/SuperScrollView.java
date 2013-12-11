package com.example.pullviewforandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class SuperScrollView extends ScrollView {

	public SuperScrollView(Context context) {
		super(context);
	}

	public SuperScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SuperScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		OverScroller overScroller = new OverScroller();
	}

	@Override
	 protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent); 
		Log.d(SuperScrollView.class.getSimpleName(), "deltaX="+deltaX+"；deltaY="+deltaY+"；scrollX="+scrollX+"；scrollY="+scrollY+"；scrollRangeX="+scrollRangeX+"；scrollRangeY="+scrollRangeY+"；maxOverScrollX="+maxOverScrollX+"；maxOverScrollY="+maxOverScrollY);
		return returnValue;
	 }
}