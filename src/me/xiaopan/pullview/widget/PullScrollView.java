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
import android.widget.ScrollView;

/**
 * 可拉的垂直滚动视图
 */
public class PullScrollView extends PullViewBase<ScrollView> {

	public PullScrollView(Context context) {
		super(context);
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
    public ScrollView createPullView() {
		ScrollView scrollView = new ScrollView(getContext());
		scrollView.setBackgroundColor(Color.WHITE);
		return scrollView;
	}

	@Override
	public boolean isCanPullHeader(ScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentHeight = null != scrollViewChild?scrollViewChild.getHeight():0;  //内容高度
        int viewHeight = getHeight();   //视图高度
        if(contentHeight > viewHeight){
            return pullView.getScrollY() == 0;
        }else{
            return false;
        }
	}

	@Override
	public boolean isCanPullFooter(ScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentHeight = null != scrollViewChild?scrollViewChild.getHeight():0;  //内容高度
        int viewHeight = getHeight();   //视图高度
        if(contentHeight > viewHeight){
            return pullView.getScrollY() >= contentHeight - viewHeight;
        }else{
            return false;
        }
	}

	@Override
    public boolean isVerticalPull() {
		return true;
	}

    @Override
    public void scrollPullViewToHeader(ScrollView pullView) {
        pullView.scrollTo(pullView.getScrollX(), 0);
    }

    @Override
    public void scrollPullViewToFooter(ScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentHeight = null != scrollViewChild?scrollViewChild.getHeight():0;  //内容高度
        int viewHeight = getHeight();   //视图高度
        if(contentHeight > viewHeight){
            pullView.scrollTo(pullView.getScrollX(), contentHeight - viewHeight);
        }
    }
}