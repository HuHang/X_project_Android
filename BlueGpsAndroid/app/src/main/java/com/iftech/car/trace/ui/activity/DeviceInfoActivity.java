package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.daasuu.bl.BubbleLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.trace.adapter.DeviceInfoAdapter;
import com.iftech.car.trace.bean.CarOrDeviceStatusBean;
import com.iftech.car.trace.bean.DeviceInfoBean;
import com.iftech.car.trace.presenter.DevicesInfoPresenter;
import com.iftech.car.trace.presenter.iface.ICarDeviceInfoPresenter;
import com.iftech.car.trace.ui.view.DeviceInfoView;
import com.iftech.car.utils.SpacesItemDecoration;
import com.iftech.car.widget.EmptyRecyclerView;
import com.iftech.car.widget.MySearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息界面
 * **/
public class DeviceInfoActivity extends AppCompatActivity implements DeviceInfoView,OnRefreshListener, OnLoadMoreListener {

    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @InjectView(R.id.swipe_target)
    EmptyRecyclerView recyclerView;
    @InjectView(R.id.id_empty_view)
    RelativeLayout id_empty_view;
    @InjectView(R.id.searchView)
    MySearchView searchView;
    private boolean isSearch = false;
    private String isUsed = "true";
    private int pageIndex = 0;
    private String shopId;
    private DeviceInfoAdapter adapter;
    private List<DeviceInfoBean> list;
    private List<String> listMenu;
    private PopupWindow popupWindow = null;
    private String TAG = "DeviceInfoActivity";

    private final static int QUERY_DATA = 10127;
    private final static int QUERY_MENU = 10128;
    private ICarDeviceInfoPresenter devicesInfoPresenter;
    String searchStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this);
        shopId = getIntent().getStringExtra("shopId");
        toolbar_title.setText("使用中");
        toolbar_title.setClickable(false);
        devicesInfoPresenter = new DevicesInfoPresenter(this);
        initView();
        initEvents();
        getMenu();
    }

    private void initView(){
        list = new ArrayList<>();
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        back.setVisibility(View.VISIBLE);
        toolbar_subtitle.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceInfoAdapter(this);
        recyclerView.setEmptyView(id_empty_view);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(16,true));
        adapter.notifyDataSetChanged();
        autoRefresh();
    }

    private void initEvents(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        searchView.setOnSearchListener(new MySearchView.OnSearchListener() {
            @Override
            public void searchClick(TextView view) {
                searchStr = view.getText().toString();
                isSearch = true;
                autoRefresh();
            }
        });

        adapter.setOnItemClickListener(new DeviceInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,DeviceInfoBean deviceInfoBean) {
                if(deviceInfoBean.getVin()!= "" && deviceInfoBean.getVin() != null){
                    Intent intent = new Intent(DeviceInfoActivity.this,TrackingActivity.class);
                    Log.d(TAG,"DeviceId="+deviceInfoBean.getId());
                    intent.putExtra("deviceId",deviceInfoBean.getId());
                    intent.putExtra("vin",deviceInfoBean.getVin());
                    intent.putExtra("fenceState",deviceInfoBean.getFenceState());
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DeviceInfoActivity.this,DeviceMapActivity.class);
                    Log.d(TAG,"DeviceId="+deviceInfoBean.getId());
                    intent.putExtra("deviceId",deviceInfoBean.getId());
                    intent.putExtra("imei",deviceInfoBean.getImei());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        if(!toolbar_title.isClickable()){
            Toast.makeText(getApplicationContext(),"请等待加载完成",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRefresh() {
        list.clear();
        pageIndex = 0;
        getData();
    }

    @Override
    public void onLoadMore() {
        pageIndex = pageIndex + 1;
        getData();
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
    private void getMenu(){
        String url = MainTabActivity.ip + Api.getGroupDevicesCountByShopList;
        try {
            JSONObject param = new JSONObject();
            param.put("shopIds",shopId);
            devicesInfoPresenter.queryJsonPost(url,MainTabActivity.username,MainTabActivity.password,param,QUERY_MENU);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void showPopupWindow(){
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(DeviceInfoActivity.this).inflate(R.layout.infowindow_select_menu, null);
        ListView listView = (ListView) bubbleLayout.findViewById(R.id.menu_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(DeviceInfoActivity.this,R.layout.infowindow_menu_item,R.id.item_tv,listMenu);
        listView.setAdapter(arrayAdapter);

        popupWindow = new PopupWindow(DeviceInfoActivity.this);
        popupWindow.setContentView(bubbleLayout);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAsDropDown(toolbar_title);
        popupWindow.setOutsideTouchable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                pageIndex = 0;
                if(position == 0){
                    isUsed = "true";
                }else if(position == 1){
                    isUsed = "false";
                }else{
                    isUsed = "";
                }
                toolbar_title.setText(listMenu.get(position));
                getData();
                popupWindow.dismiss();
            }
        });
    }

    private void getData(){
        String url ;
        JSONObject param = new JSONObject();
        try {
            param.put("isBind",isUsed);
            param.put("pageIndex",pageIndex);
            if(isSearch){
                param.put("searchStr",searchStr);
                url = MainTabActivity.ip + Api.searchDevicesByShopList;
            }else{
                url = MainTabActivity.ip+ Api.getAllDevicesByShopList;
            }
            param.put("shopIds",shopId);
            devicesInfoPresenter.queryJsonPost(url,MainTabActivity.username,MainTabActivity.password,param,QUERY_DATA);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void querySuccess(final String msg, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    switch (type){
                        case QUERY_DATA:
                            try{
                                JSONArray array = new JSONArray(msg);
                                for (int i= 0; i<array.length();i++){
                                    DeviceInfoBean deviceInfoBean = gson.fromJson(array.get(i).toString(),DeviceInfoBean.class);
                                    list.add(deviceInfoBean);
                                }
                                adapter.setData(list);
                                adapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
                            break;
                        case QUERY_MENU:
                            listMenu = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(msg);
                            for(int i= 0; i<jsonArray.length();i++){
                                CarOrDeviceStatusBean carOrDeviceStatusBean = gson.fromJson(jsonArray.get(i).toString(),CarOrDeviceStatusBean.class);
                                listMenu.add(carOrDeviceStatusBean.getName()+"("+carOrDeviceStatusBean.getCount()+"台)");
                            }
                            toolbar_title.setClickable(true);
                            toolbar_title.setText(listMenu.get(0));
                            break;
                    }
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(),"异常:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void queryFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                Toast.makeText(DeviceInfoActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
