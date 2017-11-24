package com.iftech.car.trace.ui.view;

/**
 * created by tanghuosong 2017/5/1
 * description:
 **/
public interface LoginView {

    void lostPass(String msg);

    void lostUsername(String msg);

    void loginSuccess(String msg);

    void loginFail(String msg);
}
