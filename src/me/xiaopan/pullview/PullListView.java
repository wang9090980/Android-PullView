package me.xiaopan.pullview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

import me.xiaopan.pullview.example.R;

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
    public ListView createPullView() {
        ListView listView = new ListView(getContext());
        listView.setId(android.R.id.list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setBackgroundColor(Color.WHITE);
        return listView;
    }
}
