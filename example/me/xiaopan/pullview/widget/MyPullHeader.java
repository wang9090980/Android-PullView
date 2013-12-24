package me.xiaopan.pullview.widget;

import me.xiaopan.easy.android.util.ToastUtils;
import me.xiaopan.pullview.PullHeader;
import me.xiaopan.pullview.example.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * Created by XIAOPAN on 13-12-21.
 */
public class MyPullHeader extends PullHeader{
	private int maxDegress = 180;
    private TextView hintTextView;
    private ImageView arrowImageView;
    private Matrix matrix;
    private float px, py;

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
        arrowImageView.setScaleType(ScaleType.MATRIX);
        resetPxPy();
        matrix = new Matrix();
    }

    @Override
    public void onScroll(int distance, boolean isLetGo) {
    	super.onScroll(distance, isLetGo);
    	//旋转箭头
        int height = getHeight();
    	matrix.setRotate(distance < height?((float) distance/height) * maxDegress:maxDegress, px, py);
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
	protected void onStateChange(Status newStatus) {
		switch(newStatus){
			case NORMAL:
				hintTextView.setText("下拉刷新");
				break;
			case READY :
				hintTextView.setText("松开刷新");
				break;
			case REFRESHING :
				ToastUtils.toastS(getContext(), "刷新");
				break;
		}
	}
}
