package com.iftech.car.trace.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/9
 * description: 历史轨迹
 **/
public class TrackingHistoryBean implements Serializable{

    DeviceInfoBean device;
    List<GpsBean> gps = new ArrayList<>();

    public DeviceInfoBean getDevice() {
        return device;
    }

    public void setDevice(DeviceInfoBean device) {
        this.device = device;
    }

    public List<GpsBean> getGps() {
        return gps;
    }

    public void setGps(List<GpsBean> gps) {
        this.gps = gps;
    }

    public class GpsBean{
        private long id;
        private double lng;
        private double lat;
        private double BDlng;
        private double BDlat;
        private String signalTime;
        private String singleType;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getBDlng() {
            return BDlng;
        }

        public void setBDlng(double BDlng) {
            this.BDlng = BDlng;
        }

        public double getBDlat() {
            return BDlat;
        }

        public void setBDlat(double BDlat) {
            this.BDlat = BDlat;
        }

        public String getSignalTime() {
            return signalTime;
        }

        public void setSignalTime(String signalTime) {
            this.signalTime = signalTime;
        }

        public String getSingleType() {
            return singleType;
        }

        public void setSingleType(String singleType) {
            this.singleType = singleType;
        }
    }
}
