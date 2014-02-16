package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.TextAdapter;
import me.xiaopan.pullview.sample.domain.ActivityEntry;
import me.xiaopan.pullview.sample.widget.PulldownToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullupToRefreshFooter;
import me.xiaopan.pullview.widget.PullListView;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
        
        pullListView.setPullHeaderView(new PulldownToRefreshHeader(getBaseContext(), new PulldownToRefreshHeader.OnRefreshListener() {
            @Override
            public void onRefresh(final PulldownToRefreshHeader pulldownToRefreshHeader) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    	pulldownToRefreshHeader.complete();
                    }
                }, 5000);
            }
        }));
        
        pullListView.setPullFooterView(new PullupToRefreshFooter(getBaseContext(), new PullupToRefreshFooter.OnRefreshListener() {
            @Override
            public void onRefresh(final PullupToRefreshFooter pullupToRefreshFooter) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullupToRefreshFooter.complete();
                    }
                }, 5000);
            }
        }));
        
        pullListView.triggerHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refersh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh_pulldown :
                if(!pullListView.triggerHeader()){
                	Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_refresh_pullup :
            	 if(!pullListView.triggerFooter()){
            		 Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
            	 }
            	break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
