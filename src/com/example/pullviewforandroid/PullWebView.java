package com.example.pullviewforandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

public class PullWebView extends PullViewBase<WebView> {

	public PullWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullWebView(Context context) {
		super(context);
	}

	@Override
	public WebView createRefreshableView() {
		return new WebView(getContext());
	}

	@Override
	public View createHeaderView() {
		return null;
	}

	@Override
	public View createFooterView() {
		return null;
	}

}