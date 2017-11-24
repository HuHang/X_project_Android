package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/5
 * description: 消息详情bean
 **/
public class MessageBean implements Serializable{
     private long id;
     private long deviceId;
     private int alarmType;
     private String alarmTypeStr;
     private String createdAt;
     private boolean readed;
     private String AlarmWorkState;
     private String AlarmWorkStateDisplay;
     private String AlarmFenceState;
     private String AlarmFenceStateDisplay;
     private String AlarmBattery;
     private String AlarmSignalLevel;
     private String AlarmSignalLevelDisplay;
     private String CarState;
     private String CarStateDisplay;
     private String AlarmCurrentShopType;
     private String AlarmCurrentShopTypeDisplay;
     private String AlarmCurrentShopId;
     private String AlarmCurrentShopName;
     private String AlarmShopId;
     private String AlarmShopName;

     private String AlarmBatteryState;
     private String AlarmBatteryStateDisplay;
     private String nr;
     private String imei;
     private String battery;
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
     private String ShopBDLng;
     private String ShopBDLat;
     private String CurrentBDLng;
     private String CurrentBDLat;
     private String signalTime;
     private String carId;
     private String vin;
     private String carType;
     private String carColor;
     private String shopId;
     private String shopName;
     private String bankId;
     private String bankName;
    private String currentShopId;
    private String currentShopName;
    private String shopType;
    private String shopTypeDisplay;
    private String currentShopType;
    private String currentShopTypeDisplay;
    public MessageBean() {
    }

    public MessageBean(long id, long deviceId, int alarmType, String alarmTypeStr, String createdAt, boolean readed, String alarmWorkState, String alarmWorkStateDisplay, String alarmFenceState, String alarmFenceStateDisplay, String alarmBattery, String alarmSignalLevel, String alarmSignalLevelDisplay, String carState, String carStateDisplay, String alarmCurrentShopType, String alarmCurrentShopTypeDisplay, String alarmCurrentShopId, String alarmCurrentShopName, String alarmShopId, String alarmShopName, String alarmBatteryState, String alarmBatteryStateDisplay, String nr, String imei, String battery, String batteryPercent, String workState, String workStateDisplay, String batteryState, String batteryStateDisplay, String fenceState, String fenceStateDisplay, String signalLevel, String signalLevelDisplay, boolean removed, String singleType, double lng, double lat, String shopBDLng, String shopBDLat, String currentBDLng, String currentBDLat, String signalTime, String carId, String vin, String carType, String carColor, String shopId, String shopName, String bankId, String bankName, String currentShopId, String currentShopName, String shopType, String shopTypeDisplay, String currentShopType, String currentShopTypeDisplay) {
        this.id = id;
        this.deviceId = deviceId;
        this.alarmType = alarmType;
        this.alarmTypeStr = alarmTypeStr;
        this.createdAt = createdAt;
        this.readed = readed;
        AlarmWorkState = alarmWorkState;
        AlarmWorkStateDisplay = alarmWorkStateDisplay;
        AlarmFenceState = alarmFenceState;
        AlarmFenceStateDisplay = alarmFenceStateDisplay;
        AlarmBattery = alarmBattery;
        AlarmSignalLevel = alarmSignalLevel;
        AlarmSignalLevelDisplay = alarmSignalLevelDisplay;
        CarState = carState;
        CarStateDisplay = carStateDisplay;
        AlarmCurrentShopType = alarmCurrentShopType;
        AlarmCurrentShopTypeDisplay = alarmCurrentShopTypeDisplay;
        AlarmCurrentShopId = alarmCurrentShopId;
        AlarmCurrentShopName = alarmCurrentShopName;
        AlarmShopId = alarmShopId;
        AlarmShopName = alarmShopName;
        AlarmBatteryState = alarmBatteryState;
        AlarmBatteryStateDisplay = alarmBatteryStateDisplay;
        this.nr = nr;
        this.imei = imei;
        this.battery = battery;
        this.batteryPercent = batteryPercent;
        this.workState = workState;
        this.workStateDisplay = workStateDisplay;
        this.batteryState = batteryState;
        this.batteryStateDisplay = batteryStateDisplay;
        this.fenceState = fenceState;
        this.fenceStateDisplay = fenceStateDisplay;
        this.signalLevel = signalLevel;
        this.signalLevelDisplay = signalLevelDisplay;
        this.removed = removed;
        this.singleType = singleType;
        this.lng = lng;
        this.lat = lat;
        ShopBDLng = shopBDLng;
        ShopBDLat = shopBDLat;
        CurrentBDLng = currentBDLng;
        CurrentBDLat = currentBDLat;
        this.signalTime = signalTime;
        this.carId = carId;
        this.vin = vin;
        this.carType = carType;
        this.carColor = carColor;
        this.shopId = shopId;
        this.shopName = shopName;
        this.bankId = bankId;
        this.bankName = bankName;
        this.currentShopId = currentShopId;
        this.currentShopName = currentShopName;
        this.shopType = shopType;
        this.shopTypeDisplay = shopTypeDisplay;
        this.currentShopType = currentShopType;
        this.currentShopTypeDisplay = currentShopTypeDisplay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmTypeStr() {
        return alarmTypeStr;
    }

    public void setAlarmTypeStr(String alarmTypeStr) {
        this.alarmTypeStr = alarmTypeStr;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public String getAlarmWorkState() {
        return AlarmWorkState;
    }

    public void setAlarmWorkState(String alarmWorkState) {
        AlarmWorkState = alarmWorkState;
    }

    public String getAlarmWorkStateDisplay() {
        return AlarmWorkStateDisplay;
    }

    public void setAlarmWorkStateDisplay(String alarmWorkStateDisplay) {
        AlarmWorkStateDisplay = alarmWorkStateDisplay;
    }

    public String getAlarmFenceState() {
        return AlarmFenceState;
    }

    public void setAlarmFenceState(String alarmFenceState) {
        AlarmFenceState = alarmFenceState;
    }

    public String getAlarmFenceStateDisplay() {
        return AlarmFenceStateDisplay;
    }

    public void setAlarmFenceStateDisplay(String alarmFenceStateDisplay) {
        AlarmFenceStateDisplay = alarmFenceStateDisplay;
    }

    public String getAlarmBattery() {
        return AlarmBattery;
    }

    public void setAlarmBattery(String alarmBattery) {
        AlarmBattery = alarmBattery;
    }

    public String getAlarmSignalLevel() {
        return AlarmSignalLevel;
    }

    public void setAlarmSignalLevel(String alarmSignalLevel) {
        AlarmSignalLevel = alarmSignalLevel;
    }

    public String getAlarmSignalLevelDisplay() {
        return AlarmSignalLevelDisplay;
    }

    public void setAlarmSignalLevelDisplay(String alarmSignalLevelDisplay) {
        AlarmSignalLevelDisplay = alarmSignalLevelDisplay;
    }

    public String getCarState() {
        return CarState;
    }

    public void setCarState(String carState) {
        CarState = carState;
    }

    public String getCarStateDisplay() {
        return CarStateDisplay;
    }

    public void setCarStateDisplay(String carStateDisplay) {
        CarStateDisplay = carStateDisplay;
    }

    public String getAlarmCurrentShopType() {
        return AlarmCurrentShopType;
    }

    public void setAlarmCurrentShopType(String alarmCurrentShopType) {
        AlarmCurrentShopType = alarmCurrentShopType;
    }

    public String getAlarmCurrentShopTypeDisplay() {
        return AlarmCurrentShopTypeDisplay;
    }

    public void setAlarmCurrentShopTypeDisplay(String alarmCurrentShopTypeDisplay) {
        AlarmCurrentShopTypeDisplay = alarmCurrentShopTypeDisplay;
    }

    public String getAlarmShopId() {
        return AlarmShopId;
    }

    public void setAlarmShopId(String alarmShopId) {
        AlarmShopId = alarmShopId;
    }

    public String getAlarmShopName() {
        return AlarmShopName;
    }

    public void setAlarmShopName(String alarmShopName) {
        AlarmShopName = alarmShopName;
    }

    public String getAlarmCurrentShopId() {
        return AlarmCurrentShopId;
    }

    public void setAlarmCurrentShopId(String alarmCurrentShopId) {
        AlarmCurrentShopId = alarmCurrentShopId;
    }

    public String getAlarmCurrentShopName() {
        return AlarmCurrentShopName;
    }

    public void setAlarmCurrentShopName(String alarmCurrentShopName) {
        AlarmCurrentShopName = alarmCurrentShopName;
    }

    public String getAlarmBatteryState() {
        return AlarmBatteryState;
    }

    public void setAlarmBatteryState(String alarmBatteryState) {
        AlarmBatteryState = alarmBatteryState;
    }

    public String getAlarmBatteryStateDisplay() {
        return AlarmBatteryStateDisplay;
    }

    public void setAlarmBatteryStateDisplay(String alarmBatteryStateDisplay) {
        AlarmBatteryStateDisplay = alarmBatteryStateDisplay;
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

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
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

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
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

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopTypeDisplay() {
        return shopTypeDisplay;
    }

    public void setShopTypeDisplay(String shopTypeDisplay) {
        this.shopTypeDisplay = shopTypeDisplay;
    }

    public String getCurrentShopType() {
        return currentShopType;
    }

    public void setCurrentShopType(String currentShopType) {
        this.currentShopType = currentShopType;
    }

    public String getCurrentShopTypeDisplay() {
        return currentShopTypeDisplay;
    }

    public void setCurrentShopTypeDisplay(String currentShopTypeDisplay) {
        this.currentShopTypeDisplay = currentShopTypeDisplay;
    }
}
