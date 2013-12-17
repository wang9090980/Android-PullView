package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

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
    public boolean isInTheHeader(AbsListView pullView) {
        return isFirstItemVisible(pullView);
    }

    @Override
    public boolean isInTheFooter(AbsListView pullView) {
        return isLastItemVisible(pullView);
    }

    @Override
    protected PullOrientation getPullOrientation() {
        return PullOrientation.VERTICAL;
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

    private boolean isFirstItemVisible(AdapterView pullView) {
        final Adapter adapter = pullView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
//            Log.d(PullViewBase.class.getSimpleName(), "isFirstItemVisible. Empty View.");
            return true;
        } else {
            /**
             * This check should really just be:
             * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
             * internally use a HeaderView which messes the positions up. For
             * now we'll just add one to account for it and rely on the inner
             * condition which checks getTop().
             */
            if (pullView.getFirstVisiblePosition() <= 1) {
                final View firstVisibleChild = pullView.getChildAt(0);
                if (firstVisibleChild != null) {
                    return firstVisibleChild.getTop() >= pullView.getTop();
                }
            }
        }

        return false;
    }

    private boolean isLastItemVisible(AdapterView pullView) {
        final Adapter adapter = pullView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
//            Log.d(PullViewBase.class.getSimpleName(), "isLastItemVisible. Empty View.");
            return true;
        } else {
            final int lastItemPosition = pullView.getCount() - 1;
            final int lastVisiblePosition = pullView.getLastVisiblePosition();
//            Log.d(PullViewBase.class.getSimpleName(), "isLastItemVisible. Last Item Position: " + lastItemPosition + " Last Visible Pos: " + lastVisiblePosition);

            /**
             * This check should really just be: lastVisiblePosition ==
             * lastItemPosition, but PtRListView internally uses a FooterView
             * which messes the positions up. For me we'll just subtract one to
             * account for it and rely on the inner condition which checks
             * getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - pullView.getFirstVisiblePosition();
                final View lastVisibleChild = pullView.getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= pullView.getBottom();
                }
            }
        }

        return false;
    }
}
