package com.iftech.car.trace.presenter.iface;


/**
 * created by tanghuosong 2017/5/18
 * description:
 **/
public interface IBasePresenter {
    void queryPost(String url,String username,String password, int type);

    void queryGet(String url,String username,String password, int type);
}
