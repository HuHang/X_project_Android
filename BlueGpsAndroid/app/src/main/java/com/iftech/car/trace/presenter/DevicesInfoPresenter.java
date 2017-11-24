package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.CarDeviceInfoModel;
import com.iftech.car.trace.model.iface.ICarDeviceInfoModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.ICarDeviceInfoPresenter;
import com.iftech.car.trace.ui.view.DeviceInfoView;
import org.json.JSONObject;

/**
 * created by tanghuosong 2017/5/4
 * description:
 **/
public class DevicesInfoPresenter implements ICarDeviceInfoPresenter, IBaseListener {

    private DeviceInfoView deviceInfoView;
    ICarDeviceInfoModel carDeviceInfoModel;

    public DevicesInfoPresenter(DeviceInfoView deviceInfoView){
        this.deviceInfoView = deviceInfoView;
        carDeviceInfoModel = new CarDeviceInfoModel();
    }

    @Override
    public void queryPost(String url, String username, String password, int type) {

    }

    @Override
    public void queryJsonPost(String url, String username, String password, JSONObject jsonObject, int type) {
        carDeviceInfoModel.queryJsonPost(url,username,password,jsonObject,type,this);
    }

    @Override
    public void queryGet(String url, String username, String password, int type) {
//        carDeviceInfoModel.queryGet(url,username,password,type,this);
    }

    @Override
    public void querySuccess(String msg, int type) {
        deviceInfoView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        deviceInfoView.queryFail(msg);
    }
}
