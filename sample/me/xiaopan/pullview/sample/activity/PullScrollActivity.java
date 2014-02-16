package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullScrollView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class PullScrollActivity extends Activity {
	private PullScrollView pullScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll);
		pullScrollView = (PullScrollView) findViewById(R.id.pullScrollView);
		
		final PullToRefreshHeader pullToRefreshHeader = new PullToRefreshHeader(getBaseContext());
        pullToRefreshHeader.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onNormal() {}
            @Override
            public void onReady() {}
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
        pullScrollView.setPullHeaderView(pullToRefreshHeader);
        pullScrollView.triggerHeader();
	}
}
