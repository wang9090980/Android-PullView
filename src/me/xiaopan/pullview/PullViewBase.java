package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class PullViewBase<T extends View> extends LinearLayout implements CompositeGestureDetector.GestureListener, SmoothScroller.OnScrollListener{
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private T pullView; //被拉的视图
	private State state;    //状态标识
    private SmoothScroller smoothScroller;
    private CompositeGestureDetector compositeGestureDetector;  //综合的手势识别器

	public PullViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullViewBase(Context context) {
		super(context);
		init();
	}
	
	private void init(){
        setOrientation(LinearLayout.VERTICAL);
        smoothScroller = new SmoothScroller(this, this);
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
    public void onScroll(boolean isHeader) {
        if(isHeader){
            scrollPullViewToHeader(pullView);
        }else{
            scrollPullViewToFooter(pullView);
        }
    }

    @Override
    public void onFinishScroll(boolean abort) {
        if(abort){
            logD("回滚：中断");
        }else{
            logD("回滚：已完成");
            state = State.NORMAL;
        }
    }
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
        compositeGestureDetector.onTouchEvent(ev);
        switch(ev.getAction()){
			case MotionEvent.ACTION_UP :
				logD("弹起");
                if(state == State.PULL_HEADER){
                    smoothScroller.rollback(true);
                }else if(state == State.PULL_FOOTER){
                    smoothScroller.rollback(false);
                }
				break;
			case MotionEvent.ACTION_CANCEL :
				break;
			default :
				break;
		}
		return true;
	}

    @Override
    public boolean onDown(MotionEvent e) {
        if(smoothScroller.isScrolling()){
            smoothScroller.abortScroll();
        }
        return true;
    }

    @Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(state == State.PULL_HEADER){
            if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollBy(0, (int) (distanceY * elasticForce));
                logD("正在垂直拉伸头部，ScrollY=" + getScrollY());
                if(getScrollY() >= 0){
                    logD("头部回滚完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToHeader(pullView);
                return true;
            }else if(getPullOrientation() == PullOrientation.LANDSCAPE){
                scrollBy((int) (distanceX * elasticForce), 0);
                logD("正在横向拉伸头部，ScrollX=" + getScrollX());
                if(getScrollX() >= 0){
                    logD("头部回滚完毕");
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
                logD("正在垂直拉伸尾部，ScrollY=" + getScrollY());
                if(getScrollY() <= 0){
                    logD("尾部回滚完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToFooter(pullView);
                return true;
            }else if(getPullOrientation() == PullOrientation.LANDSCAPE){
                scrollBy((int) (distanceX * elasticForce), 0);
                logD("正在垂横向伸尾部，ScrollX=" + getScrollX());
                if(getScrollX() <= 0){
                    logD("尾部回滚完毕");
                    state = State.NORMAL;
                }
                scrollPullViewToFooter(pullView);
                return true;
            }else{
                return false;
            }
        }else if(distanceY < 0){
            if(isCanPullHeader(pullView)){
                logD("开始拉伸头部");
                state = State.PULL_HEADER;
                return true;
            }else{
                return false;
            }
        }else if(distanceY > 0){
            if(isCanPullFooter(pullView)){
                logD("开始拉伸尾部");
                state = State.PULL_FOOTER;
                return true;
            }else{
                return false;
            }
        }else{
            return false;
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

//	/**
//	 * 创建头部视图
//	 * @return
//	 */
//	public abstract View createHeaderView();
//
//	/**
//	 * 创建尾部视图
//	 * @return
//	 */
//	public abstract View createFooterView();

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
}