package com.iftech.car.trace.presenter;

import com.iftech.car.trace.model.BaseModel;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.MessageFragmentView;

/**
 * created by tanghuosong 2017/5/1
 * description:
 **/
public class MessageFragmentPresenter implements IBasePresenter,IBaseListener {

    private MessageFragmentView messageFragmentView;
    private IBaseModel baseModel;

    public MessageFragmentPresenter(MessageFragmentView messageFragmentView){
        this.baseModel  = new BaseModel();
        this.messageFragmentView = messageFragmentView;
    }

    @Override
    public void queryGet(String url,String username,String password,int type) {
        baseModel.queryGet(url,username,password,type,this);
    }


    @Override
    public void queryPost(String url, String username, String password,int type) {
        baseModel.queryPost(url,username,password,type,this);
    }

    @Override
    public void querySuccess(String msg,int type) {
        messageFragmentView.querySuccess(msg,type);
    }

    @Override
    public void queryFail(String msg) {
        messageFragmentView.queryFail(msg);
    }
}
