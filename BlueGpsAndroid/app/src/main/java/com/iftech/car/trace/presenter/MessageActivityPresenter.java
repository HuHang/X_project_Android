package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.BaseModel;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.MessageActivityView;

/**
 * created by tanghuosong 2017/5/5
 * description:
 **/
public class MessageActivityPresenter implements IBasePresenter,IBaseListener{

    private MessageActivityView messageActivityView;
    private IBaseModel baseModel;

    public MessageActivityPresenter(MessageActivityView messageActivityView){
        this.baseModel  = new BaseModel();
        this.messageActivityView = messageActivityView;
    }

    @Override
    public void queryGet(String url, String username, String password,int type) {
        baseModel.queryGet(url,username,password,type,this);
    }

    @Override
    public void queryPost(String url, String username, String password, int type) {
        baseModel.queryPost(url,username,password,type,this);
    }

    @Override
    public void querySuccess(String msg, int type) {
        messageActivityView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        messageActivityView.queryFail(msg);
    }
}
