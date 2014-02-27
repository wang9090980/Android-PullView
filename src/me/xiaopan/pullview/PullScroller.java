/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private OnScrollListener onRollbackScrollListener;
    private int duration = 350;

	public PullScroller(PullViewBase<?> view, OnScrollListener onRollbackScrollListener){
		this.pullViewBase = view;
		this.onRollbackScrollListener = onRollbackScrollListener;
        this.scroller = new Scroller(view.getContext(), new AccelerateDecelerateInterpolator());
	}

    /**
     * 滚动
     * @param isHeader true：当前滚动的对象是头，false：尾
     * @param endLocation 结束位置
     * @param isScrollToFirstOrEnd 是否在滚动的同时，保证PullView也滚动到头或者尾
     */
	public void scroll(boolean isHeader, int endLocation, boolean isScrollToFirstOrEnd){
        if(!scroller.isFinished()){
            scroller.abortAnimation();
        }
        abort = false;
        scrolling = true;
        if(pullViewBase.isVerticalPull()){
        	int currentScrollY = pullViewBase.getScrollY();
        	scroller.startScroll(0, currentScrollY, 0, -(currentScrollY - endLocation), duration);
        	pullViewBase.post(new ExecuteRunnable(isHeader, isScrollToFirstOrEnd));
        }else{
        	int currentScrollX = pullViewBase.getScrollX();
        	scroller.startScroll(currentScrollX, 0, -(currentScrollX - endLocation), 0, duration);
        	pullViewBase.post(new ExecuteRunnable(isHeader, isScrollToFirstOrEnd));
        }
    }

    /**
     * 显示头部
     */
    public boolean showHeader(){
    	if(pullViewBase.getPullHeaderView() != null){
    		pullViewBase.getPullHeaderView().setStatus(PullHeaderView.Status.READY);
    		scroll(true, -pullViewBase.getPullHeaderView().getTriggerValue(), true);
    		return true;
    	}else{
    		return false;
    	}
    }

    /**
     * 回滚头部
     */
    public void rollbackHeader(){
    	scroll(true, pullViewBase.getPullHeaderView() != null?pullViewBase.getPullHeaderView().getMinScrollValue():0, false);
    }

    /**
     * 显示尾部
     */
    public boolean showFooter(){
    	if(pullViewBase.getPullFooterView() != null){
    		pullViewBase.getPullFooterView().setStatus(PullFooterView.Status.READY);
    		scroll(false, pullViewBase.getPullFooterView().getTriggerValue(), true);
    		return true;
    	}else{
    		return false;
    	}
    }

    /**
     * 回滚尾部
     */
    public void rollbackFooter(){
        scroll(false, pullViewBase.getPullFooterView() != null?pullViewBase.getPullFooterView().getMinScrollValue():0, false);
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
    	 * @param isHeader true：当前滚动的对象是头，false：尾
         * @param isScrollToFirstOrEnd 是否在滚动的同时，保证PullView也滚动到头或者尾
    	 */
    	public void onScroll(boolean isHeader, boolean isScrollToFirstOrEnd);

    	/**
    	 * 回滚完成
    	 * @param isHeader true：当前滚动的对象是头，false：尾
    	 * @param isForceAbort 是否被强行中止
    	 */
    	public void onComplete(boolean isHeader, boolean isForceAbort);
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
    	private boolean isHeader;
    	private boolean isScrollToFirstOrEnd;
    	
    	public ExecuteRunnable(boolean isHeader, boolean isScrollToFirstOrEnd){
    		this.isHeader = isHeader;
    		this.isScrollToFirstOrEnd = isScrollToFirstOrEnd;
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
                    onRollbackScrollListener.onScroll(isHeader, isScrollToFirstOrEnd);
                }
                pullViewBase.post(this);
            }else{
                if(onRollbackScrollListener != null){
                    onRollbackScrollListener.onComplete(isHeader, abort);
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