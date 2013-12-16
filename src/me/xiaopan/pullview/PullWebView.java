package me.xiaopan.pullview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
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
	public WebView createPullView() {
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

	@Override
	public boolean isInTheHeader(WebView pullView) {
        Log.d(PullViewBase.class.getSimpleName(), "ScrollY="+pullView.getScrollY()+"; true?="+(pullView.getScrollY() == 0));
		return pullView.getScrollY() == 0;
	}

	@SuppressLint("FloatMath")
	@SuppressWarnings("deprecation")
	@Override
	public boolean isInTheFooter(WebView pullView) {
        Log.d(PullViewBase.class.getSimpleName(), "ContentHeight="+pullView.getContentHeight()+"; Height="+pullView.getHeight()+"; ScrollY="+pullView.getScrollY());
        return pullView.getScrollY() >= (FloatMath.floor(pullView.getContentHeight() * pullView.getScale()) - pullView.getHeight());
	}

	@Override
	protected PullOrientation getPullOrientation() {
		return PullOrientation.VERTICAL;
	}

	@Override
	protected void scrollPullViewToHeader(WebView pullView) {
		pullView.scrollTo(0, 0);
	}

	@Override
	protected void scrollPullViewToFooter(WebView pullView) {
		
	}
}