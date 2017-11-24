package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/4
 * description: 设备信息bean
 **/
public class DeviceInfoBean implements Serializable{

     private String id;
     private String nr;
     private String imei;
     private int battery;
     private String batteryPercent;
     private String workState;
     private String workStateDisplay;
     private String batteryState;
     private String batteryStateDisplay;
     private String fenceState;
     private String fenceStateDisplay;
     private String signalLevel;
     private String signalLevelDisplay;
     private boolean removed;
     private String singleType;
     private double lng;
     private double lat;
     private double ShopLng;
     private double ShopLat;
     private String ShopBDLng;
     private String ShopBDLat;
     private String CurrentBDLng;
     private String CurrentBDLat;
     private String signalTime;
     private int carId;
     private String vin;
     private String carType;
     private String brand;
     private String carColor;
     private int shopId;
     private String shopName;
     private int bankId;
     private String bankName;
     private String currentShopId;
     private String currentShopName;
     private int shopType;
     private String shopTypeDisplay;
     private int currentShopType;
     private String currentShopTypeDisplay;


    public DeviceInfoBean() {
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(String batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getWorkStateDisplay() {
        return workStateDisplay;
    }

    public void setWorkStateDisplay(String workStateDisplay) {
        this.workStateDisplay = workStateDisplay;
    }

    public String getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(String batteryState) {
        this.batteryState = batteryState;
    }

    public String getBatteryStateDisplay() {
        return batteryStateDisplay;
    }

    public void setBatteryStateDisplay(String batteryStateDisplay) {
        this.batteryStateDisplay = batteryStateDisplay;
    }

    public String getFenceState() {
        return fenceState;
    }

    public void setFenceState(String fenceState) {
        this.fenceState = fenceState;
    }

    public String getFenceStateDisplay() {
        return fenceStateDisplay;
    }

    public void setFenceStateDisplay(String fenceStateDisplay) {
        this.fenceStateDisplay = fenceStateDisplay;
    }

    public String getSignalLevel() {
        return signalLevel;
    }

    public void setSignalLevel(String signalLevel) {
        this.signalLevel = signalLevel;
    }

    public String getSignalLevelDisplay() {
        return signalLevelDisplay;
    }

    public void setSignalLevelDisplay(String signalLevelDisplay) {
        this.signalLevelDisplay = signalLevelDisplay;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getSingleType() {
        return singleType;
    }

    public void setSingleType(String singleType) {
        this.singleType = singleType;
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

    public double getShopLng() {
        return ShopLng;
    }

    public void setShopLng(double shopLng) {
        ShopLng = shopLng;
    }

    public double getShopLat() {
        return ShopLat;
    }

    public void setShopLat(double shopLat) {
        ShopLat = shopLat;
    }

    public String getShopBDLng() {
        return ShopBDLng;
    }

    public void setShopBDLng(String shopBDLng) {
        ShopBDLng = shopBDLng;
    }

    public String getShopBDLat() {
        return ShopBDLat;
    }

    public void setShopBDLat(String shopBDLat) {
        ShopBDLat = shopBDLat;
    }

    public String getCurrentBDLng() {
        return CurrentBDLng;
    }

    public void setCurrentBDLng(String currentBDLng) {
        CurrentBDLng = currentBDLng;
    }

    public String getCurrentBDLat() {
        return CurrentBDLat;
    }

    public void setCurrentBDLat(String currentBDLat) {
        CurrentBDLat = currentBDLat;
    }

    public String getSignalTime() {
        return signalTime;
    }

    public void setSignalTime(String signalTime) {
        this.signalTime = signalTime;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCurrentShopId() {
        return currentShopId;
    }

    public void setCurrentShopId(String currentShopId) {
        this.currentShopId = currentShopId;
    }

    public String getCurrentShopName() {
        return currentShopName;
    }

    public void setCurrentShopName(String currentShopName) {
        this.currentShopName = currentShopName;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public String getShopTypeDisplay() {
        return shopTypeDisplay;
    }

    public void setShopTypeDisplay(String shopTypeDisplay) {
        this.shopTypeDisplay = shopTypeDisplay;
    }

    public int getCurrentShopType() {
        return currentShopType;
    }

    public void setCurrentShopType(int currentShopType) {
        this.currentShopType = currentShopType;
    }

    public String getCurrentShopTypeDisplay() {
        return currentShopTypeDisplay;
    }

    public void setCurrentShopTypeDisplay(String currentShopTypeDisplay) {
        this.currentShopTypeDisplay = currentShopTypeDisplay;
    }
}
