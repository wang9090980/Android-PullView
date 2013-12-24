package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by XIAOPAN on 13-12-21.
 */
public abstract class PullHeader extends LinearLayout{
    private Status status = Status.NORMAL;
    private OnStatusChangeListener onStatusChangeListener;
    
	public PullHeader(Context context) {
        super(context);
    }

    public PullHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onScroll(int distance){
    	switch(status){
    		case NORMAL:
    			if(distance >= getHeight()){
    				status = Status.READY;
    				onStateChange(status);
    			}
    			break;
    		case READY :
    			if(distance < getHeight()){
					status = Status.NORMAL;
    				onStateChange(status);
    			}
    			break;
    		case REFRESHING : 
    			break;
    	}
    }
    
    /**
     * 触发
     */
    public void trigger(){
    	status = Status.REFRESHING;
		onStatusChangeListener.onShow();
		onStateChange(status);
    }
    
    protected abstract void onStateChange(Status newStatus);
    
    /**
     * 状态
     */
    public enum Status{
    	NORMAL, READY, REFRESHING,
    }
    
    public interface OnStatusChangeListener{
    	public void onShow();
    }

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public OnStatusChangeListener getOnStatusChangeListener() {
		return onStatusChangeListener;
	}

	public void setOnStatusChangeListener(
			OnStatusChangeListener onStatusChangeListener) {
		this.onStatusChangeListener = onStatusChangeListener;
	}
}