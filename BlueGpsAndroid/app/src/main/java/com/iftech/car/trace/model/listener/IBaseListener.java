package com.iftech.car.trace.model.listener;

/**
 * created by tanghuosong 2017/5/5
 * description:
 **/
public interface IBaseListener {

    void querySuccess(String msg ,int type);

    void queryFail(String msg);
}
