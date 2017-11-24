package com.iftech.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * created by tanghuosong 2017/5/10
 * description:
 **/
public class RefreshHeaderView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onRefresh() {
        setText("正在刷新中...");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                setText("松手刷新");
            } else {
                setText("下拉刷新");
            }
        } else {
            setText("正在刷新中...");
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        setText("刷新完成");
    }

    @Override
    public void onReset() {
        setText("");
    }
}