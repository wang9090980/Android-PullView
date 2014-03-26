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

import me.xiaopan.easy.android.util.ViewUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

/**
 * 拉伸视图基类
 * @param <T>
 */
public abstract class PullViewBase<T extends View> extends LinearLayout implements PullViewInterface<T>{
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
	private boolean interceptTouchEvent;	//是否拦截触摸事件
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private T pullView; //被拉的视图
	private PullStatus pullStatus = PullStatus.NORMAL;    //状态标识
	private PullScroller pullScroller;  //滚动器，用来回滚
	private PullHeaderView pullHeaderView;  //头
	private PullFooterView pullFooterView;	//尾巴
    private PullGestureDetector pullGestureDetector;  //综合的手势识别器

	public PullViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullViewBase(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        setOrientation(isVerticalPull()?LinearLayout.VERTICAL:LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        pullScroller = new PullScroller(this, new PullScrollListener(this));
        pullGestureDetector = new PullGestureDetector(getContext(), new PullTouchListener(this));
        addViewToSelf = true;
        addView(pullView = (T) createPullView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addViewToSelf = false;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(child != null){
            if(addViewToSelf){
                super.addView(child, index, params);
            }else if(pullView instanceof ViewGroup){
                ((ViewGroup) pullView).addView(child, index, params);
            }else{
                super.addView(child, index, params);
            }
        }
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
		/* 动态设置PullView的高度或宽度，这么做是为了让Footer显示出来 */
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pullView.getLayoutParams();
        if(lp != null){
            if(isVerticalPull()){
                if (lp.height != h) {
                    lp.height = h;
                    pullView.requestLayout();
                }
            }else{
                if (lp.width != w) {
                    lp.width = w;
                    pullView.requestLayout();
                }
            }
        }

        /**
         * As we're currently in a Layout Pass, we need to schedule another one
         * to layout any changes we've made here
         */
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return interceptTouchEvent;
    }

    @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
        pullGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
	}

	/**
	 * 获取拉伸视图
	 * @return
	 */
	public T getPullView(){
		return pullView;
	}

	/**
	 * 获取拉伸头视图
	 * @return
	 */
	PullHeaderView getPullHeaderView() {
		return pullHeaderView;
	}

    /**
     * 设置拉伸头视图
     * @param pullHeaderView
     */
    public void setPullHeaderView(PullHeaderView pullHeaderView) {
        this.pullHeaderView = pullHeaderView;
        pullHeaderView.setPullViewBase(this);
        pullHeaderView.setControllCallback(new PullHeaderView.ControllCallback() {
			@Override
			public void onRollback() {
				getPullScroller().rollbackHeader();
			}
		});
        addViewToSelf = true;
        addView(pullHeaderView, 0, new LayoutParams(isVerticalPull()?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT, isVerticalPull()?ViewGroup.LayoutParams.WRAP_CONTENT:ViewGroup.LayoutParams.MATCH_PARENT));
        addViewToSelf = false;
        ViewUtils.measure(pullHeaderView);
        if(isVerticalPull()){
        	setPadding(getPaddingLeft(), getPaddingTop()-pullHeaderView.getMeasuredHeight(), getPaddingRight(), getPaddingBottom());
        }else{
        	setPadding(getPaddingLeft()-pullHeaderView.getMeasuredWidth(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        requestLayout();
    }
    
    PullFooterView getPullFooterView() {
		return pullFooterView;
	}

	/**
     * 设置尾巴
     * @param pullFooterView
     */
    public void setPullFooterView(PullFooterView pullFooterView) {
		this.pullFooterView = pullFooterView;
		pullFooterView.setPullViewBase(this);
		pullFooterView.setControllCallback(new PullFooterView.ControllCallback() {
			@Override
			public void onRollback() {
				getPullScroller().rollbackFooter();
			}
		});
        addViewToSelf = true;
        addView(pullFooterView, new LayoutParams(isVerticalPull()?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT, isVerticalPull()?ViewGroup.LayoutParams.WRAP_CONTENT:ViewGroup.LayoutParams.MATCH_PARENT));
        addViewToSelf = false;
        ViewUtils.measure(pullFooterView);
        if(isVerticalPull()){
        	setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()-pullFooterView.getMeasuredHeight());
        }else{
        	setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight()-pullFooterView.getMeasuredWidth(), getPaddingBottom());
        }
        requestLayout();
	}

	/**
     * 触发Header
     * @return true：启动成功；false：启动失败，原因是没有Header或Header正在触发中
     */
    public boolean triggerHeader(){
    	if(pullHeaderView != null && !pullHeaderView.isTriggering()){
    		if(getHeight() == 0){
    			getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
    				@Override
    				public void onGlobalLayout() {
    					triggerHeader();
    					ViewUtils.removeOnGlobalLayoutListener(getViewTreeObserver(), this);
    				}
    			});
    		}else{
    			pullScroller.showHeader();
    		}
    		return true;
    	}else{
    		return false;
    	}
    }
    
	/**
     * 触发Footer
     * @return true：启动成功；false：启动失败，原因是没有Footer或Footer正在触发中
     */
    public boolean triggerFooter(){
    	if(pullFooterView != null && !pullFooterView.isTriggering()){
    		if(getHeight() == 0){
    			getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
    				@Override
    				public void onGlobalLayout() {
    					triggerFooter();
    					ViewUtils.removeOnGlobalLayoutListener(getViewTreeObserver(), this);
    				}
    			});
    		}else{
    			pullScroller.showFooter();
    		}
    		return true;
    	}else{
    		return false;
    	}
    }

	/**
	 * 获取滚动器
	 * @returnN
	 */
	PullScroller getPullScroller() {
		return pullScroller;
	}

	/**
	 * 获取弹力强度
	 * @return
	 */
	float getElasticForce() {
		return elasticForce;
	}

	/**
	 * 设置弹力强度
	 * @param elasticForce
	 */
	public void setElasticForce(float elasticForce) {
		this.elasticForce = elasticForce;
	}

	/**
	 * 设置是否中断事件传递
	 * @param interceptTouchEvent
	 */
	void setInterceptTouchEvent(boolean interceptTouchEvent) {
		this.interceptTouchEvent = interceptTouchEvent;
	}

	/**
	 * 获取拉伸状态
	 * @return
	 */
	PullStatus getPullStatus() {
		return pullStatus;
	}

	/**
	 * 设置拉伸状态
	 * @param pullStatus
	 */
	void setPullStatus(PullStatus pullStatus) {
		this.pullStatus = pullStatus;
	}

    public static final void logI(String msg){
		Log.i(PullViewBase.class.getSimpleName(), msg);
	}

    public static final void logD(String msg){
		Log.d(PullViewBase.class.getSimpleName(), msg);
	}

    public static final void logE(String msg){
		Log.e(PullViewBase.class.getSimpleName(), msg);
	}
	
	/**
	 * 拉动状态
	 */
	public enum PullStatus{
		/**
		 * 正常
		 */
		NORMAL, 
		
		/**
		 * 拉伸头部
		 */
		PULL_HEADER, 
		
		/**
		 * 拉伸尾部
		 */
		PULL_FOOTER, 
	}
}