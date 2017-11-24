package com.iftech.car.trace.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.iftech.car.trace.bean.ResultBean;
import com.iftech.car.trace.model.BaseModel;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.LoginView;

/**
 * created by tanghuosong 2017/5/1
 * description:
 **/
public class LoginPresenter implements IBasePresenter, IBaseListener {
    private LoginView loginView ;
    private IBaseModel baseModel;
    public LoginPresenter(LoginView loginView){
        this.baseModel  = new BaseModel();
        this.loginView = loginView;
    }

    @Override
    public void queryGet(String url, String username, String password, int type) {

    }

    @Override
    public void queryPost(String url,String username, String password,int type) {

        if(TextUtils.isEmpty(username)){
            loginView.lostUsername("请输入用户名！");
            return;
        }else if(TextUtils.isEmpty(password)){
            loginView.lostPass("请输入密码");
            return;
        }
        baseModel.queryPost(url,null,null,type,this);
    }

    @Override
    public void querySuccess(String msg,int type) {
        ResultBean resultBean = new Gson().fromJson(msg,ResultBean.class);
        if(resultBean.isSuccess()){
            Log.d("登录成功",msg);
            loginView.loginSuccess(msg);
        }else{
            Log.e("登录失败",resultBean.getContent());
            loginView.loginFail("用户名或密码错误！");
        }
    }

    @Override
    public void queryFail(String msg) {
        Log.e("请求失败",msg);
        loginView.loginFail(msg);
    }
}
