package me.xiaopan.pullview.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.easy.android.util.ToastUtils;
import me.xiaopan.pullview.PullListView;
import me.xiaopan.pullview.adapter.TextAdapter;
import me.xiaopan.pullview.domain.ActivityEntry;
import me.xiaopan.pullview.example.R;
import me.xiaopan.pullview.widget.PullToRefreshHeader;
import me.xiaopan.pullview.widget.PullToRefreshHeader.OnRefreshListener;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

public class MainActivity extends ListActivity{
    private TextAdapter textAdapter;
    private PullListView pullListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        pullListView = (PullListView) findViewById(R.id.pullList_list);

        List<TextAdapter.Text> entrys = new ArrayList<TextAdapter.Text>();

        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullList), PullListActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullGrid), PullGridActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullExpandableList), PullExpandableListActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullScroll), PullScrollActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullHorizontalScroll), PullHorizontalScrollActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullWeb), PullWebActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullViewPager), PullViewPagerActivity.class).create());

        getListView().setAdapter(textAdapter = new TextAdapter(getBaseContext(), entrys));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ActivityEntry) textAdapter.getTexts().get(position - getListView().getHeaderViewsCount())).clickHandle(MainActivity.this);
            }
        });
        final PullToRefreshHeader pullToRefreshHeader = new PullToRefreshHeader(getBaseContext());
        pullToRefreshHeader.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onNormal() {
				ToastUtils.toastS(getBaseContext(), "恢复");
			}
			
			@Override
			public void onReady() {
				ToastUtils.toastS(getBaseContext(), "准备");
			}
			
			@Override
			public void onRefresh() {
				ToastUtils.toastS(getBaseContext(), "刷新");
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						pullToRefreshHeader.complete();
					}
				}, 5000);
			}
		});
        pullListView.setPullHeader(pullToRefreshHeader);
    }
}
