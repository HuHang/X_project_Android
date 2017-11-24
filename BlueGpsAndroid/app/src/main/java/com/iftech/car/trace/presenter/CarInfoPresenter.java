package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.CarDeviceInfoModel;
import com.iftech.car.trace.model.iface.ICarDeviceInfoModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.ICarDeviceInfoPresenter;
import com.iftech.car.trace.ui.view.CarInfoView;
import org.json.JSONObject;
//import net.sf.json.JSONObject;

/**
 * created by tanghuosong 2017/5/4
 * description:
 **/
public class CarInfoPresenter implements ICarDeviceInfoPresenter, IBaseListener {

    private CarInfoView carInfoView;
    ICarDeviceInfoModel carDeviceInfoModel;

    public CarInfoPresenter(CarInfoView carInfoView){
        this.carInfoView = carInfoView;
        carDeviceInfoModel = new CarDeviceInfoModel();
    }

    @Override
    public void queryPost(String url, String username, String password, int type) {
        carDeviceInfoModel.queryPost(url,username,password,type,this);
    }

    @Override
    public void queryJsonPost(String url, String username, String password, JSONObject jsonObject, int type) {
        carDeviceInfoModel.queryJsonPost(url,username,password,jsonObject,type,this);
    }

    @Override
    public void queryGet(String url, String username, String password, int type) {
        carDeviceInfoModel.queryGet(url,username,password,type,this);
    }

    @Override
    public void querySuccess(String msg, int type) {
        carInfoView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        carInfoView.queryFail(msg);
    }
}
