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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 可拉伸的ViewPager
 */
public class PullViewPager extends PullViewBase<ViewPager> {

	public PullViewPager(Context context) {
		super(context);
	}

	public PullViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ViewPager createPullView() {
		return new ViewPager(getContext());
	}

	@Override
	protected boolean isVerticalPull() {
		return false;
	}

	@Override
	protected boolean isCanPullHeader(ViewPager pullView) {
		return pullView.getAdapter() != null?pullView.getCurrentItem() == 0:false;
	}

	@Override
	protected boolean isCanPullFooter(ViewPager pullView) {
		PagerAdapter adapter = pullView.getAdapter();
		return adapter != null?pullView.getCurrentItem() == adapter.getCount() - 1:false;
	}

	@Override
	protected void scrollPullViewToHeader(ViewPager pullView) {
		
	}

	@Override
	protected void scrollPullViewToFooter(ViewPager pullView) {
		
	}
}
