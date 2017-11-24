package com.iftech.car.trace.model;

import android.util.Log;
import com.iftech.car.trace.model.iface.ICarDeviceInfoModel;
import com.iftech.car.trace.model.listener.IBaseListener;
import com.iftech.car.utils.HttpConnect;
import org.json.JSONObject;

/**
 * created by tanghuosong 2017/5/5
 * description: 车辆、设备信息查询
 **/
public class CarDeviceInfoModel extends BaseModel implements ICarDeviceInfoModel{

    @Override
    public void queryJsonPost(String url, String username, String password, JSONObject jsonObject, final int type , final IBaseListener baseListener) {
        Log.d("url",url);
        HttpConnect.postJsonServer(url, username, password,jsonObject, new HttpConnect.HttpConnectCallBack() {
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
