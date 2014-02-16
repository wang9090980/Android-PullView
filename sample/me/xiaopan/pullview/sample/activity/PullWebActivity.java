package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.util.WebViewManager;
import me.xiaopan.pullview.sample.widget.PulldownToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullupToRefreshFooter;
import me.xiaopan.pullview.widget.PullWebView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PullWebActivity extends Activity {
	private WebViewManager webViewManager;
	private PullWebView pullWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		pullWebView = (PullWebView) findViewById(R.id.pullWeb_web);
		webViewManager = new WebViewManager(pullWebView.getPullView());
		webViewManager.getWebView().loadUrl("http://www.baidu.com/");
		
		pullWebView.setPullHeaderView(new PulldownToRefreshHeader(getBaseContext(), new PulldownToRefreshHeader.OnRefreshListener() {
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
        
		pullWebView.setPullFooterView(new PullupToRefreshFooter(getBaseContext(), new PullupToRefreshFooter.OnRefreshListener() {
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

        pullWebView.triggerHeader();
	}

	@Override
	public void onBackPressed() {
		if(!webViewManager.goBack()){
			super.onBackPressed();
		}
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
                if(!pullWebView.triggerHeader()){
                	Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_refresh_pullup :
            	 if(!pullWebView.triggerFooter()){
            		 Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
            	 }
            	break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
