package com.iftech.car.trace.model.iface;


import com.iftech.car.trace.model.listener.IBaseListener;
import org.json.JSONObject;

/**
 * created by tanghuosong 2017/5/5
 * description:
 **/
public interface ICarDeviceInfoModel extends IBaseModel{

    void queryJsonPost(String url , String username, String password, JSONObject jsonObject, int type, IBaseListener listener);
}
