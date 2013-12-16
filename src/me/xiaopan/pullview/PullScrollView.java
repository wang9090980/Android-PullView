package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class PullScrollView extends PullViewBase<ScrollView> {

	public PullScrollView(Context context) {
		super(context);
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public ScrollView onCreatePullView() {
		return new ScrollView(getContext());
	}

	@Override
	public View onCreateHeaderView() {
		return null;
	}

	@Override
	public View onCreateFooterView() {
		return null;
	}

    @Override
	 protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		boolean returnValue = false;
		Log.d(PullScrollView.class.getSimpleName(), "deltaX="+deltaX+"；deltaY="+deltaY+"；scrollX="+scrollX+"；scrollY="+scrollY+"；scrollRangeX="+scrollRangeX+"；scrollRangeY="+scrollRangeY+"；maxOverScrollX="+maxOverScrollX+"；maxOverScrollY="+maxOverScrollY+"; isTouchEvent="+isTouchEvent);
//        if(scrollY <= 0){
//            deltaY = (int)(deltaY * 0.3);
//        }else if(scrollY >= scrollRangeY){
//            deltaY = (int)(deltaY * 0.3);
//        }
//        if(isTouchEvent){
//            maxOverScrollX = 1000;
//            maxOverScrollY = 1000;
//        }else{
//            maxOverScrollX = 100;
//            maxOverScrollY = 100;
//        }
        returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        return returnValue;
	 }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        switch(ev.getAction()){
            case MotionEvent.ACTION_CANCEL :
                rollBack();
                break;
            case MotionEvent.ACTION_UP :
                rollBack();
                break;
        }
        return result;
    }

    private void rollBack(){
        Log.d(PullScrollView.class.getSimpleName(), "回滚");
    }
}