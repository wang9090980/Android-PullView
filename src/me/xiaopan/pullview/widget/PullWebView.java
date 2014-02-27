/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xiaopan.pullview.widget;

import me.xiaopan.pullview.PullViewBase;
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
	protected WebView createPullView() {
		return new WebView(getContext());
	}

	@Override
	protected boolean isCanPullHeader(WebView pullView) {
        return pullView.getScrollY() == 0;
	}

	@SuppressLint("FloatMath")
	@SuppressWarnings("deprecation")
	@Override
	protected boolean isCanPullFooter(WebView pullView) {
        return pullView.getScrollY() >= FloatMath.floor(pullView.getContentHeight() * pullView.getScale()) - pullView.getHeight();
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