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
    				onStatusChangeListener.onShow(this);
    				onStatusChange(status);
    			}
    			break;
    		case READY :
    			if(distance < getHeight()){
					status = Status.NORMAL;
					onStatusChangeListener.onHide(this);
    				onStatusChange(status);
    			}
    			break;
    		case TRIGGER : 
    			break;
    		case TRIGGER_TO_NORMAL : 
    			break;
    	}
    }
    
    /**
     * 当触发
     */
    public void onTrigger(){
    	status = Status.TRIGGER;
		onStatusChange(status);
    }
    
    /**
     * 当完成
     */
    public void onComplete(){
    	status = Status.NORMAL;
		onStatusChange(status);
    }
    
    /**
     * 完成
     */
    public void complete(){
    	status = Status.TRIGGER_TO_NORMAL;
		onStatusChangeListener.onHide(this);
		onStatusChangeListener.onRollback(this);
    	onStatusChange(status);
    }
    
    protected abstract void onStatusChange(Status newStatus);
    
    /**
     * 状态
     */
    public enum Status{
    	/**
    	 * 正常
    	 */
    	NORMAL, 
    	
    	/**
    	 * 准备触发
    	 */
    	READY, 
    	
    	/**
    	 * 触发
    	 */
    	TRIGGER, 
    	
    	/**
    	 * 触发到正常
    	 */
    	TRIGGER_TO_NORMAL,
    }
    
    public interface OnStatusChangeListener{
    	public void onShow(PullHeader pullHeader);
    	public void onHide(PullHeader pullHeader);
    	public void onRollback(PullHeader pullHeader);
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