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
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 可拉的横向滚动视图
 */
public class PullHorizontalScrollView extends PullViewBase<HorizontalScrollView> {

	public PullHorizontalScrollView(Context context) {
		super(context);
	}

	public PullHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected HorizontalScrollView createPullView() {
		HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
		horizontalScrollView.setBackgroundColor(Color.WHITE);
		return horizontalScrollView;
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
	public boolean isCanPullHeader(HorizontalScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentWidth = null != scrollViewChild?scrollViewChild.getWidth():0;  //内容宽度
        int viewWidth = getWidth();   //视图宽度
        if(contentWidth > viewWidth){
            return pullView.getScrollX() == 0;
        }else{
            return false;
        }
	}

	@Override
	public boolean isCanPullFooter(HorizontalScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentWidth = null != scrollViewChild?scrollViewChild.getWidth():0;  //内容宽度
        int viewWidth = getWidth();   //视图宽度
        if(contentWidth > viewWidth){
            return pullView.getScrollX() >= contentWidth - viewWidth;
        }else{
            return false;
        }
	}

	@Override
	protected boolean isVerticalPull() {
		return false;
	}

    @Override
    protected void scrollPullViewToHeader(HorizontalScrollView pullView) {
        pullView.scrollTo(0, pullView.getScrollY());
    }

    @Override
    protected void scrollPullViewToFooter(HorizontalScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentWidth = null != scrollViewChild?scrollViewChild.getWidth():0;  //内容宽度
        int viewWidth = getWidth();   //视图宽度
        if(contentWidth > viewWidth){
            pullView.scrollTo(contentWidth - viewWidth, pullView.getScrollY());
        }
    }
}