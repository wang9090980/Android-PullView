package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.TextAdapter;
import me.xiaopan.pullview.sample.domain.ActivityEntry;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullListView;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by xiaopan on 13-12-17.
 */
public class PullListActivity extends ListActivity{
	private PullListView pullListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        pullListView = (PullListView) findViewById(R.id.pullList_list);

        List<TextAdapter.Text> entrys = new ArrayList<TextAdapter.Text>();
        for(int w = 0; w < 25; w++){
            entrys.add(new ActivityEntry.Build("第"+(w+1)+"天", null).create());
        }
        getListView().setAdapter(new TextAdapter(getBaseContext(), entrys));
        
        final PullToRefreshHeader pullToRefreshHeader = new PullToRefreshHeader(getBaseContext());
        pullToRefreshHeader.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onNormal() {
            }

            @Override
            public void onReady() {
            }

            @Override
            public void onRefresh() {
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
    }
}
