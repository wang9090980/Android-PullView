package me.xiaopan.pullview.widget;

import me.xiaopan.pullview.PullAdapterViewBase;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 可拉的GridView
 */
public class PullGridView extends PullAdapterViewBase<GridView> {
    public PullGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullGridView(Context context) {
        super(context);
    }

    @Override
    protected GridView createPullView() {
        GridView gridView = new GridView(getContext());
        gridView.setCacheColorHint(Color.TRANSPARENT);
        gridView.setBackgroundColor(Color.WHITE);
        return gridView;
    }
}