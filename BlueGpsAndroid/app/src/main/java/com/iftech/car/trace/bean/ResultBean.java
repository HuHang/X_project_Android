package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/1
 * description: 用于简单实体类映射
 **/
public class ResultBean implements Serializable{

//    {Success=bool,Content=string}

    private boolean Success;
    private String Content;

    public ResultBean() {
    }

    public ResultBean(boolean success, String content) {
        Success = success;
        Content = content;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "Success=" + Success +
                ", Content='" + Content + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
