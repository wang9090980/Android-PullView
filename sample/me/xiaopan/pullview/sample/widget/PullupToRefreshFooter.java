package me.xiaopan.pullview.sample.widget;

import me.xiaopan.pullview.PullFooterView;
import me.xiaopan.pullview.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 上拉刷新尾
 */
public class PullupToRefreshFooter extends PullFooterView{
	private int maxDegress = 180;
	private float px, py;
	private Matrix matrix;
    private TextView hintTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private OnRefreshListener onRefreshListener;
    
    public PullupToRefreshFooter(Context context, OnRefreshListener onRefreshListener) {
    	super(context);
    	this.onRefreshListener = onRefreshListener;
    	init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.pull_header, this);
        hintTextView = (TextView) findViewById(R.id.text_pullHeader_hint);
        arrowImageView = (ImageView) findViewById(R.id.image_pullHeader_arrow);
        arrowImageView.setScaleType(ScaleType.MATRIX);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_pullHeader_progress);
        resetPxPy();
        matrix = new Matrix();
        setStatus(Status.NORMAL);
    }

    @Override
    public void onScroll(int distance) {
    	super.onScroll(distance);
    	//当滚动的时候旋转箭头
        int height = getHeight();
    	matrix.setRotate((distance < height?((float) distance/height) * maxDegress:maxDegress) + 180, px, py);
    	arrowImageView.setImageMatrix(matrix);
    }
    
    private void resetPxPy(){
    	 if(arrowImageView.getDrawable() != null){
         	int width = arrowImageView.getDrawable().getIntrinsicWidth();
         	int height = arrowImageView.getDrawable().getIntrinsicHeight();
         	int paddingLeft = arrowImageView.getPaddingLeft();
         	int paddingTop = arrowImageView.getPaddingTop();
         	int paddingRight = arrowImageView.getPaddingRight();
         	int paddingBottom = arrowImageView.getPaddingBottom();
    		if(width > height){
    			int offset = (width - height)/2;
    			if(paddingTop<offset){
    				paddingTop = offset;
    			}
    			if(paddingBottom<offset){
    				paddingBottom = offset;
    			}
    		}else if(width < height){
    			int offset = (height - width)/2;
    			if(paddingLeft<offset){
    				paddingLeft = offset;
    			}
    			if(paddingRight<offset){
    				paddingRight = offset;
    			}
    		}
    		arrowImageView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
         	px = width/2f;
         	py = height/2f;
         }
    }

	public void setMaxDegress(int maxDegress) {
		this.maxDegress = maxDegress;
		resetPxPy();
	}
	
	public void setIcon(Drawable drawable){
		arrowImageView.setImageDrawable(drawable);
		resetPxPy();
	}
	
	public void setIconResource(int resId){
		arrowImageView.setImageResource(resId);
		resetPxPy();
	}
	
	public void setIconBitmap(Bitmap bm){
		arrowImageView.setImageBitmap(bm);
		resetPxPy();
	}
	
	public void setIconURI(Uri uri){
		arrowImageView.setImageURI(uri);
		resetPxPy();
	}

	@Override
	protected void onStatusChange(Status newStatus) {
		super.onStatusChange(newStatus);
		switch(newStatus){
			case NORMAL:
				hintTextView.setText("继续上拉刷新");
				arrowImageView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.INVISIBLE);
				break;
			case READY :
				hintTextView.setText("现在松开即可刷新");
				break;
			case TRIGGERING :
				hintTextView.setText("正在刷新…");
				arrowImageView.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				if(onRefreshListener != null){
					onRefreshListener.onRefresh(this);
				}
				break;
			case TRIGGER_TO_NORMAL : 
				break;
		}
	}
	
	public interface OnRefreshListener{
		public void onRefresh(PullupToRefreshFooter pullupToRefreshFooter);
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}
}
