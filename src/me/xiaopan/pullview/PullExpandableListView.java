package me.xiaopan.pullview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 可拉的ExpandableListView
 */
public class PullExpandableListView extends PullAdapterViewBase<ExpandableListView> {
    public PullExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullExpandableListView(Context context) {
        super(context);
    }

    @Override
    protected ExpandableListView createPullView() {
        ExpandableListView listView = new ExpandableListView(getContext());
        listView.setId(android.R.id.list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setBackgroundColor(Color.WHITE);
        return listView;
    }
}
