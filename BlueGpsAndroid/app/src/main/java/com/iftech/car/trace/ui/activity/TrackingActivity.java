package com.iftech.car.trace.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.daasuu.bl.BubbleLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.trace.bean.CarInfoBean;
import com.iftech.car.trace.bean.TrackingHistoryBean;
import com.iftech.car.trace.presenter.TrackingPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.TrackingView;
import com.iftech.car.utils.GpsToBaidu;
import com.iftech.car.utils.TimeUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrackingActivity extends AppCompatActivity implements TrackingView{

//    @InjectView(R.id.title)
//    Toolbar toolbar;
    @InjectView(R.id.back)
    ImageView back;
//    @InjectView(R.id.toolbar_title)
//    TextView toolbar_title;
//    @InjectView(R.id.toolbar_subtitle)
//    TextView toolbar_subtitle;
    @InjectView(R.id.mapView)
    MapView mapView;
    @InjectView(R.id.IMeiTV)
    TextView IMeiTV;
    @InjectView(R.id.latTV)
    TextView latTV;
    @InjectView(R.id.lngTV)
    TextView lngTV;
    @InjectView(R.id.imageView1)
    ImageView imageView1;
    @InjectView(R.id.imageView2)
    ImageView imageView2;
    @InjectView(R.id.currentLocation)
    TextView currentLocation;
    @InjectView(R.id.showSettings)
    ImageView showSettings;
    @InjectView(R.id.showLines)
    ImageView showLines;
    @InjectView(R.id.settingsRL)
    RelativeLayout settingsRL;
    @InjectView(R.id.startTime)
    TextView startTime;
    @InjectView(R.id.endTime)
    TextView endTime;
    @InjectView(R.id.closeBtn)
    ImageView closeBtn;

    private GeoCoder mSearch = null;//反编译地理位置
    private BaiduMap baiduMap;
    // 标注图标
    private Marker moveMarker;
    private Polyline mPolyline;
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00002;
    int position = 0;
    double speed = DISTANCE;
    private Handler mnHandler;
    private boolean isRun = false;
    private boolean isChange = true;

    private CarInfoBean carInfoBean ;
    private String deviceId = "";
    private String vin = "";
    private String fenceState = "";
    private TrackingHistoryBean trackingHistoryBean = new TrackingHistoryBean();
    private Calendar startDate   = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private IBasePresenter trackingPresenter;
    private final static int GET_HISTORY = 1004;
    private final static int GET_CURRENT_STATE = 10010;
    private final static int GET_BIND_INFO = 10111;
    private final static String TAG = "TrackingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tracking);
        ButterKnife.inject(this);
        trackingPresenter = new TrackingPresenter(this);
        deviceId = getIntent().getExtras().getString("deviceId");
        vin = getIntent().getExtras().getString("vin");
        fenceState = getIntent().getExtras().getString("fenceState");
//        toolbar_title.setText(vin);
        mSearch = GeoCoder.newInstance();
        initView();
        initEvents();
    }

    private void initView(){
        baiduMap = mapView.getMap();
        getCurrentState();
        mnHandler = new Handler(Looper.getMainLooper());
        trackingHistoryBean = new TrackingHistoryBean();
        back.setVisibility(View.VISIBLE);
//        toolbar_subtitle.setVisibility(View.GONE);
        mSearch.setOnGetGeoCodeResultListener(listener);

        long timeMill = Calendar.getInstance().getTimeInMillis()+60*60*24*1000;
        startDate.set(2016,1,1);
        endDate.set(TimeUtil.getYearByTimeStamp(timeMill),TimeUtil.getMonthByTimeStamp(timeMill),TimeUtil.getDayByTimeStamp(timeMill));
        startTime.setText(TimeUtil.timeStampToDate(Calendar.getInstance().getTimeInMillis()-1000*60*60*24));
        endTime.setText(TimeUtil.timeStampToDate(Calendar.getInstance().getTimeInMillis()));
//        getHistory();
    }

    private void initEvents(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingsRL.isShown()){
                    settingsRL.setVisibility(View.GONE);
                }else{
                    settingsRL.setVisibility(View.VISIBLE);
                }
            }
        });

        showLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRun = true;
                if(isChange){
                    if(trackingHistoryBean.getGps().size()<2){
                        Toast.makeText(getApplicationContext(),"当前时间段无移动记录",Toast.LENGTH_SHORT).show();
                    }else{
                        drawPolyLine();
                        move();
                        isChange = false;
                    }
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                setTime(startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(endTime);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsRL.setVisibility(View.GONE);
            }
        });
    }

    private void setTime(final TextView view){
        TimePickerView pvTime = new TimePickerView.Builder(TrackingActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                view.setText(TimeUtil.timeStampToDate(date.getTime()));
                isRun = false;
                isChange = true;
                getHistory();
            }
        }).setRangDate(startDate,endDate).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    private void showCurrent(){
        if(baiduMap != null ){
            baiduMap.clear();
        }
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(carInfoBean.getCurrentBDLat(),carInfoBean.getCurrentBDLng())));
        IMeiTV.setText(getString(R.string.device_num)+carInfoBean.getImei());
        latTV.setText(""+carInfoBean.getCurrentBDLng());
        lngTV.setText(""+carInfoBean.getCurrentBDLat());



        //定义Maker坐标点
        LatLng point = GpsToBaidu.gpsToBaiDu(carInfoBean.getLat(), carInfoBean.getLng());
        //构建Marker图标
        BitmapDescriptor bitmap = null;
        if("100".equals(fenceState)){
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.blue_car);
        }else{
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.red_car);
        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        Marker marker = (Marker) baiduMap.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putString("vin","车架号："+vin);
        marker.setExtraInfo(bundle);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo((float) 12.0));
        baiduMap.animateMapStatus(u);
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.infowindow_monitor, null);
        final TextView textView = (TextView) bubbleLayout.findViewById(R.id.infoTV);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker1) {
                if( !isRun){
                    InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker1.getPosition(), -87);
                    textView.setText(marker1.getExtraInfo().getString("vin"));
                    baiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });
    }

    private void getCurrentState(){
        String url = MainTabActivity.ip+Api.currentState+"?id="+deviceId;
        Log.d(TAG,url);
        trackingPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,GET_CURRENT_STATE);
    }
    private void getHistory(){
        String url = MainTabActivity.ip+ Api.getHistoryTrack
                +"?id="+deviceId+"&startTime="+ startTime.getText().toString().replace(" ","T")+"&endTime="+endTime.getText().toString().replace(" ","T");
        Log.d(TAG,url);
        trackingPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,GET_HISTORY);
    }

    private void getBindInfo(){
        String url = MainTabActivity.ip+Api.getBindInfo+"?vin="+vin;
        Log.d(TAG,url);
        trackingPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,GET_BIND_INFO);
    }

    // 画运动轨迹
    private void drawPolyLine() {

        if(baiduMap != null ){
            baiduMap.clear();
        }
        if(moveMarker != null){
            moveMarker.remove();
        }
        // 设置地图中心为第一个点
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(GpsToBaidu.gpsToBaiDu(trackingHistoryBean.getGps().get(0).getLat(),trackingHistoryBean.getGps().get(0).getLng()));
        baiduMap.setMapStatus(u);
        // 设置起点终点标注
        OverlayOptions ooA = new MarkerOptions().position(GpsToBaidu.gpsToBaiDu(trackingHistoryBean.getGps().get(0).getLat(),trackingHistoryBean.getGps().get(0).getLng())).icon( BitmapDescriptorFactory.fromResource(R.drawable.icon_st)).draggable(true);
        Marker marker1 = (Marker) baiduMap.addOverlay(ooA);
        Bundle bundle = new Bundle();
        bundle.putString("vin","起点："+trackingHistoryBean.getGps().get(0).getSignalTime().substring(0,19).replace("T"," "));
        marker1.setExtraInfo(bundle);

        OverlayOptions ooB = new MarkerOptions().position(GpsToBaidu.gpsToBaiDu(trackingHistoryBean.getGps().get(trackingHistoryBean.getGps().size()-1).getLat(),trackingHistoryBean.getGps().get(trackingHistoryBean.getGps().size()-1).getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_en)).draggable(true);
        Marker marker2 = (Marker) baiduMap.addOverlay(ooB);
        Bundle bundle1 = new Bundle();
        bundle1.putString("vin","终点："+trackingHistoryBean.getGps().get(trackingHistoryBean.getGps().size()-1).getSignalTime().substring(0,19).replace("T"," "));
        marker2.setExtraInfo(bundle1);

        List<LatLng> pointsLines = new ArrayList<>();
        for(int index = 0; index<trackingHistoryBean.getGps().size(); index++){
            pointsLines.add(GpsToBaidu.gpsToBaiDu(trackingHistoryBean.getGps().get(index).getLat(),trackingHistoryBean.getGps().get(index).getLng()));
        }

        PolylineOptions polylineOptions = new PolylineOptions().points(pointsLines).width(5).color(Color.RED);

        mPolyline = (Polyline) baiduMap.addOverlay(polylineOptions);
        OverlayOptions markerOptions;
        //构建Marker图标
        BitmapDescriptor bitmap = null;
        if("100".equals(fenceState)){
//            markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_car)).position(pointsLines.get(0)).rotate(1);
            markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_car)).position(pointsLines.get(0));
//                    .rotate((float) getAngle(0));
        }else{
            markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_car)).position(pointsLines.get(0));
//                    .rotate(1);
        }

        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));
        moveMarker = (Marker) baiduMap.addOverlay(markerOptions);
        Bundle bundle2 = new Bundle();
        bundle2.putString("vin","车架号："+vin);
        moveMarker.setExtraInfo(bundle2);
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 180;
            } else {
                return 180;
//                return 90;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
//            deltAngle = 90;
        }
        double radio = Math.atan(slope);
//        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        double angle = 180 * (radio / Math.PI) + deltAngle;
//        return angle;
        return 1.0;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        if(slope == 0.0){
            slope=0.1;
        }
        return slope;
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return speed;
        }
        return Math.abs((speed * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 循环进行移动逻辑
     */
    private void move(){
        if(trackingHistoryBean.getGps().size()<2){
            Toast.makeText(getApplicationContext(),"当前时间段没有移动记录",Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (isRun){
                    for (int i = 0; i < trackingHistoryBean.getGps().size() - 1; i++) {
                        position = i;
                        final LatLng startPoint = GpsToBaidu.gpsToBaiDu(trackingHistoryBean.getGps().get(i).getLat(),trackingHistoryBean.getGps().get(i).getLng());
                        final LatLng endPoint = GpsToBaidu.gpsToBaiDu(trackingHistoryBean.getGps().get(i + 1).getLat(),trackingHistoryBean.getGps().get(i+1).getLng());
                        moveMarker.setPosition(startPoint);
                        mnHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (baiduMap == null) {
                                    return;
                                }
                                // marker的旋转角度
                                moveMarker.setRotate((float) getAngle(startPoint, endPoint));
//                                moveMarker.setRotate(1);
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
//                        double slope = 1.0;
                        //是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);
                        double intercept = getInterception(slope, startPoint);
//                        double intercept = 1.0;

                        double xMoveDistance = isReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);

                        for (double j = startPoint.latitude; j > endPoint.latitude == isReverse; j = j - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }

                            final LatLng finalLatLng = latLng;
                            mnHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (baiduMap == null) {
                                        return;
                                    }
                                    moveMarker.setPosition(finalLatLng);
                                }
                            });
                            TrackingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(TrackingActivity.this).inflate(R.layout.infowindow_tracking, null);
                                    TextView infoTV = (TextView) bubbleLayout.findViewById(R.id.infoTV);
                                    if(trackingHistoryBean.getGps().size() >position){
                                        infoTV.setText(trackingHistoryBean.getGps().get(position).getSignalTime().substring(0,19).replace("T"," "));
                                        InfoWindow infoWindow = new InfoWindow(bubbleLayout,moveMarker.getPosition(),-27);
                                        baiduMap.showInfoWindow(infoWindow);
                                    }
                                }
                            });

                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(i == trackingHistoryBean.getGps().size()-2){
                            isRun = false;
                        }
                    }// for 循环结束
                }
             }
        }).start();
    }
    @Override
    public void querySuccess(final String msg, final int type) {
        Log.d(TAG,msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case GET_HISTORY:
                        trackingHistoryBean.getGps().clear();
                        trackingHistoryBean = new Gson().fromJson(msg,TrackingHistoryBean.class);
                        if(trackingHistoryBean.getGps().size()>2){
                            drawPolyLine();
                        }else {
                            showCurrent();
                            Toast.makeText(TrackingActivity.this,"当前时间没有历史轨迹",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case GET_CURRENT_STATE:
                        carInfoBean = new Gson().fromJson(msg,CarInfoBean.class);
                        showCurrent();
                        getBindInfo();
                        break;
                    case GET_BIND_INFO:
                        if(null != msg && !"null".equals(msg)){
                            try{
                                JSONObject jsonObject = new JSONObject(msg);
                                JSONArray urls  = jsonObject.getJSONArray("imagesUrl");
                                Log.d(TAG,urls.toString());
                                Glide.with(TrackingActivity.this).load(urls.get(0)).into(imageView1);
                                Glide.with(TrackingActivity.this).load(urls.get(1)).into(imageView2);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:break;
                }
            }
        });
    }
    @Override
    public void queryFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrackingActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 从经纬度获取地址位置
    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(final GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentLocation.setText("定位失败！");
                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentLocation.setText(result.getAddress());
                }
            });
        }

        @Override
        public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentLocation.setText("定位失败！");
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentLocation.setText(result.getAddress());
                    }
                });
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}