package me.xiaopan.pullview;


public class PullHeaderViewControllCallback implements PullHeaderView.ControllCallback {
	private PullViewBase<?> pullViewBase;
	
	public PullHeaderViewControllCallback(PullViewBase<?> pullViewBase) {
		this.pullViewBase = pullViewBase;
	}
	
	@Override
	public void onRollback() {
		pullViewBase.getPullScroller().rollbackHeader();
	}
}