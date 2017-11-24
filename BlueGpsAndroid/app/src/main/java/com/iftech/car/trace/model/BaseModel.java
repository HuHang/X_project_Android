package com.iftech.car.trace.model;

import android.util.Log;
import com.iftech.car.trace.model.iface.IBaseModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.utils.HttpConnect;

/**
 * created by tanghuosong 2017/5/5
 * description: 请求后台数据的基类
 **/
public class BaseModel implements IBaseModel{

    // post 请求
    @Override
    public void queryPost(final String url, String username, String password, final int type, final IBaseListener baseListener) {
        Log.d("url",url);
        HttpConnect.postServer(url, username, password, new HttpConnect.HttpConnectCallBack() {
            @Override
            public void onFinish(String response) {
                Log.d("response",response);
                baseListener.querySuccess(response,type);
            }

            @Override
            public void onError(String response) {
                Log.d("response",response);
                baseListener.queryFail(response);
            }
        });
    }
    // get 请求
    @Override
    public void queryGet(String url, String username, String password, final int type, final IBaseListener baseListener) {
        Log.d("url",url);
        HttpConnect.getServer(url, username, password, new HttpConnect.HttpConnectCallBack() {
            @Override
            public void onFinish(String response) {
                Log.d("response",response);
                baseListener.querySuccess(response,type);
            }

            @Override
            public void onError(String response) {
                Log.d("response",response);
                baseListener.queryFail(response);
            }
        });
    }
}
