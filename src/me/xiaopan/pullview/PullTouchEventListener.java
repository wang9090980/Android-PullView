/*
 * Copyright 2013 Peng fei Pan
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

import me.xiaopan.pullview.PullGestureDetector.OnTouchListener;
import me.xiaopan.pullview.PullViewBase.PullStatus;
import android.view.MotionEvent;

/**
 * 触摸事件处理监听器
 */
@SuppressWarnings("rawtypes")
public class PullTouchEventListener implements OnTouchListener {
	private PullViewBase pullViewBase;
    
    public PullTouchEventListener(PullViewBase pullViewBase) {
		super();
		this.pullViewBase = pullViewBase;
	}

	@Override
    public boolean onTouchDown(MotionEvent e) {
        if(pullViewBase.getPullScroller().isScrolling()){
        	pullViewBase.getPullScroller().abortScroll();
        }
        pullViewBase.setIntercept(false);
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
	public boolean onTouchScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		switch(pullViewBase.getPullStatus()){
    		case PULL_HEADER : 
    			if(pullViewBase.isVerticalPull()){
    				pullViewBase.scrollBy(0, (int) (distanceY * pullViewBase.getElasticForce()));
    				if(pullViewBase.getScrollY() >= 0 && Math.abs(distanceY) <= 10){
    					pullViewBase.setPullStatus(PullStatus.NORMAL);
        			}
    			}else{
    				pullViewBase.scrollBy((int) (distanceX * pullViewBase.getElasticForce()), 0);
    				if(pullViewBase.getScrollX() >= 0 && Math.abs(distanceX) <= 10){
    					pullViewBase.setPullStatus(PullStatus.NORMAL);
    				}
    			}
    			pullViewBase.handleScrollCallback();
    			pullViewBase.scrollPullViewToHeader(pullViewBase.getPullView());
    			break;
    		case PULL_FOOTER : 
    			if(pullViewBase.isVerticalPull()){
    				pullViewBase.scrollBy(0, (int) (distanceY * pullViewBase.getElasticForce()));
    				if(pullViewBase.getScrollY() <= 0 && Math.abs(distanceY) <= 10){
    					pullViewBase.setPullStatus(PullStatus.NORMAL);
    				}
    			}else{
    				pullViewBase.scrollBy((int) (distanceX * pullViewBase.getElasticForce()), 0);
    				if(pullViewBase.getScrollX() <= 0 && Math.abs(distanceX) <= 10){
    					pullViewBase.setPullStatus(PullStatus.NORMAL);
    				}
    			}
    			pullViewBase.handleScrollCallback();
    			pullViewBase.scrollPullViewToFooter(pullViewBase.getPullView());
    			break;
    		default : 
    			if(pullViewBase.isVerticalPull()){
    				if(distanceY < 0){	//如果向下拉
//	        			if(pullViewBase.isCanPullHeader(pullViewBase.getPullView())){
//	        				if(pullViewBase.getScrollY() <= (pullViewBase.getPullHeaderView() != null?pullViewBase.getPullHeaderView().getMinScrollValue():0)){
//	        					pullViewBase.logD("滚动：开始拉伸头部，ScrollY=" + pullViewBase.getScrollY());
//	        					pullViewBase.setPullStatus(PullStatus.PULL_HEADER);
//	        				}else{
//	        					pullViewBase.logD("滚动：垂直-正在回滚头部，ScrollY=" + pullViewBase.getScrollY());
//	        					pullViewBase.scrollBy(0, (int) distanceY);
//	        					pullViewBase.scrollPullViewToHeader(pullViewBase.getPullView());
//	        				}
//	        			}
	        			if(pullViewBase.getScrollY() > 0){
	        				//如果Footer正在显示就先通过滚动隐藏Footer
	        				
	        			}else{
	        				//如果可以拉伸Header并且
	        				if(pullViewBase.isCanPullHeader(pullViewBase.getPullView())){
		        				if(pullViewBase.getScrollY() <= (pullViewBase.getPullHeaderView() != null?pullViewBase.getPullHeaderView().getMinScrollValue():0)){
		        					pullViewBase.logD("滚动：开始拉伸头部，ScrollY=" + pullViewBase.getScrollY());
		        					pullViewBase.setPullStatus(PullStatus.PULL_HEADER);
		        				}
		        			}
	        			}
	        		}else if(distanceY > 0){	//如果向上拉
    					if(pullViewBase.getScrollY() < 0){
    						//如果Header正在显示就先通过滚动隐藏Header
    						pullViewBase.logD("滚动：手动回滚Header，ScrollY=" + pullViewBase.getScrollY());
    						pullViewBase.scrollBy(0, (int) (distanceY));
    						pullViewBase.scrollPullViewToHeader(pullViewBase.getPullView());
    					}else{
    						//如果可以拉伸Footer
    						if(pullViewBase.isCanPullFooter(pullViewBase.getPullView())){
    							pullViewBase.logD("滚动：开始拉伸尾部，ScrollY=" + pullViewBase.getScrollY());
    							pullViewBase.setPullStatus(PullStatus.PULL_FOOTER);
    						}
    					}
	        		}
    			}else{
    				if(distanceX< 0){	//如果向右拉
    					if(pullViewBase.isCanPullHeader(pullViewBase.getPullView())){
	        				if(pullViewBase.getScrollX() <= (pullViewBase.getPullHeaderView() != null?pullViewBase.getPullHeaderView().getMinScrollValue():0)){
	        					pullViewBase.logD("滚动：开始拉伸头部，ScrollX=" + pullViewBase.getScrollX());
	        					pullViewBase.setPullStatus(PullStatus.PULL_HEADER);
	        				}else{
	        					pullViewBase.logD("滚动：正在回滚头部，ScrollY=" + pullViewBase.getScrollX());
	        					pullViewBase.scrollBy((int) distanceX, 0);
	        					pullViewBase.scrollPullViewToHeader(pullViewBase.getPullView());
	        				}
	        			}
	        		}else if(distanceX > 0){	//如果向上拉
	        			if(pullViewBase.isCanPullFooter(pullViewBase.getPullView())){
	        				pullViewBase.logD("滚动：开始拉伸尾部，ScrollY=" + pullViewBase.getScrollX());
	        				pullViewBase.setPullStatus(PullStatus.PULL_FOOTER);
	        			}
	        		}
    			}
    			break;
		}
    	return true;
	}
    
    @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	pullViewBase.setIntercept(pullViewBase.getPullStatus() != PullStatus.NORMAL);
		return true;
	}
}
