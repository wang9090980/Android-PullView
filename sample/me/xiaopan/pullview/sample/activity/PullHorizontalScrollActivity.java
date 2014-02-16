package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.widget.PulldownToRefreshHorizontalHeader;
import me.xiaopan.pullview.sample.widget.PullupToRefreshHorizontalFooter;
import me.xiaopan.pullview.widget.PullHorizontalScrollView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PullHorizontalScrollActivity extends Activity {
	private PullHorizontalScrollView pullHorizontalScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_horizontal_scroll);
		pullHorizontalScrollView = (PullHorizontalScrollView) findViewById(R.id.pullHorizontalScrollView);
		
		pullHorizontalScrollView.setPullHeaderView(new PulldownToRefreshHorizontalHeader(getBaseContext(), new PulldownToRefreshHorizontalHeader.OnRefreshListener() {
            @Override
            public void onRefresh(final PulldownToRefreshHorizontalHeader pulldownToRefreshHorizontalHeader) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pulldownToRefreshHorizontalHeader.complete();
                    }
                }, 5000);
            }
        }));
        
		pullHorizontalScrollView.setPullFooterView(new PullupToRefreshHorizontalFooter(getBaseContext(), new PullupToRefreshHorizontalFooter.OnRefreshListener() {
            @Override
            public void onRefresh(final PullupToRefreshHorizontalFooter pullupToRefreshHorizontalFooter) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullupToRefreshHorizontalFooter.complete();
                    }
                }, 5000);
            }
        }));
        
        pullHorizontalScrollView.triggerHeader();
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
                if(!pullHorizontalScrollView.triggerHeader()){
                	Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_refresh_pullup :
            	 if(!pullHorizontalScrollView.triggerFooter()){
            		 Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
            	 }
            	break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
