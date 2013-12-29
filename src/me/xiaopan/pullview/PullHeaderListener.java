package me.xiaopan.pullview;

import me.xiaopan.pullview.PullHeader.OnStatusChangeListener;

public class PullHeaderListener implements OnStatusChangeListener {
	private PullViewBase<?> pullViewBase;
	
	public PullHeaderListener(PullViewBase<?> pullViewBase) {
		this.pullViewBase = pullViewBase;
	}
	
	@Override
	public void onShow(PullHeader pullHeader) {
		pullViewBase.setHeaderMinScrollValue(-pullViewBase.getPullHeader().getHeight());
	}

	@Override
	public void onHide(PullHeader pullHeader) {
		pullViewBase.setHeaderMinScrollValue(0);
	}

	@Override
	public void onRollback(PullHeader pullHeader) {
		pullViewBase.getRollbackScroller().rollbackHeader();
	}
}