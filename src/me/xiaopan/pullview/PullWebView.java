package me.xiaopan.pullview;

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
	public WebView onCreatePullView() {
		return new WebView(getContext());
	}

	@Override
	public View onCreateHeaderView() {
		return null;
	}

	@Override
	public View onCreateFooterView() {
		return null;
	}

}