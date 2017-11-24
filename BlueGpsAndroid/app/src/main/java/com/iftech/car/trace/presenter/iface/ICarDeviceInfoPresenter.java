package com.iftech.car.trace.presenter.iface;

import org.json.JSONObject;

/**
 * created by tanghuosong 2017/5/18
 * description:
 **/
public interface ICarDeviceInfoPresenter extends IBasePresenter{

    void queryJsonPost(String url, String username, String password, JSONObject jsonObject, int type);

}
