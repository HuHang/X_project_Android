package com.iftech.car.trace.ui.view;

/**
 * created by tanghuosong 2017/5/18
 * description: 所有view的基类
 **/
public interface BaseView {

    void querySuccess(String msg,int type);

    void queryFail(String msg);
}
