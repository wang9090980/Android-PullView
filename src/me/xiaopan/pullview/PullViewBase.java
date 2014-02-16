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
public abstract class PullViewBase<T extends View> extends LinearLayout{
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
	private boolean intercept;	//是否拦截事件
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private boolean forbidTouchEvent;	//禁止触摸事件
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
        addView(pullView = createPullView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addViewToSelf = false;
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(addViewToSelf){
            super.addView(child, index, params);
        }else if(pullView instanceof ViewGroup){
            ((ViewGroup) pullView).addView(child, index, params);
        }else{
        	super.addView(child, index, params);
        }
	}
	
	@Override
	protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		/* 动态设置PullView的高度或宽度，这么做是为了让Footer显示出来 */
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pullView.getLayoutParams();
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
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!forbidTouchEvent){
			pullGestureDetector.onTouchEvent(ev);
			switch(ev.getAction()){
				case MotionEvent.ACTION_UP : pullScroller.rollback(); break;
				case MotionEvent.ACTION_CANCEL : pullScroller.rollback(); break;
			}
			if(!intercept){
				super.dispatchTouchEvent(ev);
			}
			return true;
		}else{
			return false;
		}
	}

	/**
     * 处理滚动回调
     */
    void handleScrollCallback(){
    	switch(pullStatus){
    		case PULL_HEADER :
    			if(pullHeaderView != null){
    	        	pullHeaderView.onScroll(Math.abs(isVerticalPull()?getScrollY():getScrollX()));
    	        }
    			break;
    		case PULL_FOOTER : 
    			if(pullFooterView != null){
    				pullFooterView.onScroll(Math.abs(isVerticalPull()?getScrollY():getScrollX()));
    	        }
    			break;
    		case NORMAL : 
    			break;
    	}
    }

	/**
	 * 创建内容视图
	 * @return 内容视图
	 */
    protected abstract T createPullView();

    /**
     * 是否是垂直拉伸
     * @return true：垂直拉伸；false：横向拉伸
     */
    protected abstract boolean isVerticalPull();

	/**
	 * 是否可以拉头部
     * @param pullView 拉伸视图
	 * @return 是否可以拉头部
	 */
    protected abstract boolean isCanPullHeader(T pullView);
	
	/**
	 * 是否可以拉尾部
     * @param pullView 拉伸视图
	 * @return 是否可以拉尾部
	 */
    protected abstract boolean isCanPullFooter(T pullView);
	
	/**
	 * 滚动拉伸视图到头部
     * @param pullView 拉伸视图
	 */
	protected abstract void scrollPullViewToHeader(T pullView);

	/**
	 * 滚动拉伸视图到尾部
     * @param pullView 拉伸视图
	 */
	protected abstract void scrollPullViewToFooter(T pullView);

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
    			pullHeaderView.setStatus(PullHeaderView.Status.READY);
    			pullScroller.scroll(true, isVerticalPull()?pullHeaderView.getHeight():pullHeaderView.getWidth());
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
    			pullFooterView.setStatus(PullFooterView.Status.READY);
    			pullScroller.scroll(false, -(isVerticalPull()?pullFooterView.getHeight():pullFooterView.getWidth()));
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
	 * 是否需要中断事件传递
	 * @return
	 */
	boolean isIntercept() {
		return intercept;
	}

	/**
	 * 设置是否中断事件传递
	 * @param intercept
	 */
	void setIntercept(boolean intercept) {
		this.intercept = intercept;
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
	
	/**
	 * 是否禁止触摸事件
	 * @return
	 */
	boolean isForbidTouchEvent() {
		return forbidTouchEvent;
	}

	/**
	 * 设置是否禁止触摸事件
	 * @param forbidTouchEvent
	 */
	void setForbidTouchEvent(boolean forbidTouchEvent) {
		this.forbidTouchEvent = forbidTouchEvent;
	}

	void logI(String msg){
		Log.i(PullViewBase.class.getSimpleName(), msg);
	}
	
	void logD(String msg){
		Log.d(PullViewBase.class.getSimpleName(), msg);
	}
	
	void logE(String msg){
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