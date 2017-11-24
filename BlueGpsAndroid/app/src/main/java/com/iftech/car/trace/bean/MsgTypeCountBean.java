package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/4
 * description: 所有订阅消息类型及其数量
 **/
public class MsgTypeCountBean implements Serializable{

      private int KeyValue;
      private String KeyStr;
      private int Count;
      private boolean isFavorite;

      public MsgTypeCountBean(int keyValue, String keyStr, int count, boolean isFavorite) {
            KeyValue = keyValue;
            KeyStr = keyStr;
            Count = count;
            this.isFavorite = isFavorite;
      }

      public int getKeyValue() {
            return KeyValue;
      }

      public void setKeyValue(int keyValue) {
            KeyValue = keyValue;
      }

      public String getKeyStr() {
            return KeyStr;
      }

      public void setKeyStr(String keyStr) {
            KeyStr = keyStr;
      }

      public int getCount() {
            return Count;
      }

      public void setCount(int count) {
            Count = count;
      }

      public boolean isFavorite() {
            return isFavorite;
      }

      public void setFavorite(boolean favorite) {
            isFavorite = favorite;
      }
}
