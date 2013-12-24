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
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private T pullView; //被拉的视图
	private State state;    //状态标识
	private PullHeader pullHeader;  //拉伸头
    private RolbackScroller rollbackScroller;  //滚动器，用来回滚
    private CompositeGestureDetector compositeGestureDetector;  //综合的手势识别器

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
		super.dispatchTouchEvent(ev);
        compositeGestureDetector.onTouchEvent(ev);
        switch(ev.getAction()){
			case MotionEvent.ACTION_UP : handleUpTouchEvent("弹起"); break;
			case MotionEvent.ACTION_CANCEL : handleUpTouchEvent("取消"); break;
		}
		return true;
	}
    
    @Override
    public boolean onTouchDown(MotionEvent e) {
        if(rollbackScroller.isScrolling()){
            rollbackScroller.abortScroll();
        }
        return true;
    }

    @Override
	public boolean onTouchScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(state == State.PULL_HEADER){
            if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollBy(0, (int) (distanceY * elasticForce));
                handleScrollCallback();
                logD("滚动：垂直-正在拉伸头部，ScrollY=" + getScrollY());
                if(getScrollY() >= 0){
                    logD("滚动：垂直-手动回滚头部完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToHeader(pullView);
                return true;
            }else if(getPullOrientation() == PullOrientation.LANDSCAPE){
                scrollBy((int) (distanceX * elasticForce), 0);
                handleScrollCallback();
                logD("滚动：横向-正在拉伸头部，ScrollX=" + getScrollX());
                if(getScrollX() >= 0){
                    logD("滚动：横向-手动回滚头部完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToHeader(pullView);
                return true;
            }else{
                return false;
            }
        }else if(state == State.PULL_FOOTER){
            if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollBy(0, (int) (distanceY * elasticForce));
                handleScrollCallback();
                logD("滚动：垂直-正在拉伸尾部，ScrollY=" + getScrollY());
                if(getScrollY() <= 0){
                    logD("滚动：垂直-手动回滚尾部完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToFooter(pullView);
                return true;
            }else if(getPullOrientation() == PullOrientation.LANDSCAPE){
                scrollBy((int) (distanceX * elasticForce), 0);
                handleScrollCallback();
                logD("滚动：横向-正在拉伸尾部，ScrollX=" + getScrollX());
                if(getScrollX() <= 0){
                    logD("滚动：横向-手动回滚尾部完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToFooter(pullView);
                return true;
            }else{
                return false;
            }
        }else if((getPullOrientation() == PullOrientation.VERTICAL?distanceY:distanceX) < 0){
            if(isCanPullHeader(pullView)){
                logD("滚动：开始拉伸头部");
                state = State.PULL_HEADER;
                return true;
            }else{
                return false;
            }
        }else if((getPullOrientation() == PullOrientation.VERTICAL?distanceY:distanceX) > 0){
            if(isCanPullFooter(pullView)){
                logD("滚动：开始拉伸尾部");
                state = State.PULL_FOOTER;
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
	}
    
    /**
     * 处理滚动回调
     */
    private void handleScrollCallback(){
    	switch(state){
    		case PULL_HEADER :
    			if(pullHeader != null){
    	        	pullHeader.onScroll(Math.abs(getPullOrientation() == PullOrientation.VERTICAL?getScrollY():getScrollX()));
    	        }
    			break;
    		case PULL_FOOTER : 
    			break;
    		case NORMAL : 
    			break;
    	}
    }

    /**
     * 处理弹起事件
     * @param eventActionName
     */
    private void handleUpTouchEvent(String eventActionName){
        if(getPullOrientation() == PullOrientation.VERTICAL){
            if(getScrollY() != 0){
                if(state == State.PULL_HEADER){
                    logD(eventActionName+"：垂直-回滚-头部");
                    rollbackScroller.rollback();
                }else if(state == State.PULL_FOOTER){
                    logD(eventActionName+"：垂直-回滚-尾部");
                    rollbackScroller.rollback();
                }else{
                    logD(eventActionName+"：垂直-回滚-没有目的");
                    rollbackScroller.rollback();
                }
            }else{
                logD(eventActionName+"：垂直-无需回滚");
            }
        }else if(getPullOrientation() == PullOrientation.LANDSCAPE){
            if(getScrollX() != 0){
                if(state == State.PULL_HEADER){
                    logD(eventActionName+"：横向-回滚-头部");
                    rollbackScroller.rollback();
                }else if(state == State.PULL_FOOTER){
                    logD(eventActionName+"：横向-回滚-尾部");
                    rollbackScroller.rollback();
                }else{
                    logD(eventActionName+"：横向-回滚-没有目的");
                    rollbackScroller.rollback();
                }
            }else{
                logD(eventActionName+"：横向-无需回滚");
            }
        }else{
            logD(eventActionName+"：未知的拉伸方向");
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
            state = State.NORMAL;
            if(pullHeader != null){
            	if(pullHeader.getStatus() == PullHeader.Status.READY){
            		pullHeader.onTrigger();
            	}else if(pullHeader.getStatus() == PullHeader.Status.TRIGGER_TO_NORMAL){
            		pullHeader.onComplete();
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
        pullHeader.setOnStatusChangeListener(new PullHeaderStausChangeListener(this));
        addViewToSelf = true;
        addView(pullHeader, 0);
        addViewToSelf = false;
        ViewUtils.measure(pullHeader);
        setPadding(getPaddingLeft(), -pullHeader.getMeasuredHeight(), getPaddingRight(), getPaddingBottom());
    }

    /**
     * 获取拉伸方向
     * @return 拉伸方向
     */
    protected abstract PullOrientation getPullOrientation();

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
	 * 方向，定义拉伸方向
	 */
	public enum PullOrientation{
		/**
		 * 垂直拉伸
		 */
		VERTICAL,
		
		/**
		 * 横向拉伸
		 */
		LANDSCAPE,
	}
	
	/**
	 * 状态
	 */
	public enum State{
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
}