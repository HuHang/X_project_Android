package com.iftech.car.trace.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.common.Constant;
import com.iftech.car.common.MyApplication;
import com.iftech.car.trace.adapter.IndexRecycleAdapter;
import com.iftech.car.trace.bean.ShopBean;
import com.iftech.car.trace.bean.ReportBean;
import com.iftech.car.trace.bean.UserInfoBean;
import com.iftech.car.trace.presenter.IndexPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.activity.*;
import com.iftech.car.trace.ui.view.IndexView;
import com.iftech.car.utils.SpUtils;
import com.iftech.car.utils.SpacesItemDecoration;
import com.iftech.car.widget.EmptyRecyclerView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
/**
 * 首页implements recyclerView.PullLoadMoreListener
 * **/
public class IndexFragment extends Fragment implements IndexView,AppBarLayout.OnOffsetChangedListener, OnRefreshListener, OnLoadMoreListener {

    @InjectView(R.id.shopTV)
    TextView shopTV;
    @InjectView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;

    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;
    @InjectView(R.id.gridLayout)
    GridLayout gridLayout;
    @InjectView(R.id.monitor_ll)
    LinearLayout monitor_ll;
    @InjectView(R.id.car_ll)
    LinearLayout car_ll;
    @InjectView(R.id.device_ll)
    LinearLayout device_ll;
    @InjectView(R.id.inventory_ll)
    LinearLayout inventory_ll;
    /**第二层菜单*/
    @InjectView(R.id.bind_img)
    ImageView bind_img;
    @InjectView(R.id.relieve_img)
    ImageView relieve_img;
    @InjectView(R.id.more_img)
    ImageView more_img;

    @InjectView(R.id.swipe_target)
    EmptyRecyclerView recyclerView;
    @InjectView(R.id.id_empty_view)
    RelativeLayout id_empty_view;

    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @InjectView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private final static String TAG = "IndexFragment";

    private final static int DATE_SHOP = 10129;
    private final static int DATE_SUB = 10123;

    private IndexRecycleAdapter adapter ;
    private String ids = "";
    private String names = "";
    private String shopNames ;
    private String shopIds;
    private IBasePresenter iIndexPresenter;
    private List<ReportBean> list1 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.inject(this, view);
        iIndexPresenter = new IndexPresenter(this);
        if(!"".equals(SpUtils.getStringSp(getContext(),Constant.userInfoSP,Constant.shopName))){
            shopTV.setText(SpUtils.getStringSp(getContext(), Constant.userInfoSP,Constant.shopName));
        }
        initView();
        initEvents();
        return view;
    }

    private void initView(){
        UserInfoBean userInfoBean = new Gson().fromJson(SpUtils.getStringSp(getContext(),Constant.userInfoSP,Constant.userInfo),UserInfoBean.class);
        if(userInfoBean.getUserInfo().getRoleType() == 100){
            relieve_img.setVisibility(View.VISIBLE);
        }
        if(userInfoBean.getUserInfo().getRoleType() == 100 || userInfoBean.getUserInfo().getRoleType() == 300
                || userInfoBean.getUserInfo().getRoleType() ==400){
            bind_img.setVisibility(View.VISIBLE);
        }
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        shopNames = SpUtils.getStringSp(getContext(),Constant.userInfoSP,Constant.shopName);
        shopIds = SpUtils.getStringSp(getContext(),Constant.userInfoSP,Constant.shopIds);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(16,false));
        recyclerView.setEmptyView(id_empty_view);
        adapter = new IndexRecycleAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getShops();
        autoRefresh();

    }


//获取所有4s店
    private void getShops(){
        String url = MainTabActivity.ip+ Api.getAllShops;
        iIndexPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,DATE_SHOP);
    }

//获取dashboard
    private void getData(int type){
        String url ;
//        if(type == DATE_SUB){
            url = MainTabActivity.ip+ Api.getDashBoard;
//        }
        Log.d(TAG,url);
        iIndexPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,type);
    }
    private void initEvents(){

        shopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "shopTV=========: "+shopIds);
                Intent intent = new Intent(getActivity(),ShopActivity.class);
                intent.putExtra("isSimple",false);
                intent.putExtra("shopIds",shopIds);
                startActivityForResult(intent,Constant.SHOP_RETURN);
            }
        });
        adapter.setOnItemClickListener(new IndexRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,ReportBean reportBean) {
                Intent intent = new Intent(getActivity(),ChartActivity.class);
                intent.putExtra("url",reportBean.getUrl());
                intent.putExtra("title",reportBean.getHeader());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    public void onRefresh() {
        getData(DATE_SUB);
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
    }

    private void autoRefresh() {
        swipeToLoadLayout.setRefreshing(true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        swipeToLoadLayout.setEnabled(verticalOffset == 0);
        swipeToLoadLayout.setRefreshEnabled(verticalOffset == 0);
        if(swipeToLoadLayout.isRefreshing() ){
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && this.getContext() != null) {
            MyApplication application = (MyApplication) this.getContext().getApplicationContext();
            if (application.appBarLayout != null) {
                application.appBarLayout.addOnOffsetChangedListener(this);
            }
        }else if (isVisibleToUser && this.getContext() == null) {
            //viewpager中第一页加载的太早,getContext还拿不到,做个延迟
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (IndexFragment.this.getContext() != null) {

                        MyApplication application = (MyApplication) IndexFragment.this.getContext().getApplicationContext();
                        if (application.appBarLayout != null) {
                            application.appBarLayout.addOnOffsetChangedListener(IndexFragment.this);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        app_bar_layout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(! hidden){
            autoRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        app_bar_layout.removeOnOffsetChangedListener(this);
    }

    @OnClick ({R.id.monitor_ll,R.id.car_ll,R.id.device_ll,R.id.bind_img,R.id.relieve_img,R.id.more_img})
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.monitor_ll:
                intent.putExtra("shopId",shopIds);
                intent.putExtra("shopName",shopNames);
                intent.setClass(getActivity(), Monitor2Activity.class);
                break;
            case R.id.car_ll:
                intent.putExtra("shopId",shopIds);
                intent.putExtra("shopName",shopNames);
                intent.setClass(getActivity(), CarInfoActivity.class);
                break;
            case R.id.device_ll:
                intent.putExtra("shopId",shopIds);
                intent.putExtra("shopName",shopNames);
                intent.setClass(getActivity(), DeviceInfoActivity.class);
                break;
//            case R.id.inventory_ll:
//                Toast.makeText(getContext(),"敬请期待",Toast.LENGTH_SHORT).show();
//                intent.setClass(getActivity(), InventoryTabActivity.class);
//                break;
            case R.id.bind_img:
                intent.setClass(getActivity(), BindDeviceActivity.class);
                break;
            case R.id.relieve_img:
                intent.putExtra("shopId",shopIds);
                intent.putExtra("shopName",shopNames);
                intent.setClass(getActivity(), RelieveDeviceActivity.class);
                break;
            case R.id.more_img:
                intent.setClass(getActivity(),MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.SHOP_RETURN){
            if(data != null){
                Log.d(TAG,data.getStringExtra("names"));
                Log.d(TAG,data.getStringExtra("ids"));
                shopIds = data.getStringExtra("ids");
                shopNames = data.getStringExtra("names");
                shopTV.setText(data.getStringExtra("names"));
            }
        }
    }

    @Override
    public void querySuccess(final String msg,final int type) {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (type){
                        case DATE_SUB:
                            list1.clear();
                            if (!"".equals(msg) && null != msg){
                                try{
                                    Gson gson  = new Gson();
                                    JSONArray jsonArray = new JSONArray(msg);
                                    for (int i=0 ;i<jsonArray.length();i++){
                                        ReportBean reportBean = gson.fromJson(jsonArray.get(i).toString(),ReportBean.class);
                                        if(reportBean.isOpen()){
                                            list1.add(reportBean);
                                        }
                                    }
                                    adapter.setData(list1);
                                }catch (JSONException e){
                                    Toast.makeText(getContext(),"发生异常，请重试！",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            swipeToLoadLayout.setRefreshing(false);
                            break;
                        case DATE_SHOP:
                            Log.d(TAG, "run: 请求了商店");
                            if (!"".equals(msg) && null != msg){
                                try {
                                    JSONArray parentItem = new JSONArray(msg);
                                    Gson gson = new Gson();
                                    List<ShopBean> shopList = new ArrayList<>();

                                    for(int i=0;i<parentItem.length();i++){
                                        ShopBean shop = gson.fromJson(parentItem.get(i).toString(),ShopBean.class);
                                        ids += shop.getId()+",";
                                        names += shop.getName()+" ";
                                        for(ShopBean childShopBean : shop.getChildShops() ){
                                            ids += childShopBean.getId()+",";
                                            names += childShopBean.getName()+" ";
                                        }

                                    }
                                        ids = ids.substring(0,ids.length()-1); // 去掉最后一个逗号,
                                        ids = "["+ids+"]";
                                    shopIds = ids;
                                    shopNames = names;
                                    shopTV.setText(shopNames);
//                                    SpUtils.setStringSp(getContext(),Constant.userInfoSP,Constant.shopIds,ids);
//                                    SpUtils.setStringSp(getContext(),Constant.userInfoSP,Constant.shopName,names);
                                    Log.d(TAG, "run: "+ids);

                                }catch (JSONException e){
                                    Toast.makeText(getContext(),"发生异常，请重试！",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                            break;
                        default:
                            break;
                    }
                }

            });
        }
    }

    @Override
    public void queryFail(final String msg) {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"destroy");
        super.onDestroy();
    }
}
