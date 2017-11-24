package com.iftech.car.common;

/**
 * created by tanghuosong 2017-05-01 11:07
 * description:
 **/
public class Api {
    // 用户登录 ?email=" +"&pwd=";
    public final static String loginWithInfo = "/api/Users/LoginWithInfo";

    // 获取商店
    public final static String getAllShops = "/api/Shops/GetAllWithChild";
    // 获取商店下所有的绑定的车（监控界面）
    public final static String monitorCars = "/api/Shops/GetBindedByShops?";
    // 分商店返回所有的车
    public final static String getBindDeviceByShops = "/api/shops/GetBindedGroupByShops?";

    // 获取车辆信息（绑定和未绑定） { int shopId : 127, int pageIndex: 0 , bool? isBind: false}
    public final static String getAllCars = "/api/cars/AllByShopIdsWithPage";
    public final static String getAllCarsByShopList = "/api/cars/AllByShopIdsWithPageWithList";

    // 获取设备信息（使用和闲置) { int? shopId: 127, int pageIndex: 0, bool? isBind : false}
    public final static String getAllDevices = "/api/devices/AllByShopIdsWithPage";
    public final static String getAllDevicesByShopList = "/api/devices/AllByShopIdsWithPageWithList";

    // 搜索车辆信息  string searchStr: 白, int? shopIds[] : 127, int? shopIds[] : 110, string isBind: false, int pageIndex: 0
    public final static String searchCars = "/api/cars/FuzzySearch";
    public final static String searchCarsByShopList = "/api/cars/FuzzySearchWithList";

    // 搜索设备信息
    public final static String searchDevices = "/api/devices/FuzzySearch";
    public final static String searchDevicesByShopList = "/api/devices/FuzzySearchWithList";

    // 获取设备当前信息 ?id = (deviceId)
    public final static String currentState = "/api/Devices/GetLastest";
    // 获取某一段时间内数据 ?id=*&startTime=*&endTime=*
    public final static String getHistoryTrack = "/api/Devices/GetGpsList";
    // 获取设备绑定信息
    public final static String getBindInfo = "/api/CarDeviceBinds/GetBindInfo";

    //获取车辆的绑定和未绑定的统计信息 int? shopId: 127
    public final static String getGroupCarsCount = "/api/cars/GroupCarsCount?";
    public final static String getGroupCarsCountByShopList = "/api/cars/GroupCarsCountWithList";

    //获取设备的绑定和未绑定统计信息 int? shopId: 127
    public final static String getGroupDevicesCount = "/api/devices/GroupDeviceCount?";
    public final static String getGroupDevicesCountByShopList = "/api/devices/GroupDeviceCountWithList";

    // 获取所有订阅及其数量
    public final static String getAllFavoriteAndCount = "/api/Messages/FavoriteAlarmMsgTypeCount";
    // 订阅某类消息 int alarmType
    public final static String subScribeAlarm = "/api/FavoriteMessages/Subscribe";
    // 取消订阅某类警报	int alarmType
    public final static String unSubScripAlarm = "/api/FavoriteMessages/UnSubscribe";
    //获取未读消息 全部 报警数量统计
    public final static String getUnreadAlarmMsgTypeCount = "/api/Messages/UnreadAlarmMsgTypeCount";
    //获取所有类型的未读消息报警数量统计
    public final static String getAlarmMsgTypeCount = "/api/Messages/AlarmMsgTypeCount";
    //获取已读消息, 获取未读消息 {int? type: 19, int pageIndex: 1, bool? isRead: false }
    public final static String getUnReadAlarmMsgWithPage= "/api/Messages/UnReadAlarmMsgWithPage";
    // 标记一类消息为已读 type= type为必须，可以为空。空表示将所有消息都标记为已读
    public final static String readOneTypeMsg = "/api/Messages/ReadAllAlarmMsg";
    // 标记单个消息为已读?id=**
    public final static String readOneMsg = "/api/Messages/ReadAlarmMsg";

    // 快速搜索VIN号   string searchStr: 898, int? shopId: 127
    public final static String queryVin = "/api/cars/QSVIN";
    // 快速搜索Imei号 string searchStr: 8, int? shopId: 127
    public final static String queryImei = "/api/devices/QSIMEI";
    // 快速搜索Imei号和Vin号   string searchStr: 2,  int? shopId: 9,  string searchType: vin
    public final static String queryImeiOrVin = "/api/globals/QSVINOrIMEI";

    // 验证是否可以进行绑定   int shopId: 127, string imei: 868120146773707, string vin:LFPM4ACP1G1A73874
    public final static String checkVin = "/api/devices/canBind";
    //附带图片进行上传   int shopId: 127, string imei: 868120146773707,  string vin:LFPM4ACP1G1A73874,  两张图片
    public final static String bindVin = "/api/devices/BindWithPic";
    // 解绑设备
    public final static String relieveVin = "/api/Devices/UnBind?imeiOrVin=";

    // 所有列表
    public final static String getAllCharts = "/Report_DailySupervision/AllCharts";
    // 日常监管摘要  DateTime? startTime:, DateTime? endTime:
    public final static String dailySupervision = "/api/Report_DailySupervisions/Summary";
    // 日常监管详情
    public final static String getReportByType = "/api/Report_DailySupervisions/GetReportByType";
    // 获得所有的dashboard
    public final static String getDashBoard = "/api/dashboards/Dashboard";

}
