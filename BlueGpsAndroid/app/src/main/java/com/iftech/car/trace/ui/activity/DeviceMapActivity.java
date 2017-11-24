package com.iftech.car.trace.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.daasuu.bl.BubbleLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.trace.bean.CarInfoBean;
import com.iftech.car.trace.presenter.TrackingPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.TrackingView;
import com.iftech.car.utils.GpsToBaidu;
import com.iftech.car.utils.TimeUtil;

import java.util.Calendar;

public class DeviceMapActivity extends AppCompatActivity implements TrackingView{

    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.mapView)
    MapView mapView;
    BaiduMap baiduMap;
    private IBasePresenter trackingPresenter;
    private final static String TAG = "DeviceMapActivity";
    private final static int GET_CURRENT_STATE = 12345;
    private String deviceId ;
    private String imei;
    private CarInfoBean carInfoBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_map);
        ButterKnife.inject(this);
        trackingPresenter = new TrackingPresenter(this);
        deviceId = getIntent().getStringExtra("deviceId");
        imei  =getIntent().getStringExtra("imei");
        initView();
        initEvents();
    }


    private void initView(){
        baiduMap = mapView.getMap();
        getCurrentState();
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCurrentState(){
        String url = MainTabActivity.ip+ Api.currentState+"?id="+deviceId;
        Log.d(TAG,url);
        trackingPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,GET_CURRENT_STATE);
    }

    private void showCurrent(){
        if(baiduMap != null ){
            baiduMap.clear();
        }
//        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(carInfoBean.getCurrentBDLat(),carInfoBean.getCurrentBDLng())));
//        IMeiTV.setText(getString(R.string.device_num)+carInfoBean.getImei());
//        latTV.setText(""+carInfoBean.getCurrentBDLng());
//        lngTV.setText(""+carInfoBean.getCurrentBDLat());
//
//        long timeMill = Calendar.getInstance().getTimeInMillis()+60*60*24*1000;
//        startDate.set(2016,1,1);
//        endDate.set(TimeUtil.getYearByTimeStamp(timeMill),TimeUtil.getMonthByTimeStamp(timeMill),TimeUtil.getDayByTimeStamp(timeMill));
//        startTime.setText(TimeUtil.timeStampToDate(Calendar.getInstance().getTimeInMillis()-1000*60*60*24));
//        endTime.setText(TimeUtil.timeStampToDate(Calendar.getInstance().getTimeInMillis()));

        //定义Maker坐标点
        LatLng point = GpsToBaidu.gpsToBaiDu(carInfoBean.getLat(), carInfoBean.getLng());
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_device);
//        if("100".equals(fenceState)){
//            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.blue_car);
//        }else{
//            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.red_car);
//        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        Marker marker = (Marker) baiduMap.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putString("vin","设备号："+imei);
        marker.setExtraInfo(bundle);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo((float) 12.0));
        baiduMap.animateMapStatus(u);
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.infowindow_monitor, null);
        final TextView textView = (TextView) bubbleLayout.findViewById(R.id.infoTV);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker1) {
                InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker1.getPosition(), -87);
                textView.setText(marker1.getExtraInfo().getString("vin"));
                baiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }


    @Override
    public void querySuccess(final String msg, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                carInfoBean = new Gson().fromJson(msg,CarInfoBean.class);
                showCurrent();
            }
        });
    }

    @Override
    public void queryFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
