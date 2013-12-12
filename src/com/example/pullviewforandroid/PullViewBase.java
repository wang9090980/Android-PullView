package com.example.pullviewforandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public abstract class PullViewBase<T extends View> extends LinearLayout{
	private T refreshableView;
	private View headerView;
	private View footerView;
	
	public PullViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullViewBase(Context context) {
		super(context);
	}
	
	private void init(){
		refreshableView = createRefreshableView();
		headerView = createHeaderView();
		footerView = createFooterView();
		addView(refreshableView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
	}

	public abstract T createRefreshableView();

	public abstract View createHeaderView();

	public abstract View createFooterView();
}