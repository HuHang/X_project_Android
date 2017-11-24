package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.BaseModel;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.CarInfoView;
import com.iftech.car.trace.ui.view.ShopView;

/**
 * created by tanghuosong 2017/5/4
 * description:
 **/
public class ShopPresenter implements IBasePresenter, IBaseListener {

    private ShopView shopView;
    IBaseModel baseModel;

    public ShopPresenter(ShopView shopView){
        this.shopView = shopView;
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
    public void querySuccess(String msg, int type) {
        shopView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        shopView.queryFail(msg);
    }
}
