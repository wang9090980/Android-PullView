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