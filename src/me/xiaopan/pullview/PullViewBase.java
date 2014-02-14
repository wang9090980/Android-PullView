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

/**
 * 拉伸视图基类
 * @param <T>
 */
public abstract class PullViewBase<T extends View> extends LinearLayout{
	private int headerMinScrollValue;	//头部最小滚动值
	private int footerMinScrollVaule;	//尾部最小滚动值
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
	private boolean intercept;	//是否拦截事件
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private T pullView; //被拉的视图
	private Status status = Status.NORMAL;    //状态标识
	private PullHeaderView pullHeaderView;  //拉伸头
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
        rollbackScroller = new RolbackScroller(this, new RollbackEventHandleListener(this));
        compositeGestureDetector = new CompositeGestureDetector(getContext(), new TouchEventHandleListener(this));
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
	public boolean dispatchTouchEvent(MotionEvent ev) {
		compositeGestureDetector.onTouchEvent(ev);
        switch(ev.getAction()){
			case MotionEvent.ACTION_UP : rollbackScroller.rollback(); break;
			case MotionEvent.ACTION_CANCEL : rollbackScroller.rollback(); break;
		}
        if(!intercept){
        	super.dispatchTouchEvent(ev);
        }
		return true;
	}

	/**
     * 处理滚动回调
     */
    void handleScrollCallback(){
    	switch(status){
    		case PULL_HEADER :
    			if(pullHeaderView != null){
    	        	pullHeaderView.onScroll(Math.abs(isVerticalPull()?getScrollY():getScrollX()));
    	        }
    			break;
    		case PULL_FOOTER : 
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

	PullHeaderView getPullHeaderView() {
		return pullHeaderView;
	}

    /**
     * 设置拉伸头视图
     * @param pullHeaderView
     */
    public void setPullHeaderView(PullHeaderView pullHeaderView) {
        this.pullHeaderView = pullHeaderView;
        pullHeaderView.setControllCallback(new PullHeaderViewControllCallback(this));
        addViewToSelf = true;
        addView(pullHeaderView, 0);
        addViewToSelf = false;
        ViewUtils.measure(pullHeaderView);
        setPadding(getPaddingLeft(), -pullHeaderView.getMeasuredHeight(), getPaddingRight(), getPaddingBottom());
    }

	RolbackScroller getRollbackScroller() {
		return rollbackScroller;
	}

	int getHeaderMinScrollValue() {
		return headerMinScrollValue;
	}

	void setHeaderMinScrollValue(int headerMinScrollValue) {
		this.headerMinScrollValue = headerMinScrollValue;
	}

	int getFooterMinScrollVaule() {
		return footerMinScrollVaule;
	}

	void setFooterMinScrollVaule(int footerMinScrollVaule) {
		this.footerMinScrollVaule = footerMinScrollVaule;
	}

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

	boolean isIntercept() {
		return intercept;
	}

	void setIntercept(boolean intercept) {
		this.intercept = intercept;
	}

	Status getStatus() {
		return status;
	}

	void setStatus(Status status) {
		this.status = status;
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
}