package me.xiaopan.pullview;

import me.xiaopan.pullview.PullViewBase.PullOrientation;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * 平滑滚动器
 */
public class SmoothScroller {
    private boolean abort;
    private boolean scrolling;
    private boolean isHeader;
	private View view;
    private Scroller scroller;
    private ExecuteRunnable executeRunnable;
    private OnScrollListener onScrollListener;
    private PullOrientation pullOrientation;

	public SmoothScroller(View view, OnScrollListener onScrollListener){
		this.view = view;
		this.onScrollListener = onScrollListener;
        this.executeRunnable = new ExecuteRunnable();
        this.scroller = new Scroller(view.getContext(), new AccelerateDecelerateInterpolator());
	}

    /**
     * 开始滚动
     */
    public void startScroll(PullOrientation pullOrientation, int endLocation, boolean isHeader){
        if(!scroller.isFinished()){
            scroller.abortAnimation();
        }
        this.pullOrientation = pullOrientation;
        this.isHeader = isHeader;
        abort = false;
        scrolling = true;
        if(pullOrientation == PullOrientation.VERTICAL){
        	int currentScrollY = view.getScrollY();
        	scroller.startScroll(0, currentScrollY, 0, Math.abs(currentScrollY - endLocation) * (currentScrollY<0?1:-1));
        	view.post(executeRunnable);
        }else{
        	int currentScrollX = view.getScrollX();
        	scroller.startScroll(currentScrollX, 0, Math.abs(currentScrollX - endLocation) * (currentScrollX<0?1:-1), 0);
        	view.post(executeRunnable);
        }
    }

    /**
     * 开始滚动
     */
    public void rollback(PullOrientation pullOrientation, boolean isHeader){
        startScroll(pullOrientation, 0, isHeader);
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
     * 滚动监听器
     */
    public interface OnScrollListener{
       public void onScroll(boolean isHeader);
       public void onFinishScroll(boolean abort);
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
                if(pullOrientation == PullOrientation.VERTICAL){
                	view.scrollTo(view.getScrollX(), scroller.getCurrY());
                }else{
                	view.scrollTo(scroller.getCurrX(), view.getScrollY());
                }
                if(onScrollListener != null){
                    onScrollListener.onScroll(isHeader);
                }
                view.post(executeRunnable);
            }else{
                if(onScrollListener != null){
                    onScrollListener.onFinishScroll(abort);
                }
                scrolling = false;
            }
        }
    }
}