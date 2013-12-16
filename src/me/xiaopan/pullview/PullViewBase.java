package me.xiaopan.pullview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public abstract class PullViewBase<T extends View> extends LinearLayout implements GestureDetector.OnGestureListener{
	private float elasticForce = 0.45f;
	protected T pullView;
	private State state;
	private View headerView;
	private View footerView;
	private Scroller scroller;
	private GestureDetector gestureDetector;
	
	public PullViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullViewBase(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		setBackgroundColor(Color.BLACK);
		pullView = createPullView();
		headerView = createHeaderView();
		footerView = createFooterView();
		addView(pullView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		gestureDetector = new GestureDetector(getContext(), this);
		scroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
	}

//	@Override
//	public void addView(View child, int index, ViewGroup.LayoutParams params) {
//		final T refreshableView = getPullView();
//		if (refreshableView instanceof ViewGroup) {
//			((ViewGroup) refreshableView).addView(child, index, params);
//		} else {
//			throw new UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView");
//		}
//	}
	
	@Override
	public void computeScroll() {
		if(scroller.computeScrollOffset()){
			scrollTo(0, scroller.getCurrY());
			invalidate();
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean intercept = false;
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN : 
				logD("按下");
				break;
			case MotionEvent.ACTION_MOVE : 
				logD("滑动");
				if(state == State.PULL_HEADER){
					logD("正在拉伸头部");
					intercept = true;
				}else if(state == State.PULL_FOOTER){
					logD("正在拉伸尾部");
					intercept = true;
				}else if(isInTheHeader()){
					logD("开始拉伸头部");
					state = State.PULL_HEADER;
					intercept = true;
				}else if(isInTheFooter()){
					logD("开始拉伸尾部");
					state = State.PULL_FOOTER;
					intercept = true;
				}
				break;
			case MotionEvent.ACTION_UP : 
				logD("弹起");
				if(state == State.PULL_HEADER){
					logD("回滚头部，开始位置="+getScrollY()+"；结束位置："+0+"；耗时="+500);
					scroller.startScroll(0, getScrollY(), 0, Math.abs(getScrollY()));
					invalidate();
					state = State.NORMAL;
				}else if(state == State.PULL_FOOTER){
					logD("回滚尾部");
					state = State.NORMAL;
				}
				break;
			case MotionEvent.ACTION_CANCEL : 
				break;
			default : 
				break;
		}
		gestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
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
			if(getPullOrientation() == PullOrientation.VERTICAL){
				scrollBy(0, (int) (distanceY * elasticForce));
			}else{
				scrollBy((int) (distanceX * elasticForce), 0);
			}
			scrollPullViewToHeader();
			return true;
		}else if(state == State.PULL_FOOTER){
			if(getPullOrientation() == PullOrientation.VERTICAL){
				scrollBy(0, (int) (distanceY * elasticForce));
			}else{
				scrollBy((int) (distanceX * elasticForce), 0);
			}
			scrollPullViewToFooter();
			return true;
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

	/**
	 * 创建头视图
	 * @return
	 */
	public abstract View createHeaderView();

	/**
	 * 建尾视图
	 * @return
	 */
	public abstract View createFooterView();
	
	/**
	 * 是否在头部
	 * @return
	 */
	public abstract boolean isInTheHeader();
	
	/**
	 * 是否在尾部
	 * @return
	 */
	public abstract boolean isInTheFooter();
	
	/**
	 * 获取拉伸方向
	 * @return
	 */
	protected abstract PullOrientation getPullOrientation();
	
	/**
	 * 滚动拉伸视图到头部
	 */
	protected abstract void scrollPullViewToHeader();
	
	/**
	 * 滚动拉伸视图到尾部
	 */
	protected abstract void scrollPullViewToFooter();

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