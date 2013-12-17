package me.xiaopan.pullview;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 综合手势识别器
 */
public class CompositeGestureDetector implements GestureDetector.OnGestureListener{
    private GestureListener gestureListener;
    private GestureDetector gestureDetector;    //手势识别器

    public CompositeGestureDetector(Context context, GestureListener gestureListener) {
        this.gestureListener = gestureListener;
        gestureDetector = new GestureDetector(context, this);
    }

    public void onTouchEvent(MotionEvent ev){
        gestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return gestureListener.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    public interface GestureListener{
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    }
}
