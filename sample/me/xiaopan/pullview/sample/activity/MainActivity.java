package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.easy.android.util.ToastUtils;
import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.TextAdapter;
import me.xiaopan.pullview.sample.domain.ActivityEntry;
import me.xiaopan.pullview.sample.widget.PullToRefreshFooter;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullListView;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
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
        entrys.add(new ActivityEntry.Build("Button", null).create());
        entrys.add(new ActivityEntry.Build("CalendarView", null).create());
        entrys.add(new ActivityEntry.Build("CheckBox", null).create());
        entrys.add(new ActivityEntry.Build("EditText", null).create());
        entrys.add(new ActivityEntry.Build("Gallery", null).create());
        entrys.add(new ActivityEntry.Build("ImageView", null).create());
        entrys.add(new ActivityEntry.Build("ProgressBar", null).create());
        entrys.add(new ActivityEntry.Build("RatingBar", null).create());
        entrys.add(new ActivityEntry.Build("RadioButton", null).create());
        entrys.add(new ActivityEntry.Build("Spinner", null).create());
        entrys.add(new ActivityEntry.Build("SurfaceView", null).create());
        entrys.add(new ActivityEntry.Build("SeekBar", null).create());
        entrys.add(new ActivityEntry.Build("TabHost", null).create());
        entrys.add(new ActivityEntry.Build("TextView", null).create());
        entrys.add(new ActivityEntry.Build("TimePicker", null).create());
        entrys.add(new ActivityEntry.Build("ToggleButton", null).create());

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
        pullListView.setPullHeaderView(pullToRefreshHeader);
        pullListView.triggerHeader();
        
        final PullToRefreshFooter pullToRefreshFooter = new PullToRefreshFooter(getBaseContext());
        pullToRefreshFooter.setOnRefreshListener(new PullToRefreshFooter.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshFooter.complete();
                    }
                }, 5000);
            }
        });
        pullListView.setPullFooterView(pullToRefreshFooter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_refresh :
                pullListView.triggerHeader();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
