package me.xiaopan.pullview;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * 拉伸头视图
 */
public abstract class PullHeaderView extends LinearLayout{
    private Status status = Status.NORMAL;
    private ControllCallback controllCallback;
    private int minScrollValue;	//最小滚动值
    private PullViewBase<?> pullViewBase;
    
	public PullHeaderView(Context context) {
        super(context);
    }
	
    void setPullViewBase(PullViewBase<?> pullViewBase) {
		this.pullViewBase = pullViewBase;
	}

	protected void onScroll(int distance){
    	switch(status){
    		case NORMAL:
    			if(distance >= (pullViewBase.isVerticalPull()?getHeight():getWidth())){
    				setStatus(Status.READY);
    			}
    			break;
    		case READY :
    			if(distance < (pullViewBase.isVerticalPull()?getHeight():getWidth())){
    				setStatus(Status.NORMAL);
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
		setStatus(Status.TRIGGERING);
    }
    
    /**
     * 当完成
     */
    void onComplete(){
		setStatus(Status.NORMAL);
    }
    
    /**
     * 完成
     */
    public void complete(){
    	setStatus(Status.TRIGGER_TO_NORMAL);
    }

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
        onStatusChange(status);
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
    
    public int getTriggerValue(){
    	return pullViewBase.isVerticalPull()?getHeight():getWidth();
    }
    
    /**
     *   获取最小滚动值
     * @return
     */
    public int getMinScrollValue() {
		return minScrollValue;
	}
    
    protected void onStatusChange(Status newStatus){
    	switch(newStatus){
			case NORMAL:
				minScrollValue = 0;
				break;
			case READY :
				minScrollValue = -(pullViewBase.isVerticalPull()?getHeight():getWidth());
				break;
			case TRIGGERING :
				break;
			case TRIGGER_TO_NORMAL : 
				minScrollValue = 0;
				controllCallback.onRollback();
				break;
		}
    }

	/**
     * 控制回调
     */
    public interface ControllCallback{
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