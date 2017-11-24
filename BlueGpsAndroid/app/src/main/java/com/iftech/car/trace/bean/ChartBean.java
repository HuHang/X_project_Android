package com.iftech.car.trace.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/17
 * description:
 **/
public class ChartBean implements Serializable {

    private String type;
    private List<Item> data = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public class Item{
        private String type;
        private String name;
        private String url;
        private String remark;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
