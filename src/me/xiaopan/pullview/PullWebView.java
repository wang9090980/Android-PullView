package me.xiaopan.pullview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

/**
 * 可拉的WebView
 */
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

//	@Override
//	public View createHeaderView() {
//		return null;
//	}
//
//	@Override
//	public View createFooterView() {
//		return null;
//	}

	@Override
	public boolean isInTheHeader(WebView pullView) {
        int contentHeight = pullView.getContentHeight();
        float currScale = pullView.getScale();
        float realContentHeight = FloatMath.floor(contentHeight * currScale);
        int webViewHeight = pullView.getHeight();
        Log.d(PullViewBase.class.getSimpleName(), "ScrollY="+pullView.getScrollY());
		return realContentHeight > webViewHeight?pullView.getScrollY() == 0:false;
	}

	@SuppressLint("FloatMath")
	@SuppressWarnings("deprecation")
	@Override
	public boolean isInTheFooter(WebView pullView) {
        int currScrollY = pullView.getScrollY();
        int contentHeight = pullView.getContentHeight();
        float currScale = pullView.getScale();
        float realContentHeight = FloatMath.floor(contentHeight * currScale);
        int webViewHeight = pullView.getHeight();
        boolean inFooter = realContentHeight > webViewHeight?currScrollY >= realContentHeight - webViewHeight:false;
        Log.d(
            PullViewBase.class.getSimpleName(),
            "currScrollY="+currScrollY
            +"; contentHeight="+contentHeight
            +"; currScale="+currScale
            +"; realContentHeight="+realContentHeight
            +"; webViewHeight="+webViewHeight
            +"; inFooter="+inFooter
        );
        return inFooter;
	}

	@Override
	protected PullOrientation getPullOrientation() {
		return PullOrientation.VERTICAL;
	}

	@Override
	protected void scrollPullViewToHeader(WebView pullView) {
		pullView.scrollTo(pullView.getScrollX(), 0);
	}

	@Override
	protected void scrollPullViewToFooter(WebView pullView) {
        int currScrollY = pullView.getScrollY();
        int contentHeight = pullView.getContentHeight();
        float currScale = pullView.getScale();
        float realContentHeight = FloatMath.floor(contentHeight * currScale);
        int webViewHeight = pullView.getHeight();
        if(realContentHeight > webViewHeight){
            pullView.scrollTo(pullView.getScrollX(), (int)(realContentHeight - webViewHeight));
        }
	}
}