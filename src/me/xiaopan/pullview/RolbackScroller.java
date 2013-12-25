package me.xiaopan.pullview;

import me.xiaopan.pullview.PullViewBase.PullOrientation;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * 回滚滚动器
 */
public class RolbackScroller {
    private boolean abort;
    private boolean scrolling;
	private PullViewBase<?> pullViewBase;
    private Scroller scroller;
    private ExecuteRunnable executeRunnable;
    private OnRollbackScrollListener onRollbackScrollListener;
    private int duration = 350;

	public RolbackScroller(PullViewBase<?> view, OnRollbackScrollListener onRollbackScrollListener){
		this.pullViewBase = view;
		this.onRollbackScrollListener = onRollbackScrollListener;
        this.executeRunnable = new ExecuteRunnable();
        this.scroller = new Scroller(view.getContext(), new AccelerateDecelerateInterpolator());
	}

    /**
     * 回滚
     * @param endLocation 结束位置
     */
	public void startScroll(int endLocation){
        if(!scroller.isFinished()){
            scroller.abortAnimation();
        }
        abort = false;
        scrolling = true;
        if(pullViewBase.getPullOrientation() == PullOrientation.VERTICAL){
        	int currentScrollY = pullViewBase.getScrollY();
        	scroller.startScroll(0, currentScrollY, 0, Math.abs(currentScrollY - endLocation) * (currentScrollY<0?1:-1), duration);
        	pullViewBase.post(executeRunnable);
        }else{
        	int currentScrollX = pullViewBase.getScrollX();
        	scroller.startScroll(currentScrollX, 0, Math.abs(currentScrollX - endLocation) * (currentScrollX<0?1:-1), 0, duration);
        	pullViewBase.post(executeRunnable);
        }
    }

    /**
     * 回滚
     */
    public void rollback(){
    	startScroll(pullViewBase.getMinScrollValue());
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
    public interface OnRollbackScrollListener{
    	/**
    	 * 回滚
    	 */
    	public void onRollbackScroll();
    	
    	/**
    	 * 回滚完成
    	 * @param isForceAbort 是否被强行中止
    	 */
    	public void onRollbackComplete(boolean isForceAbort);
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
        @Override
        public void run() {
            if(scroller.computeScrollOffset()){
                scrolling = true;
                if(pullViewBase.getPullOrientation() == PullOrientation.VERTICAL){
                	pullViewBase.scrollTo(pullViewBase.getScrollX(), scroller.getCurrY());
                }else{
                	pullViewBase.scrollTo(scroller.getCurrX(), pullViewBase.getScrollY());
                }
                if(onRollbackScrollListener != null){
                    onRollbackScrollListener.onRollbackScroll();
                }
                pullViewBase.post(executeRunnable);
            }else{
                if(onRollbackScrollListener != null){
                    onRollbackScrollListener.onRollbackComplete(abort);
                }
                scrolling = false;
            }
        }
    }

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}