package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/17
 * description:
 **/
public class Stock implements Serializable {
    private String type;
    private int data;

    @Override
    public String toString() {
        return "Stock{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
