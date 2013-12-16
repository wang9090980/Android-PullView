package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public abstract class PullViewBase<T extends View> extends LinearLayout implements GestureDetector.OnGestureListener{
	private T pullView;
	private View headerView;
	private View footerView;
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
		pullView = onCreatePullView();
		headerView = onCreateHeaderView();
		footerView = onCreateFooterView();
		addView(pullView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		gestureDetector = new GestureDetector(getContext(), this);
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
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(PullWebView.class.getSimpleName(), "distanceX=");
		gestureDetector.onTouchEvent(event);
		return true;
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
//		scrollBy(-(int) distanceX, -(int) distanceY);
		Log.d(PullWebView.class.getSimpleName(), "distanceX="+distanceX+"; distanceY="+distanceY);
		return true;
	}

	public T getPullView(){
		return pullView;
	}

	/**
	 * 当创建内容视图
	 * @return
	 */
	public abstract T onCreatePullView();

	/**
	 * 当创建头视图
	 * @return
	 */
	public abstract View onCreateHeaderView();

	/**
	 * 当创建尾视图
	 * @return
	 */
	public abstract View onCreateFooterView();

	@Override
	public void onShowPress(MotionEvent e) {}
	@Override
	public void onLongPress(MotionEvent e) {}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {return false;}
}