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

import me.xiaopan.pullview.PullScroller.OnScrollListener;
import me.xiaopan.pullview.PullViewBase.PullStatus;

/**
 * 回滚事件处理监听器
 */
@SuppressWarnings("rawtypes")
public class PullScrollListener implements OnScrollListener {
	private PullViewBase pullViewBase;
    
    public PullScrollListener(PullViewBase pullViewBase) {
		super();
		this.pullViewBase = pullViewBase;
	}

    @Override
    public void onScroll(boolean isScrollHeader) {
    	pullViewBase.setForbidTouchEvent(true);
    	pullViewBase.handleScrollCallback();
    }

    @Override
    public void onComplete(boolean isScrollHeader, boolean isForceAbort) {
    	pullViewBase.setForbidTouchEvent(false);
        if(isForceAbort){
        	pullViewBase.logD("回滚：中断");
        }else{
        	pullViewBase.logD("回滚：已完成");
            pullViewBase.setPullStatus(PullStatus.NORMAL);
            if(isScrollHeader){
            	if(pullViewBase.getPullHeaderView() != null){
                	if(pullViewBase.getPullHeaderView().getStatus() == PullHeaderView.Status.READY){
                		pullViewBase.getPullHeaderView().onTrigger();
                	}else if(pullViewBase.getPullHeaderView().getStatus() == PullHeaderView.Status.TRIGGER_TO_NORMAL){
                		pullViewBase.getPullHeaderView().onComplete();
                	}
                }
            }else{
            	if(pullViewBase.getPullFooterView() != null){
                	if(pullViewBase.getPullFooterView().getStatus() == PullFooterView.Status.READY){
                		pullViewBase.getPullFooterView().onTrigger();
                	}else if(pullViewBase.getPullFooterView().getStatus() == PullFooterView.Status.TRIGGER_TO_NORMAL){
                		pullViewBase.getPullFooterView().onComplete();
                	}
                }
            }
        }
    }
}
