package com.iftech.car.trace.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tanghuosong on 2016/11/1.
 */
public class ShopBean implements Serializable {
    private long id;
    private String name;
    private String fullName;
    private String address;
    private String longitude;
    private String latitude;
    private String allBankPath;
    private int shopType;
    private long parentId;
    private boolean isChecked = false;
    private String sortLetters; // 显示数据拼音的首字母
    List<ShopBean> ChildShops;

    @Override
    public String toString() {
        return "ShopBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", allBankPath='" + allBankPath + '\'' +
                ", shopType=" + shopType +
                ", parentId=" + parentId +
                ", isChecked=" + isChecked +
                ", sortLetters='" + sortLetters + '\'' +
                ", ChildShops=" + ChildShops +
                '}';
    }

    public ShopBean(){}

    public ShopBean(long id, String name, String fullName, String address, String longitude, String latitude, String allBankPath, int shopType, long parentId, boolean isChecked, String sortLetters, List<ShopBean> childShops) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.allBankPath = allBankPath;
        this.shopType = shopType;
        this.parentId = parentId;
        this.isChecked = isChecked;
        this.sortLetters = sortLetters;
        ChildShops = childShops;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getShopType() {
        return shopType;
    }

    public String getAllBankPath() {
        return allBankPath;
    }

    public void setAllBankPath(String allBankPath) {
        this.allBankPath = allBankPath;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public List<ShopBean> getChildShops() {
        return ChildShops;
    }

    public void setChildShops(List<ShopBean> ChildShops) {
        this.ChildShops = ChildShops;
    }

}
