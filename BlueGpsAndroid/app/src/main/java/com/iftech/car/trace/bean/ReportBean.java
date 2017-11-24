package com.iftech.car.trace.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/19
 * description:
 **/
public class ReportBean implements Serializable{
    private boolean isOpen;
    private int chartType;
    private String chartTypeDisplay;
    private int headerId ;
    private String header;
    private String footer;
    private String url;
    private String defaultUrl;
    private List<Item> summary  = new ArrayList<>();

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    public String getChartTypeDisplay() {
        return chartTypeDisplay;
    }

    public void setChartTypeDisplay(String chartTypeDisplay) {
        this.chartTypeDisplay = chartTypeDisplay;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Item> getSummary() {
        return summary;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public void setSummary(List<Item> summary) {
        this.summary = summary;
    }

    public class Item{
        private String type;
        private String typeDisplay;
        private String data;
        private String remark;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeDisplay() {
            return typeDisplay;
        }

        public void setTypeDisplay(String typeDisplay) {
            this.typeDisplay = typeDisplay;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
