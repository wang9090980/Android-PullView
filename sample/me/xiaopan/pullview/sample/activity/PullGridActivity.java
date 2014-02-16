package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.ImageAdapter;
import me.xiaopan.pullview.sample.widget.PulldownToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullupToRefreshFooter;
import me.xiaopan.pullview.widget.PullGridView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
        
        pullGridView.setPullHeaderView(new PulldownToRefreshHeader(getBaseContext(), new PulldownToRefreshHeader.OnRefreshListener() {
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
        
        pullGridView.setPullFooterView(new PullupToRefreshFooter(getBaseContext(), new PullupToRefreshFooter.OnRefreshListener() {
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
        
        pullGridView.triggerHeader();
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
                if(!pullGridView.triggerHeader()){
                	Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_refresh_pullup :
            	 if(!pullGridView.triggerFooter()){
            		 Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
            	 }
            	break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
