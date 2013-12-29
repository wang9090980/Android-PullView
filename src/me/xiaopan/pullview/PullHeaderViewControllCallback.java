package me.xiaopan.pullview;


public class PullHeaderViewControllCallback implements PullHeaderView.ControllCallback {
	private PullViewBase<?> pullViewBase;
	
	public PullHeaderViewControllCallback(PullViewBase<?> pullViewBase) {
		this.pullViewBase = pullViewBase;
	}
	
	@Override
	public void onShow() {
		pullViewBase.setHeaderMinScrollValue(-pullViewBase.getPullHeaderView().getHeight());
	}

	@Override
	public void onHide() {
		pullViewBase.setHeaderMinScrollValue(0);
	}

	@Override
	public void onRollback() {
		pullViewBase.getRollbackScroller().rollbackHeader();
	}
}