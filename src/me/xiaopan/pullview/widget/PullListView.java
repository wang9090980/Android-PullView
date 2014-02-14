package me.xiaopan.pullview.widget;

import me.xiaopan.pullview.PullAdapterViewBase;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可拉的ListView
 */
public class PullListView extends PullAdapterViewBase<ListView> {
    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullListView(Context context) {
        super(context);
    }

    @Override
    protected ListView createPullView() {
        ListView listView = new ListView(getContext());
        listView.setId(android.R.id.list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setBackgroundColor(Color.WHITE);
        return listView;
    }
}