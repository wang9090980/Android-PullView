package me.xiaopan.pullview;

import android.view.View;

/**
 * Created by xiaopan on 2014/3/26 0026.
 */
public interface PullViewInterface<T extends View> {
    /**
     * 创建内容视图
     * @return 内容视图
     */
    public T createPullView();

    /**
     * 是否是垂直拉伸
     * @return true：垂直拉伸；false：横向拉伸
     */
    public boolean isVerticalPull();

    /**
     * 是否可以拉头部
     * @param pullView 拉伸视图
     * @return 是否可以拉头部
     */
    public boolean isCanPullHeader(T pullView);

    /**
     * 是否可以拉尾部
     * @param pullView 拉伸视图
     * @return 是否可以拉尾部
     */
    public boolean isCanPullFooter(T pullView);

    /**
     * 滚动拉伸视图到头部
     * @param pullView 拉伸视图
     */
    public void scrollPullViewToHeader(T pullView);

    /**
     * 滚动拉伸视图到尾部
     * @param pullView 拉伸视图
     */
    public void scrollPullViewToFooter(T pullView);
}
