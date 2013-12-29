package me.xiaopan.pullview;

import me.xiaopan.easy.android.util.ViewUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class PullViewBase<T extends View> extends LinearLayout implements CompositeGestureDetector.OnTouchListener, RolbackScroller.OnRollbackScrollListener{
	private int headerMinScrollValue;	//头部最小滚动值
	private int footerMinScrollVaule;	//尾部最小滚动值
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
	private boolean showing;
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private T pullView; //被拉的视图
	private Status status = Status.NORMAL;    //状态标识
	private PullHeader pullHeader;  //拉伸头
    private RolbackScroller rollbackScroller;  //滚动器，用来回滚
    private CompositeGestureDetector compositeGestureDetector;  //综合的手势识别器
    private boolean lanjie;

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
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        rollbackScroller = new RolbackScroller(this, this);
        compositeGestureDetector = new CompositeGestureDetector(getContext(), this);
		pullView = createPullView();
        addViewToSelf = true;
		addView(pullView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addViewToSelf = false;
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(addViewToSelf){
            super.addView(child, index, params);
        }else if(pullView instanceof ViewGroup){
            ((ViewGroup) pullView).addView(child, index, params);
        }
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		compositeGestureDetector.onTouchEvent(ev);
        switch(ev.getAction()){
			case MotionEvent.ACTION_UP : rollbackScroller.rollback(); break;
			case MotionEvent.ACTION_CANCEL : rollbackScroller.rollback(); break;
		}
        if(!lanjie){
        	super.dispatchTouchEvent(ev);
        }
		return true;
	}
    
    @Override
    public boolean onTouchDown(MotionEvent e) {
        if(rollbackScroller.isScrolling()){
            rollbackScroller.abortScroll();
        }
        lanjie = false;
        return true;
    }

    @Override
	public boolean onTouchScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		switch(status){
    		case PULL_HEADER : 
    			if(isVerticalPull()){
    				scrollBy(0, (int) (distanceY * elasticForce));
    			}else{
    				scrollBy((int) (distanceX * elasticForce), 0);
    			}
    			if((isVerticalPull()?getScrollY():getScrollX()) >= 0 && Math.abs(isVerticalPull()?distanceY:distanceX) <= 10){
    				status = Status.NORMAL;
    			}
    			handleScrollCallback();
    			scrollPullViewToHeader(pullView);
    			break;
    		case PULL_FOOTER : 
    			if(isVerticalPull()){
    				scrollBy(0, (int) (distanceY * elasticForce));
    			}else{
    				scrollBy((int) (distanceX * elasticForce), 0);
    			}
    			if((isVerticalPull()?getScrollY():getScrollX()) <= 0 && Math.abs(isVerticalPull()?distanceY:distanceX) <= 10){
    				status = Status.NORMAL;
    			}
    			handleScrollCallback();
    			scrollPullViewToFooter(pullView);
    			break;
    		default : 
    			if(isVerticalPull()){
	    			logD("ScrollY");
    				if(distanceY < 0){	//如果向下拉
	        			if(showing && getScrollY() > headerMinScrollValue){
	        				scrollBy(0, (int) distanceY);
	        				scrollPullViewToHeader(pullView);
	        			}else if(isCanPullHeader(pullView)){
        					logD("滚动：开始拉伸头部");
        					status = Status.PULL_HEADER;
	        			}
	        		}else if(distanceY > 0){	//如果向上拉
	        			if(showing && getScrollY() < 0){
	        				logD("滚动：垂直-正在回滚头部，ScrollY=" + getScrollY());
	        				scrollBy(0, (int) (distanceY));
	        				scrollPullViewToHeader(pullView);
	        			}else if(isCanPullFooter(pullView)){
	        				logD("滚动：开始拉伸尾部");
	        				status = Status.PULL_FOOTER;
	        			}
	        		}
    			}else{
    				if(distanceX< 0){	//如果向下拉
	        			if(distanceX > headerMinScrollValue){
	        				scrollBy((int) distanceX, 0);
	        				scrollPullViewToHeader(pullView);
	        			}else{
	        				if(isCanPullHeader(pullView)){
	        					logD("滚动：开始拉伸头部");
	        					status = Status.PULL_HEADER;
	        				}
	        			}
	        		}else if(distanceX > 0){	//如果向上拉
	        			if(isCanPullFooter(pullView)){
	        				logD("滚动：开始拉伸尾部");
	        				status = Status.PULL_FOOTER;
	        			}
	        		}
    			}
    			break;
		}
    	return true;
	}
    
    @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	lanjie = status != Status.NORMAL;
		return true;
	}

	/**
     * 处理滚动回调
     */
    private void handleScrollCallback(){
    	switch(status){
    		case PULL_HEADER :
    			if(pullHeader != null){
    	        	pullHeader.onScroll(Math.abs(isVerticalPull()?getScrollY():getScrollX()));
    	        }
    			break;
    		case PULL_FOOTER : 
    			break;
    		case NORMAL : 
    			break;
    	}
    }

    @Override
    public void onRollbackScroll() {
        handleScrollCallback();
    }

    @Override
    public void onRollbackComplete(boolean isForceAbort) {
        if(isForceAbort){
            logD("回滚：中断");
        }else{
            logD("回滚：已完成");
            status = Status.NORMAL;
            if(pullHeader != null){
            	if(pullHeader.getStatus() == PullHeader.Status.READY){
            		pullHeader.onTrigger();
            		showing = true;
            	}else if(pullHeader.getStatus() == PullHeader.Status.TRIGGER_TO_NORMAL){
            		pullHeader.onComplete();
            		showing = false;
            	}
            }
        }
    }

	public T getPullView(){
		return pullView;
	}

	/**
	 * 创建内容视图
	 * @return 内容视图
	 */
	public abstract T createPullView();

    /**
     * 设置拉伸头
     * @param pullHeader
     */
    public void setPullHeader(PullHeader pullHeader) {
        this.pullHeader = pullHeader;
        pullHeader.setOnStatusChangeListener(new PullHeaderListener(this));
        addViewToSelf = true;
        addView(pullHeader, 0);
        addViewToSelf = false;
        ViewUtils.measure(pullHeader);
        setPadding(getPaddingLeft(), -pullHeader.getMeasuredHeight(), getPaddingRight(), getPaddingBottom());
    }

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
	public abstract boolean isCanPullHeader(T pullView);
	
	/**
	 * 是否可以拉尾部
     * @param pullView 拉伸视图
	 * @return 是否可以拉尾部
	 */
	public abstract boolean isCanPullFooter(T pullView);
	
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

	public void logI(String msg){
		Log.i(PullViewBase.class.getSimpleName(), msg);
	}
	
	public void logD(String msg){
		Log.d(PullViewBase.class.getSimpleName(), msg);
	}
	
	public void logE(String msg){
		Log.e(PullViewBase.class.getSimpleName(), msg);
	}
	
	/**
	 * 状态
	 */
	public enum Status{
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

	public PullHeader getPullHeader() {
		return pullHeader;
	}

	public RolbackScroller getRollbackScroller() {
		return rollbackScroller;
	}

	public int getHeaderMinScrollValue() {
		return headerMinScrollValue;
	}

	void setHeaderMinScrollValue(int headerMinScrollValue) {
		this.headerMinScrollValue = headerMinScrollValue;
	}

	public int getFooterMinScrollVaule() {
		return footerMinScrollVaule;
	}

	void setFooterMinScrollVaule(int footerMinScrollVaule) {
		this.footerMinScrollVaule = footerMinScrollVaule;
	}

	public Status getStatus() {
		return status;
	}
}