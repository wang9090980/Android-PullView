package me.xiaopan.pullview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public abstract class PullViewBase<T extends View> extends LinearLayout implements GestureDetector.OnGestureListener{
	private float elasticForce = 0.4f;  //弹力强度，用来实现拉橡皮筋效果
    private boolean self;
    private T pullView;
	private State state;    //状态标识
	private Scroller scroller;  //滚动器，用来回滚头部或尾部
	private GestureDetector gestureDetector;    //手势识别器

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
		pullView = createPullView();
        self = true;
		addView(pullView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        self = false;
		gestureDetector = new GestureDetector(getContext(), this);
		scroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(self){
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
		gestureDetector.onTouchEvent(ev);
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
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(state == State.PULL_HEADER){
            logD("正在拉伸头部");
            if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollBy(0, (int) (distanceY * elasticForce));
                if(getScrollY() == 0){
                    state = State.NORMAL;
                }
            }else{
                scrollBy((int) (distanceX * elasticForce), 0);
                if(getScrollX() == 0){
                    state = State.NORMAL;
                }
            }
            scrollPullViewToHeader(pullView);
            return true;
        }else if(state == State.PULL_FOOTER){
            logD("正在拉伸尾部");
            if(getPullOrientation() == PullOrientation.VERTICAL){
                scrollBy(0, (int) (distanceY * elasticForce));
                if(getScrollY() == 0){
                    state = State.NORMAL;
                }
            }else{
                scrollBy((int) (distanceX * elasticForce), 0);
                if(getScrollX() == 0){
                    state = State.NORMAL;
                }
            }
            scrollPullViewToFooter(pullView);
            return true;
        }else if(distanceY < 0){
            if(isInTheHeader(pullView)){
                logD("开始拉伸头部");
                state = State.PULL_HEADER;
                return true;
            }else{
                return false;
            }
        }else if(distanceY > 0){
            if(isInTheFooter(pullView)){
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
	 * @return
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
	 * 是否在头部
     * @param pullView 拉伸视图
	 * @return
	 */
	public abstract boolean isInTheHeader(T pullView);
	
	/**
	 * 是否在尾部
     * @param pullView 拉伸视图
	 * @return
	 */
	public abstract boolean isInTheFooter(T pullView);
	
	/**
	 * 获取拉伸方向
	 * @return
	 */
	protected abstract PullOrientation getPullOrientation();
	
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

	@Override
	public void onShowPress(MotionEvent e) {}
	
	@Override
	public void onLongPress(MotionEvent e) {}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {return false;}
	
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
		LANDSCAPE;
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
		ROLLBACK_FOOTER;
	}
}