package com.iftech.car.trace.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/23
 * description: 监控界面显示item bean
 **/
public class MonitorBean implements Serializable{
    private ShopBean shop;
    private List<DeviceInfoBean> cars  = new ArrayList<>();

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public List<DeviceInfoBean> getCars() {
        return cars;
    }

    public void setCars(List<DeviceInfoBean> cars) {
        this.cars = cars;
    }
}
