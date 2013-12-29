package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.util.WebViewManager;
import me.xiaopan.pullview.widget.PullWebView;
import android.app.Activity;
import android.os.Bundle;

public class PullWebActivity extends Activity {
	private WebViewManager webViewManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		PullWebView pullWebView = (PullWebView) findViewById(R.id.pullWeb_web);
		webViewManager = new WebViewManager(pullWebView.getPullView());
		webViewManager.getWebView().loadUrl("http://www.baidu.com/");
	}

	@Override
	public void onBackPressed() {
		if(!webViewManager.goBack()){
			super.onBackPressed();
		}
	}
}
