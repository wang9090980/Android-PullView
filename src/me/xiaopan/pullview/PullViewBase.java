package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public abstract class PullViewBase<T extends View> extends LinearLayout implements CompositeGestureDetector.GestureListener{
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
    private boolean addViewToSelf;  //给自己添加视图，当为true的时候新视图将添加到自己的ViewGroup里，否则将添加到pullView（只有pullView是ViewGroup的时候才会添加成功）里
    private T pullView; //被拉的视图
	private State state;    //状态标识
	private Scroller scroller;  //滚动器，用来回滚头部或尾部
    private CompositeGestureDetector compositeGestureDetector;  //综合的手势识别器

	public PullViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
        compositeGestureDetector = new CompositeGestureDetector(context, this);
	}

	public PullViewBase(Context context) {
		super(context);
		init();
	}
	
	private void init(){
        setOrientation(LinearLayout.VERTICAL);
		pullView = createPullView();
        addViewToSelf = true;
		addView(pullView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addViewToSelf = false;
		scroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
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
	public void computeScroll() {
		if(scroller.computeScrollOffset()){
			if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollTo(0, scroller.getCurrY());
                invalidate();
            }else if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollTo(scroller.getCurrX(), 0);
                invalidate();
            }
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean result = super.dispatchTouchEvent(ev);
        switch(ev.getAction()){
			case MotionEvent.ACTION_UP :
				logD("弹起");
                if(state == State.PULL_HEADER){
                    rollback();
					state = State.NORMAL;
				}else if(state == State.PULL_FOOTER){
                    rollback(-Math.abs(getScrollY()));
					state = State.NORMAL;
				}
				break;
			case MotionEvent.ACTION_CANCEL : 
				break;
			default : 
				break;
		}
        compositeGestureDetector.onTouchEvent(ev);
		return result;
	}

    private void rollback(int dy){
        logD("回滚，开始位置="+getScrollY()+"；距离："+dy+"；耗时="+500);
        scroller.startScroll(0, getScrollY(), 0, dy);
        invalidate();
    }

    private void rollback(){
        rollback(Math.abs(getScrollY()));
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if(scroller.computeScrollOffset()){
            scroller.abortAnimation();
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
		LANDSCAPE
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
		
		/**
		 * 回滚头部
		 */
		ROLLBACK_HEADER, 
		
		/**
		 * 回滚尾部
		 */
		ROLLBACK_FOOTER
	}
}