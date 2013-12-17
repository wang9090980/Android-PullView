package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 可拉的垂直滚动视图
 */
public abstract class PullScrollView extends PullViewBase<ScrollView> {

	public PullScrollView(Context context) {
		super(context);
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public ScrollView createPullView() {
		return new ScrollView(getContext());
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
	public boolean isInTheHeader(ScrollView pullView) {
		return false;
	}

	@Override
	public boolean isInTheFooter(ScrollView pullView) {
		return false;
	}

	@Override
	protected PullOrientation getPullOrientation() {
		return PullOrientation.VERTICAL;
	}
}