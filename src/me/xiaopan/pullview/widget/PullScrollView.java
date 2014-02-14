package me.xiaopan.pullview.widget;

import me.xiaopan.pullview.PullViewBase;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 可拉的垂直滚动视图
 */
public class PullScrollView extends PullViewBase<ScrollView> {

	public PullScrollView(Context context) {
		super(context);
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ScrollView createPullView() {
		ScrollView scrollView = new ScrollView(getContext());
		scrollView.setBackgroundColor(Color.WHITE);
		return scrollView;
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
	public boolean isCanPullHeader(ScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentHeight = null != scrollViewChild?scrollViewChild.getHeight():0;  //内容高度
        int viewHeight = getHeight();   //视图高度
        if(contentHeight > viewHeight){
            return pullView.getScrollY() == 0;
        }else{
            return false;
        }
	}

	@Override
	public boolean isCanPullFooter(ScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentHeight = null != scrollViewChild?scrollViewChild.getHeight():0;  //内容高度
        int viewHeight = getHeight();   //视图高度
        if(contentHeight > viewHeight){
            return pullView.getScrollY() >= contentHeight - viewHeight;
        }else{
            return false;
        }
	}

	@Override
	protected boolean isVerticalPull() {
		return true;
	}

    @Override
    protected void scrollPullViewToHeader(ScrollView pullView) {
        pullView.scrollTo(pullView.getScrollX(), 0);
    }

    @Override
    protected void scrollPullViewToFooter(ScrollView pullView) {
        View scrollViewChild = pullView.getChildAt(0);
        int contentHeight = null != scrollViewChild?scrollViewChild.getHeight():0;  //内容高度
        int viewHeight = getHeight();   //视图高度
        if(contentHeight > viewHeight){
            pullView.scrollTo(pullView.getScrollX(), contentHeight - viewHeight);
        }
    }
}