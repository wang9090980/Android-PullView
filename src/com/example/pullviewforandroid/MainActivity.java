package com.example.pullviewforandroid;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	private WebViewManager webViewManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		PullWebView pullWebView = (PullWebView) findViewById(R.id.pullWeb_web);
		webViewManager = new WebViewManager(pullWebView.getRefreshableView());
		webViewManager.getWebView().loadUrl("http://baidu.com");
	}

	@Override
	public void onBackPressed() {
		if(!webViewManager.goBack()){
			super.onBackPressed();
		}
	}
}
