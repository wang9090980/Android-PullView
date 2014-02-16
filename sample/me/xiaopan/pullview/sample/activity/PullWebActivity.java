package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.util.WebViewManager;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullWebView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class PullWebActivity extends Activity {
	private WebViewManager webViewManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		PullWebView pullWebView = (PullWebView) findViewById(R.id.pullWeb_web);
		webViewManager = new WebViewManager(pullWebView.getPullView());
		webViewManager.getWebView().loadUrl("http://www.baidu.com/");
		
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
        pullWebView.setPullHeaderView(pullToRefreshHeader);
        pullWebView.triggerHeader();
	}

	@Override
	public void onBackPressed() {
		if(!webViewManager.goBack()){
			super.onBackPressed();
		}
	}
}
