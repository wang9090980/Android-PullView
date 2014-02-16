package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.ViewPagerAdapter;
import me.xiaopan.pullview.sample.widget.PulldownToRefreshHorizontalHeader;
import me.xiaopan.pullview.sample.widget.PullupToRefreshHorizontalFooter;
import me.xiaopan.pullview.widget.PullViewPager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PullViewPagerActivity extends Activity {
	private PullViewPager pullViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		pullViewPager = (PullViewPager) findViewById(R.id.pullViewPager_viewPager);
		
		List<View> viewList = new ArrayList<View>();
		viewList.add(create(R.drawable.image_1));
		viewList.add(create(R.drawable.image_2));
		viewList.add(create(R.drawable.image_3));
		viewList.add(create(R.drawable.image_4));
		viewList.add(create(R.drawable.image_5));
		viewList.add(create(R.drawable.image_6));
		pullViewPager.getPullView().setAdapter(new ViewPagerAdapter(viewList));
		
        pullViewPager.setPullHeaderView(new PulldownToRefreshHorizontalHeader(getBaseContext(), new PulldownToRefreshHorizontalHeader.OnRefreshListener() {
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
        
        pullViewPager.setPullFooterView(new PullupToRefreshHorizontalFooter(getBaseContext(), new PullupToRefreshHorizontalFooter.OnRefreshListener() {
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

        pullViewPager.triggerHeader();
	}
	
	private ImageView create(int resId){
		ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.image, null);
		imageView.setImageResource(resId);
		return imageView;
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
                if(!pullViewPager.triggerHeader()){
                	Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_refresh_pullup :
            	 if(!pullViewPager.triggerFooter()){
            		 Toast.makeText(getBaseContext(), "对不起，当前正忙", Toast.LENGTH_SHORT).show();
            	 }
            	break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
