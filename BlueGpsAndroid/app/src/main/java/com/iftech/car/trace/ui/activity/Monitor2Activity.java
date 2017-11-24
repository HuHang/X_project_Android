package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.daasuu.bl.BubbleLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.trace.adapter.MonitorExpandListViewAdapter;
import com.iftech.car.trace.bean.DeviceInfoBean;
import com.iftech.car.trace.bean.MonitorBean;
import com.iftech.car.trace.bean.ShopBean;
import com.iftech.car.trace.presenter.CarInfoPresenter;
import com.iftech.car.trace.presenter.iface.ICarDeviceInfoPresenter;
import com.iftech.car.trace.ui.view.CarInfoView;
import com.iftech.car.utils.GpsToBaidu;
import com.iftech.car.widget.PinnedHeaderExpandableListView;
import com.kaopiz.kprogresshud.KProgressHUD;
import android.os.Handler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
//import net.sf.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 监控界面
 * **/
public class Monitor2Activity extends AppCompatActivity implements CarInfoView , OnRefreshListener, OnLoadMoreListener {

    private final static String TAG = "Monitor2Activity";
//    @InjectView(R.id.title)
//    Toolbar toolbar;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.shopCount)
    TextView shopCount;
    @InjectView(R.id.carCount)
    TextView carCount;
    @InjectView(R.id.mapView)
    MapView mapView;
    BaiduMap baiduMap;

    @InjectView(R.id.swipe_target)
    PinnedHeaderExpandableListView recyclerView;
    @InjectView(R.id.id_empty_view)
    RelativeLayout id_empty_view;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @InjectView(R.id.mLayout)
    SlidingUpPanelLayout mLayout;

    @InjectView(R.id.spreadStatus)
    ImageView spreadStatus;
    @InjectView(R.id.spreadStatusLL)
    LinearLayout spreadStatusLL;

    Animation animation;

    private KProgressHUD hud;
    private MonitorExpandListViewAdapter adapter;
    private List<MonitorBean> list;
    private List<LatLng> latLngs;
    private String shopIds;
    private int carsCount =0;
    ICarDeviceInfoPresenter carInfoPresenter;

    private final static int QUERY_DATA =10124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitor2);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this);
        carInfoPresenter = new CarInfoPresenter(this);
        shopIds = getIntent().getStringExtra("shopId");
        initView();
        initEvents();
    }


    private void initView(){
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("获取中...")
                .setCancellable(false);

        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        back.setVisibility(View.VISIBLE);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        list = new ArrayList<>();
        latLngs = new ArrayList<>();
        recyclerView.setEmptyView(id_empty_view);
        autoRefresh();
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if(slideOffset == 1.0){
                    spreadStatus.setImageDrawable(getResources().getDrawable(R.drawable.monitor_expand));
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    mLayout.setTouchEnabled(true);
                    spreadStatus.setImageDrawable(getResources().getDrawable(R.drawable.monitor_close));

                    spreadStatusLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        }
                    });
                }
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    spreadStatusLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    });
                    mLayout.setTouchEnabled(false);
                    mLayout.setScrollableView(swipeToLoadLayout);

                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

    }

    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        list.clear();
        getData();
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }


    private void showCars(){
        for(int i = 0;i<list.size();i++) {
            for(DeviceInfoBean deviceInfoBean : list.get(i).getCars()){
                final String deviceId = deviceInfoBean.getId();
                final LatLng point = GpsToBaidu.gpsToBaiDu(deviceInfoBean.getLat(),deviceInfoBean.getLng());
                MarkerOptions options = null;
                if(deviceInfoBean.getFenceState().equals("100")){
                    options = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_car));
                }else{
                    options = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_car));
                }
                Marker marker = (Marker) baiduMap.addOverlay(options);
                Bundle bundle = new Bundle();
                bundle.putString("vin",deviceInfoBean.getVin()+"\n"+deviceInfoBean.getImei());
                bundle.putString("deviceId",deviceId+"");
                marker.setExtraInfo(bundle);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
                //以LatLng为中心点坐标，显示地图的大小比例，参数可调，值越大，地图比例越小，显示范围越小
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo((float) 11.0));
                baiduMap.animateMapStatus(u);
            }
                final ShopBean shopBean = list.get(i).getShop();
            //店铺标注
                if(shopBean.getLatitude() != null && shopBean.getLongitude() != null){
//                    Log.d("===========",shopBean.getLatitude()+"==========="+shopBean.getLatitude());
                    final LatLng point = GpsToBaidu.gpsToBaiDu(Double.parseDouble(shopBean.getLatitude()),Double.parseDouble(shopBean.getLongitude()));
                    MarkerOptions options;
                    if (shopBean.getShopType() == 0){
                         options = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_4sstore));
                    }else if (shopBean.getShopType() == 2){
                         options = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_twolibrary));
                    }else {
                         options = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_twonet));
                    }

                    Marker marker = (Marker) baiduMap.addOverlay(options);
                    Bundle bundle = new Bundle();
                    bundle.putString("vin",shopBean.getName()+"\n车辆数："+list.get(i).getCars().size()+"台");
                    marker.setExtraInfo(bundle);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
                    //以LatLng为中心点坐标，显示地图的大小比例，参数可调，值越大，地图比例越小，显示范围越小
                    baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo((float) 11.0));
                    baiduMap.animateMapStatus(u);
                }
        }
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.infowindow_monitor, null);
        final TextView textView = (TextView) bubbleLayout.findViewById(R.id.infoTV);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                textView.setText(marker.getExtraInfo().getString("vin"));
                InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker.getPosition(), -87);
                baiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    private void getData(){
        JSONObject param = new JSONObject();
        hud.show();
        try{
            param.put("shopIds",shopIds);
            String url = MainTabActivity.ip +"/api/shops/GetBindedGroupByShopsWithList";
            carInfoPresenter.queryJsonPost(url,MainTabActivity.username,MainTabActivity.password,param,QUERY_DATA);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void querySuccess( final String msg,final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case QUERY_DATA:
                        try{
                            JSONArray array = new JSONArray(msg);
                            Gson gson = new Gson();
                            for (int i= 0; i<array.length();i++){
                                MonitorBean monitorBean = gson.fromJson(array.get(i).toString(),MonitorBean.class);
                                list.add(monitorBean);
                                carsCount += monitorBean.getCars().size();
                                for(DeviceInfoBean deviceInfoBean : monitorBean.getCars()){
                                    latLngs.add(GpsToBaidu.gpsToBaiDu(deviceInfoBean.getLat(),deviceInfoBean.getLng()));
                                }
                            }
//                            recyclerView.setHeaderView(getLayoutInflater().inflate(R.layout.item_monitor_parent_slide,
//                                    recyclerView, false));
                            adapter = new MonitorExpandListViewAdapter(Monitor2Activity.this,recyclerView);
                            adapter.setData(list);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            carCount.setText(getString(R.string.car_count)+carsCount);
                            shopCount.setText(getString(R.string.shop_count)+list.size());
                            showCars();
                            adapter.setOnclickListener(new MonitorExpandListViewAdapter.OnclickListener() {
                                @Override
                                public void OnChildClick(int position, DeviceInfoBean deviceInfoBean) {
                                    Intent intent = new Intent(Monitor2Activity.this,TrackingActivity.class);
                                    intent.putExtra("vin",deviceInfoBean.getVin());
                                    intent.putExtra("deviceId",deviceInfoBean.getId());
                                    intent.putExtra("fenceState",deviceInfoBean.getFenceState());
                                    startActivity(intent);
                                }
                            });
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"异常:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        swipeToLoadLayout.setRefreshing(false);
                        swipeToLoadLayout.setLoadingMore(false);
                        scheduleDismiss();
                        break;
                }
            }
        });
    }
//    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
//        @Override
//        public boolean onGroupClick(ExpandableListView parent, View v,
//                                    int groupPosition, long id) {
//            if (expandFlag == -1) {
//                // 展开被选的group
//                recyclerView.expandGroup(groupPosition);
//                // 设置被选中的group置于顶端
//                recyclerView.setSelectedGroup(groupPosition);
//                expandFlag = groupPosition;
//            } else if (expandFlag == groupPosition) {
//                recyclerView.collapseGroup(expandFlag);
//                expandFlag = -1;
//            } else {
//                recyclerView.collapseGroup(expandFlag);
//                // 展开被选的group
//                recyclerView.expandGroup(groupPosition);
//                // 设置被选中的group置于顶端
//                recyclerView.setSelectedGroup(groupPosition);
//                expandFlag = groupPosition;
//            }
//            return true;
//        }
//    }
    @Override
    public void queryFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                Toast.makeText(Monitor2Activity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

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
