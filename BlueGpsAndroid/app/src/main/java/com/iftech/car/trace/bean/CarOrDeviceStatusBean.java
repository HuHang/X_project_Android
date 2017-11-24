package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/18
 * description:
 **/
public class CarOrDeviceStatusBean implements Serializable{
     private String type;
     private String name;
     private int count;

    public CarOrDeviceStatusBean() {
    }

    public CarOrDeviceStatusBean(String type, String name, int count) {
        this.type = type;
        this.name = name;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
