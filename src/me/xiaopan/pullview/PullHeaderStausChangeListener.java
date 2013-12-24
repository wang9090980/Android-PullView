package me.xiaopan.pullview;

import me.xiaopan.pullview.PullHeader.OnStatusChangeListener;

public class PullHeaderStausChangeListener implements OnStatusChangeListener {
	private PullViewBase<?> pullViewBase;
	
	public PullHeaderStausChangeListener(PullViewBase<?> pullViewBase) {
		this.pullViewBase = pullViewBase;
	}

	@Override
	public void onComplete() {
		pullViewBase.getRollbackScroller().rollback();
	}
}