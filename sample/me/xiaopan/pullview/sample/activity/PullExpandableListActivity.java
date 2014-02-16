package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.GroupAadpter;
import me.xiaopan.pullview.sample.adapter.TextAdapter;
import me.xiaopan.pullview.sample.domain.ActivityEntry;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullExpandableListView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by xiaopan on 13-12-17.
 */
public class PullExpandableListActivity extends Activity {
	private PullExpandableListView pullExpandableListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list);
        pullExpandableListView = (PullExpandableListView) findViewById(android.R.id.list);

        List<TextAdapter.Text> entrys = new ArrayList<TextAdapter.Text>();
        for(int w = 0; w < 25; w++){
            entrys.add(new ActivityEntry.Build("第"+(w+1)+"天", null).create());
        }

        List<GroupAadpter.Group> groups = new ArrayList<GroupAadpter.Group>();

        for(int w = 0; w < 7; w++){
            String[] texts = new String[10];
            for(int w2 = 0; w2 < texts.length; w2++){
                texts[w2] = "第"+(w+1)+"组的第"+(w2+1)+"个条目";
            }
            groups.add(new GroupAadpter.Group("第"+(w+1)+"组", texts));
        }

        pullExpandableListView.getPullView().setAdapter(new GroupAadpter(getBaseContext(), groups));
        pullExpandableListView.getPullView().setGroupIndicator(null);
        
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
        
        pullExpandableListView.setPullHeaderView(pullToRefreshHeader);
        pullExpandableListView.triggerHeader();
    }
}
