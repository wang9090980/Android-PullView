package me.xiaopan.pullview;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * 回滚滚动器
 */
public class PullScroller {
    private boolean abort;
    private boolean scrolling;
	private PullViewBase<?> pullViewBase;
    private Scroller scroller;
//    private ExecuteRunnable executeRunnable;
    private OnScrollListener onRollbackScrollListener;
    private int duration = 350;

	public PullScroller(PullViewBase<?> view, OnScrollListener onRollbackScrollListener){
		this.pullViewBase = view;
		this.onRollbackScrollListener = onRollbackScrollListener;
//        this.executeRunnable = new ExecuteRunnable();
        this.scroller = new Scroller(view.getContext(), new AccelerateDecelerateInterpolator());
	}

    /**
     * 回滚
     * @param isScrollHeader
     * @param endLocation 结束位置
     */
	public void scroll(boolean isScrollHeader, int endLocation){
        if(!scroller.isFinished()){
            scroller.abortAnimation();
        }
        abort = false;
        scrolling = true;
        if(pullViewBase.isVerticalPull()){
        	int currentScrollY = pullViewBase.getScrollY();
        	scroller.startScroll(0, currentScrollY, 0, Math.abs(currentScrollY - endLocation) * (currentScrollY<0?1:-1), duration);
        	pullViewBase.post(new ExecuteRunnable(isScrollHeader));
        }else{
        	int currentScrollX = pullViewBase.getScrollX();
        	scroller.startScroll(currentScrollX, 0, Math.abs(currentScrollX - endLocation) * (currentScrollX<0?1:-1), 0, duration);
        	pullViewBase.post(new ExecuteRunnable(isScrollHeader));
        }
    }

    /**
     * 回滚头部
     */
    public void rollbackHeader(){
    	scroll(true, pullViewBase.getPullHeaderView() != null?pullViewBase.getPullHeaderView().getMinScrollValue():0);
    }

    /**
     * 回滚尾部
     */
    public void rollbackFooter(){
        scroll(false, pullViewBase.getPullFooterView() != null?pullViewBase.getPullFooterView().getMinScrollValue():0);
    }

    /**
     * 回滚
     */
    public void rollback(){
    	switch(pullViewBase.getPullStatus()){
    		case NORMAL :
    			pullViewBase.logD("无需回滚");
    			break;
	    	case PULL_HEADER :
	    		pullViewBase.logD("回滚-头部");
	    		rollbackHeader();
	    		break;
	    	case PULL_FOOTER :
	    		pullViewBase.logD("回滚-尾部");
	    		rollbackFooter();
	    		break;
    	}
    }

    /**
     * 中断滚动
     */
    public void abortScroll(){
        if(!scroller.isFinished()){
            abort = true;
            scroller.abortAnimation();
        }
    }

    /**
     * 回滚滚动监听器
     */
    public interface OnScrollListener{
    	/**
    	 * 滚动
    	 * @param isScrollHeader
    	 */
    	public void onScroll(boolean isScrollHeader);

    	/**
    	 * 回滚完成
    	 * @param isScrollHeader
    	 * @param isForceAbort 是否被强行中止
    	 */
    	public void onComplete(boolean isScrollHeader, boolean isForceAbort);
    }

    /**
     * 滚动中
     * @return
     */
    public boolean isScrolling() {
        return scrolling;
    }

    /**
     * 执行Runnable
     */
    private class ExecuteRunnable implements Runnable{
    	private boolean isScrollHeader;
    	
    	public ExecuteRunnable(boolean isScrollHeader){
    		this.isScrollHeader = isScrollHeader;
    	}
    	
        @Override
        public void run() {
            if(scroller.computeScrollOffset()){
                scrolling = true;
                if(pullViewBase.isVerticalPull()){
                	pullViewBase.scrollTo(pullViewBase.getScrollX(), scroller.getCurrY());
                }else{
                	pullViewBase.scrollTo(scroller.getCurrX(), pullViewBase.getScrollY());
                }
                pullViewBase.invalidate();
                if(onRollbackScrollListener != null){
                    onRollbackScrollListener.onScroll(isScrollHeader);
                }
                pullViewBase.post(this);
            }else{
                if(onRollbackScrollListener != null){
                    onRollbackScrollListener.onComplete(isScrollHeader, abort);
                }
                scrolling = false;
            }
        }
    }

	/**
	 * 获取滚动持续时间
	 * @return
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * 设置滚动持续时间
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
}