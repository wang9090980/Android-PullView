package me.xiaopan.pullview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import me.xiaopan.pullview.PullHeader;
import me.xiaopan.pullview.example.R;

/**
 * Created by XIAOPAN on 13-12-21.
 */
public class MyPullHeader extends PullHeader{
    private TextView hintTextView;
    private ImageView arrowImageView;

    public MyPullHeader(Context context) {
        super(context);
        init();
    }

    public MyPullHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.header_pull, this);
        hintTextView = (TextView) findViewById(R.id.text_headerPull_hint);
        arrowImageView = (ImageView) findViewById(R.id.image_headerPull_arrow);
    }

    @Override
    public void onScroll() {
        super.onScroll();
    }
}
