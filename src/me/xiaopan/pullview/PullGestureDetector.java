/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xiaopan.pullview;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 综合手势识别器
 */
public class PullGestureDetector implements GestureDetector.OnGestureListener{
    private GestureDetector gestureDetector;    //手势识别器
    private OnTouchListener onTouchListener;

    public PullGestureDetector(Context context, OnTouchListener onTouchListener) {
        this.gestureDetector = new GestureDetector(context, this);
        this.onTouchListener = onTouchListener;
    }

    public void onTouchEvent(MotionEvent ev){
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP :
                onTouchListener.onTouchUpOrCancel();
                break;
            case MotionEvent.ACTION_CANCEL :
                onTouchListener.onTouchUpOrCancel();
                break;
            default:
                gestureDetector.onTouchEvent(ev);
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return onTouchListener.onTouchDown(e);
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
        return onTouchListener.onTouchScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return onTouchListener.onFling(e1, e2, velocityX, velocityY);
    }

    /**
     * 触摸监听器
     */
    public interface OnTouchListener{
        /**
         * 当按下
         * @param e
         * @return
         */
        public boolean onTouchDown(MotionEvent e);
        
        /**
         * 当滚动
         * @param e1
         * @param e2
         * @param distanceX
         * @param distanceY
         * @return
         */
        public boolean onTouchScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        /**
         * 当飞速滚动
         * @param e1
         * @param e2
         * @param velocityX
         * @param velocityY
         * @return
         */
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);

        /**
         * 当弹起或取消
         */
        public void onTouchUpOrCancel();
    }
}
