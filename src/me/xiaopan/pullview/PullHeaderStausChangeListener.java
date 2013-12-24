package me.xiaopan.pullview;

import me.xiaopan.pullview.PullHeader.OnStatusChangeListener;

public class PullHeaderStausChangeListener implements OnStatusChangeListener {
	private PullViewBase<?> pullViewBase;
	
	public PullHeaderStausChangeListener(PullViewBase<?> pullViewBase) {
		this.pullViewBase = pullViewBase;
	}

	@Override
	public void onShow() {
		pullViewBase.setPadding(pullViewBase.getPaddingLeft(), -pullViewBase.getPullHeader().getHeight(), pullViewBase.getPaddingRight(), pullViewBase.getPaddingBottom());
	}

	@Override
	public void onComplete() {
		pullViewBase.getRollbackScroller().rollback();
	}
}