package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.BaseModel;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.ChartView;

/**
 * created by tanghuosong 2017/5/5
 * description:
 **/
public class ChartPresenter implements IBasePresenter,IBaseListener{

    private ChartView chartView;
    private IBaseModel baseModel;

    public ChartPresenter(ChartView chartView){
        this.baseModel  = new BaseModel();
        this.chartView = chartView;
    }

    @Override
    public void queryPost(String url, String username, String password, int type) {

    }

    @Override
    public void queryGet(String url, String username, String password, int type) {
        baseModel.queryGet(url,username,password,type,this);
    }

    @Override
    public void querySuccess(String msg,int type) {
        chartView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        chartView.queryFail(msg);
    }
}
