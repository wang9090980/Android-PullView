package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.PullViewPager;
import me.xiaopan.pullview.sample.adapter.ViewPagerAdapter;
import me.xiaopan.pullview.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PullViewPagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		
		PullViewPager pullViewPager = (PullViewPager) findViewById(R.id.pullViewPager_viewPager);
		
		List<View> viewList = new ArrayList<View>();
		viewList.add(create(R.drawable.images_1));
		viewList.add(create(R.drawable.images_2));
		viewList.add(create(R.drawable.images_3));
		viewList.add(create(R.drawable.images_4));
		viewList.add(create(R.drawable.images_5));
		viewList.add(create(R.drawable.images_6));
		pullViewPager.getPullView().setAdapter(new ViewPagerAdapter(viewList));
	}
	
	private ImageView create(int resId){
		ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.image, null);
		imageView.setImageResource(resId);
		return imageView;
	}
}
