package me.xiaopan.pullview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
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

	@SuppressLint("FloatMath")
	@SuppressWarnings("deprecation")
	@Override
	public boolean isCanPullHeader(WebView pullView) {
		float contentHeight = FloatMath.floor(pullView.getContentHeight() * pullView.getScale());   //内容高度
        int viewHeight = pullView.getHeight();  //视图高度
        if(contentHeight > viewHeight){
            return pullView.getScrollY() == 0;
        }else{
            return false;
        }
	}

	@SuppressLint("FloatMath")
	@SuppressWarnings("deprecation")
	@Override
	public boolean isCanPullFooter(WebView pullView) {
        float contentHeight = FloatMath.floor(pullView.getContentHeight() * pullView.getScale());   //内容高度
        int viewHeight = pullView.getHeight();  //视图高度
        if(contentHeight > viewHeight){
            return pullView.getScrollY() >= contentHeight - viewHeight;
        }else{
            return false;
        }
	}

	@Override
	protected boolean isVerticalPull() {
		return true;
	}

	@Override
	protected void scrollPullViewToHeader(WebView pullView) {
		pullView.scrollTo(pullView.getScrollX(), 0);
	}

	@SuppressLint("FloatMath")
	@SuppressWarnings("deprecation")
	@Override
	protected void scrollPullViewToFooter(WebView pullView) {
        float contentHeight = FloatMath.floor(pullView.getContentHeight() * pullView.getScale());   //内容高度
        int viewHeight = pullView.getHeight();  //视图高度
        if(contentHeight > viewHeight){
            pullView.scrollTo(pullView.getScrollX(), (int)(contentHeight - viewHeight));
        }
	}
}