package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;

/**
 * 可拉的AdapterView
 */
public abstract class PullAdapterViewBase<T extends AbsListView> extends PullViewBase<T>{
    public PullAdapterViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullAdapterViewBase(Context context) {
        super(context);
    }

    @Override
    protected boolean isVerticalPull() {
        return true;
    }

    @Override
    protected boolean isCanPullHeader(AbsListView pullView) {
        boolean result = false;
        final Adapter adapter = pullView.getAdapter();
        if (null != adapter && !adapter.isEmpty() && pullView.getFirstVisiblePosition() <= 1) {
            final View firstVisibleChild = pullView.getChildAt(0);
            if (firstVisibleChild != null) {
                result = firstVisibleChild.getTop() >= pullView.getTop();
            }
        }
        return result;
    }

    @Override
    protected boolean isCanPullFooter(AbsListView pullView) {
        boolean result = false;
        final Adapter adapter = pullView.getAdapter();
        if (null != adapter && !adapter.isEmpty()) {
            final int lastItemPosition = pullView.getCount() - 1;
            final int lastVisiblePosition = pullView.getLastVisiblePosition();
//            Log.d(PullViewBase.class.getSimpleName(), "isLastItemVisible. Last Item Position: " + lastItemPosition + " Last Visible Pos: " + lastVisiblePosition);

            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - pullView.getFirstVisiblePosition();
                final View lastVisibleChild = pullView.getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    result = lastVisibleChild.getBottom() <= pullView.getBottom();
                }
            }
        }

        return result;
    }

    @Override
    protected void scrollPullViewToHeader(AbsListView pullView) {
        pullView.setSelection(0);
    }

    @Override
    protected void scrollPullViewToFooter(AbsListView pullView) {
        if(!pullView.getAdapter().isEmpty()){
            pullView.setSelection(pullView.getCount() - 1);
        }
    }
}
