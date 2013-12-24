package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by XIAOPAN on 13-12-21.
 */
public abstract class PullHeader extends LinearLayout{
    private Status status = Status.NORMAL;
    
	public PullHeader(Context context) {
        super(context);
    }

    public PullHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onScroll(int distance, boolean isLetGo){
    	switch(status){
    		case NORMAL:
    			if(distance >= getHeight()){
    				status = Status.READY;
    				onStateChange(status);
    			}
    			break;
    		case READY :
    			if(distance <= getHeight()){
    				if(isLetGo){
    					status = Status.REFRESHING;
    				}else{
    					status = Status.NORMAL;
    				}
    				onStateChange(status);
    			}
    			break;
			default:
				break;
    	}
    }
    
    protected abstract void onStateChange(Status newStatus);
    
    /**
     * 状态
     */
    public enum Status{
    	NORMAL, READY, REFRESHING,
    }
}