package com.iftech.car.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tanghuosong on 2017/3/22.
 * Description:操作sharePreference 工具
 */
public class SpUtils {

    public static String getStringSp(Context context, String spName, String contentName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(contentName,"");
    }

    public static int getIntSp(Context context, String spName, String contentName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(contentName,0);
    }
    public static Long getLongSp(Context context, String spName, String contentName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(contentName,0);
    }

    public static void setStringSp(Context context, String spName, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void setLongSp(Context context, String spName, String key, Long value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();
    }

}
