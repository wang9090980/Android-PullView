package me.xiaopan.pullview.activity;

import me.xiaopan.pullview.PullWebView;
import me.xiaopan.pullview.activity.util.WebViewManager;
import me.xiaopan.pullview.example.R;
import android.app.Activity;
import android.os.Bundle;

public class PullWebViewActivity extends Activity {
	private WebViewManager webViewManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		PullWebView pullWebView = (PullWebView) findViewById(R.id.pullWeb_web);
		webViewManager = new WebViewManager(pullWebView.getPullView());
		webViewManager.getWebView().loadUrl("http://baidu.com");
	}

	@Override
	public void onBackPressed() {
		if(!webViewManager.goBack()){
			super.onBackPressed();
		}
	}
}