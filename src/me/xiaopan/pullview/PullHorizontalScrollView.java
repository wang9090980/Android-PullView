package me.xiaopan.pullview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 可拉的横向滚动视图
 */
public class PullHorizontalScrollView extends PullViewBase<HorizontalScrollView> {

	public PullHorizontalScrollView(Context context) {
		super(context);
	}

	public PullHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected HorizontalScrollView createPullView() {
		HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
		horizontalScrollView.setBackgroundColor(Color.WHITE);
		return horizontalScrollView;
	}

//	@Override
//	public View createHeaderView() {
//		return null;
//	}
//
//	@Override
//	public View createFooterView() {
//		return null;
//	}

	@Override
	public boolean isCanPullHeader(HorizontalScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentWidth = null != scrollViewChild?scrollViewChild.getWidth():0;  //内容宽度
        int viewWidth = getWidth();   //视图宽度
        if(contentWidth > viewWidth){
            return pullView.getScrollX() == 0;
        }else{
            return false;
        }
	}

	@Override
	public boolean isCanPullFooter(HorizontalScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentWidth = null != scrollViewChild?scrollViewChild.getWidth():0;  //内容宽度
        int viewWidth = getWidth();   //视图宽度
        if(contentWidth > viewWidth){
            return pullView.getScrollX() >= contentWidth - viewWidth;
        }else{
            return false;
        }
	}

	@Override
	protected boolean isVerticalPull() {
		return false;
	}

    @Override
    protected void scrollPullViewToHeader(HorizontalScrollView pullView) {
        pullView.scrollTo(0, pullView.getScrollY());
    }

    @Override
    protected void scrollPullViewToFooter(HorizontalScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentWidth = null != scrollViewChild?scrollViewChild.getWidth():0;  //内容宽度
        int viewWidth = getWidth();   //视图宽度
        if(contentWidth > viewWidth){
            pullView.scrollTo(contentWidth - viewWidth, pullView.getScrollY());
        }
    }
}