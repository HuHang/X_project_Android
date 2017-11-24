package com.iftech.car.trace.model.iface;

import com.iftech.car.trace.model.listener.IBaseListener;

/**
 * created by tanghuosong 2017/5/5
 * description:
 **/
public interface IBaseModel {

    void queryPost(String url, String username, String password,int type, IBaseListener baseListener);

    void queryGet(String url, String username, String password, int type, IBaseListener baseListener);

}
