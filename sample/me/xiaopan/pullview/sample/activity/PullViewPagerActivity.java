package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.ViewPagerAdapter;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader;
import me.xiaopan.pullview.sample.widget.PullToRefreshHeader.OnRefreshListener;
import me.xiaopan.pullview.widget.PullViewPager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class PullViewPagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		
		PullViewPager pullViewPager = (PullViewPager) findViewById(R.id.pullViewPager_viewPager);
		
		List<View> viewList = new ArrayList<View>();
		viewList.add(create(R.drawable.image_1));
		viewList.add(create(R.drawable.image_2));
		viewList.add(create(R.drawable.image_3));
		viewList.add(create(R.drawable.image_4));
		viewList.add(create(R.drawable.image_5));
		viewList.add(create(R.drawable.image_6));
		pullViewPager.getPullView().setAdapter(new ViewPagerAdapter(viewList));
		
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
        pullViewPager.setPullHeaderView(pullToRefreshHeader);
        pullViewPager.triggerHeader();
	}
	
	private ImageView create(int resId){
		ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.image, null);
		imageView.setImageResource(resId);
		return imageView;
	}
}
