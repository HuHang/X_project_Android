package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.iftech.car.trace.adapter.CarInfoAdapter;
import com.iftech.car.trace.bean.CarInfoBean;
import com.iftech.car.trace.bean.CarOrDeviceStatusBean;
import com.iftech.car.trace.presenter.CarInfoPresenter;
import com.iftech.car.trace.presenter.iface.ICarDeviceInfoPresenter;
import com.iftech.car.trace.ui.view.CarInfoView;
import com.iftech.car.utils.SpacesItemDecoration;
import com.iftech.car.widget.EmptyRecyclerView;
import com.iftech.car.widget.MySearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import net.sf.json.JSONObject;

/**
 * 车辆信息界面
 * **/
public class CarInfoActivity extends AppCompatActivity implements CarInfoView ,OnRefreshListener, OnLoadMoreListener {

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
    @InjectView(R.id.searchView)
    MySearchView searchView;
    @InjectView(R.id.id_empty_view)
    RelativeLayout id_empty_view;
    private ICarDeviceInfoPresenter carInfoPresenter;
    private final static String TAG = "CarInfoActivity";

    private String isBind = "true";
    private String shopId ;
    private int pageIndex = 0;
    private List<CarInfoBean> list;
    private List<String> listMenu;
    private CarInfoAdapter adapter ;
    private PopupWindow popupWindow = null;
    private boolean isSearch = false;

    private final static int QUERY_DATA = 10126;
    private final static int QUERY_MENU = 10125;

    String searchStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this);
        toolbar_title.setText("已绑定");
        toolbar_title.setClickable(false);
        shopId = getIntent().getStringExtra("shopId");
        carInfoPresenter = new CarInfoPresenter(this);
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
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(16,true));
        recyclerView.setEmptyView(id_empty_view);
        adapter = new CarInfoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        autoRefresh();
    }

    private void initEvents(){

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

        adapter.setOnItemClickListener(new CarInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,CarInfoBean carInfoBean) {
                if(carInfoBean.getImei() != null && !carInfoBean.getImei().equals("")){
                    Intent intent = new Intent(CarInfoActivity.this,TrackingActivity.class);
                    intent.putExtra("vin",carInfoBean.getVin());
                    intent.putExtra("deviceId",carInfoBean.getDeviceId());
                    intent.putExtra("fenceState",carInfoBean.getFenceState());
                    startActivity(intent);
                }else{
                    Toast.makeText(CarInfoActivity.this,"该车未绑定设备，无法跟踪！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        if(!toolbar_title.isClickable()){
            Toast.makeText(getApplicationContext(),"请等待加载完成",Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        JSONObject param = new JSONObject();
        try {
            param.put("shopIds",shopId);
            String url = MainTabActivity.ip+Api.getGroupCarsCountByShopList;
            carInfoPresenter.queryJsonPost(url,MainTabActivity.username,MainTabActivity.password,param,QUERY_MENU);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void showPopupWindow(){
//        final List<String> list1 = new ArrayList<>();
//        list1.add("绑定车辆");
//        list1.add("未绑车辆");
//        list1.add("全部");
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(CarInfoActivity.this).inflate(R.layout.infowindow_select_menu, null);
        ListView listView = (ListView) bubbleLayout.findViewById(R.id.menu_list);
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter(CarInfoActivity.this,R.layout.infowindow_menu_item,R.id.item_tv,listMenu);
        listView.setAdapter(arrayAdapter);

        popupWindow = new PopupWindow(CarInfoActivity.this);
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
                    isBind = "true";
                }else if(position == 1){
                    isBind = "false";
                }else{
                    isBind = "";
                }
                toolbar_title.setText(listMenu.get(position));
                getData();
                popupWindow.dismiss();
            }
        });
    }

    private void getData(){
        String url = "";
        JSONObject param = new JSONObject();
        try{
            param.put("isBind",isBind);
            param.put("pageIndex",pageIndex);
            if(isSearch){
                param.put("searchStr",searchStr);
                url = MainTabActivity.ip+Api.searchCarsByShopList;
            }else{
                url = MainTabActivity.ip+ Api.getAllCarsByShopList;
            }

            param.put("shopIds",shopId);
            carInfoPresenter.queryJsonPost(url,MainTabActivity.username,MainTabActivity.password,param,QUERY_DATA);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void querySuccess( final String msg, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                switch (type){
                    case QUERY_DATA:
                        Gson gson = new Gson();
                        JSONArray array = new JSONArray(msg);
                        for (int i= 0; i<array.length();i++){
                            CarInfoBean carInfoBean = gson.fromJson(array.get(i).toString(),CarInfoBean.class);
                            list.add(carInfoBean);
                        }
                        adapter.setData(list);
                        adapter.notifyDataSetChanged();
                        swipeToLoadLayout.setLoadingMore(false);
                        swipeToLoadLayout.setRefreshing(false);
                        break;
                    case QUERY_MENU:
                        Gson gson1 = new Gson();
                        listMenu = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(msg);
                        for(int i= 0; i<jsonArray.length();i++){
                            CarOrDeviceStatusBean carOrDeviceStatusBean = gson1.fromJson(jsonArray.get(i).toString(),CarOrDeviceStatusBean.class);
                            listMenu.add(carOrDeviceStatusBean.getName()+"("+carOrDeviceStatusBean.getCount()+"台)");
                        }
                        toolbar_title.setClickable(true);
                        toolbar_title.setText(listMenu.get(0));
                        break;
                }
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(),"异常"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void queryFail(final String msg) {
        Log.d(TAG,msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                swipeToLoadLayout.setRefreshing(false);
                Toast.makeText(CarInfoActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isBind = "true";
    }
}
