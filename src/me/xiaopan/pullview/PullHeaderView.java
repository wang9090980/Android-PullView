package me.xiaopan.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 拉伸头视图
 */
public abstract class PullHeaderView extends LinearLayout{
    private Status status = Status.NORMAL;
    private ControllCallback controllCallback;
    
	public PullHeaderView(Context context) {
        super(context);
    }

    public PullHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onScroll(int distance){
    	switch(status){
    		case NORMAL:
    			if(distance >= getHeight()){
    				status = Status.READY;
    				controllCallback.onShow();
    				onStatusChange(status);
    			}
    			break;
    		case READY :
    			if(distance < getHeight()){
					status = Status.NORMAL;
					controllCallback.onHide();
    				onStatusChange(status);
    			}
    			break;
    		case TRIGGERING :
    			break;
    		case TRIGGER_TO_NORMAL : 
    			break;
    	}
    }
    
    /**
     * 当触发
     */
    void onTrigger(){
    	status = Status.TRIGGERING;
		onStatusChange(status);
    }
    
    /**
     * 当完成
     */
    void onComplete(){
    	status = Status.NORMAL;
		onStatusChange(status);
    }
    
    /**
     * 完成
     */
    public void complete(){
    	status = Status.TRIGGER_TO_NORMAL;
		controllCallback.onHide();
		controllCallback.onRollback();
    	onStatusChange(status);
    }
    
    protected abstract void onStatusChange(Status newStatus);

	/**
	 * 获取状态
	 * @return
	 */
	public Status getStatus() {
		return status;
	}

    /**
     * 设置状态
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * 触发中
     * @return
     */
    public boolean isTriggering(){
        return status == Status.TRIGGERING;
    }

    void setControllCallback(ControllCallback controllCallback) {
		this.controllCallback = controllCallback;
	}
    
    /**
     * 控制回调
     */
    public interface ControllCallback{
    	/**
    	 * 显示
    	 */
    	public void onShow();
    	
    	/**
    	 * 隐藏
    	 */
    	public void onHide();
    	
    	/**
    	 * 回滚
    	 */
    	public void onRollback();
    }
    
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
    	 * 触发中
    	 */
    	TRIGGERING,
    	
    	/**
    	 * 触发到正常
    	 */
    	TRIGGER_TO_NORMAL,
    }
}