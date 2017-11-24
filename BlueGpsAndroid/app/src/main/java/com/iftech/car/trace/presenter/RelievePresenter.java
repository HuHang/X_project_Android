package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.BaseModel;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.RelieveDeviceView;

/**
 * created by tanghuosong 2017/5/9
 * description:
 **/
public class RelievePresenter implements IBasePresenter ,IBaseListener{


    private RelieveDeviceView relieveDeviceView;
    IBaseModel baseModel;
    public RelievePresenter(RelieveDeviceView relieveDeviceView){

        this.relieveDeviceView = relieveDeviceView;
        baseModel = new BaseModel();
    }

    @Override
    public void queryPost(String url, String username, String password, int type) {
        baseModel.queryPost(url,username,password,type,this);
    }

    @Override
    public void queryGet(String url, String username, String password, int type) {
        baseModel.queryGet(url,username,password,type,this);
    }

    @Override
    public void querySuccess(String msg,int type) {
        relieveDeviceView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        relieveDeviceView.queryFail(msg);
    }
}
