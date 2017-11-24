package com.iftech.car.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by Tang Huo song on 2016/11/8.
 */
public class GpsToBaidu {
    public static LatLng gpsToBaiDu(double lat,double lng){
        // 将gps坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // 创建LatLng
        LatLng latLng = new LatLng(lat,lng);
        // sourceLatLng待转换坐标
        converter.coord(latLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

}
