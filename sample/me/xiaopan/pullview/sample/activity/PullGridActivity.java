package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.ImageAdapter;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullGridView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by xiaopan on 13-12-17.
 */
public class PullGridActivity extends Activity {

    private PullGridView pullGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        pullGridView = (PullGridView) findViewById(R.id.pullGrid_grid);

        pullGridView.getPullView().setAdapter(new ImageAdapter(getBaseContext(), getResources().getStringArray(R.array.urls_small)));
        pullGridView.getPullView().setNumColumns(2);
        
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
        pullGridView.setPullHeaderView(pullToRefreshHeader);
        pullGridView.triggerHeader();
    }
}
