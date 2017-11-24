package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.adapter.ShopAdapter;
import com.iftech.car.trace.bean.ShopBean;
import com.iftech.car.trace.presenter.ShopPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.ShopView;
import com.iftech.car.utils.CharacterParser;
import com.iftech.car.widget.MySearchView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements ShopView{

    @InjectView(R.id.toolbar_right)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.allSelect)
    TextView allSelect;

    @InjectView(R.id.shop_expandListView)
    ExpandableListView shop_expandListView;
    @InjectView(R.id.sidebar)
    WaveSideBar sidebar;
    @InjectView(R.id.searchView)
    MySearchView searchView;

    Animation animation;

    private KProgressHUD hud;
    private ShopAdapter shopAdapter;
    private List<ShopBean> shopList = new ArrayList<>();
    private List<ShopBean> selectShops = new ArrayList<>();
    private List<ShopBean> searchList = new ArrayList<>();
    private List<ShopBean> shopCheckedList = new ArrayList<>();
    private CharacterParser characterParser;
    private String ids = "";
    private String names ="";
    private boolean isSimple = true;
    private IBasePresenter shopPresenter;
    private final static int QUERY_DATA = 10129;
    private final static String TAG = "ShopActivity";

    private String shopIds ;
    private String shopId;

    private String[] shopArray ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shopPresenter = new ShopPresenter(this);
        ButterKnife.inject(this);
        toolbar_title.setText(getString(R.string.selectShop));
        isSimple = getIntent().getBooleanExtra("isSimple",true);
        shopIds = getIntent().getStringExtra("shopIds");
        shopId = getIntent().getStringExtra("shopId");
        initView();
        initEvents();
        getShops();
    }
    private void initView (){
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("获取中...")
                .setCancellable(false);

        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        shop_expandListView = (ExpandableListView) findViewById(R.id.shop_expandListView);
        sidebar = (WaveSideBar) findViewById(R.id.sidebar);

        // 处理已经选中的4S店
        if(!isSimple){
           shopIds = shopIds.replace("[","");
           shopIds = shopIds.replace("]","");
           shopArray = shopIds.split(",");
        }
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "resultReturn: ===="+selectShops.size());
                resultReturn();
                finish();
            }
        });

        searchView.setOnSearchListener(new MySearchView.OnSearchListener() {
            @Override
            public void searchClick(TextView view) {
                searchList.clear();
                for (ShopBean shopBean :shopList){
                    if(shopBean.getName().contains(view.getText().toString())){
                        searchList.add(shopBean);
                    }
                }
                showShops(searchList);
            }
        });

        if (isSimple){
            allSelect.setVisibility(View.GONE);
        }else {
            allSelect.setOnClickListener(new  View.OnClickListener(){
                @Override
                public void onClick(View v){
                    shopAdapter  = new ShopAdapter(getApplicationContext(),shopList,isSimple);
                    if (!allSelect.isSelected()){
                        allSelect.setSelected(true);
                        allSelect.setText("全不选");
                        for (ShopBean shopBean :shopList){
                            shopBean.setChecked(true);
                            if (!shopCheckedList.contains(shopBean)){
                                shopCheckedList.add(shopBean);
                            }
                            for (ShopBean childShop :shopBean.getChildShops()){
                                childShop.setChecked(true);
                                if (!shopCheckedList.contains(childShop)){
                                    shopCheckedList.add(childShop);
                                }
                            }

                        }
                    }else {
                        allSelect.setSelected(false);
                        allSelect.setText("全选");
                        for (ShopBean shopBean :shopList){
                            shopBean.setChecked(false);
                            if (shopCheckedList.contains(shopBean)){
                                shopCheckedList.remove(shopBean);
                            }

                            for (ShopBean childShop :shopBean.getChildShops()){
                                childShop.setChecked(false);
                                if (shopCheckedList.contains(childShop)){
                                    shopCheckedList.remove(childShop);
                                }
                            }
                        }

                    }
                    shopAdapter.setShopChecked(shopCheckedList);
                    shop_expandListView.setAdapter(shopAdapter);
                }
            });
        }


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

    // 结果返回
    private void resultReturn(){
        Intent resultIntent = new Intent();

        if(selectShops.size()>0){
            for(ShopBean shop : selectShops){
                if(!isSimple){
                    ids += shop.getId()+",";
                    names += shop.getName()+" ";
                }else {
                    shopId = shop.getId()+"";
                    names = shop.getName();
                }
            }

            if(!isSimple){
                ids = ids.substring(0,ids.length()-1); // 去掉最后一个逗号,
                ids = "["+ids+"]";
                resultIntent.putExtra("ids",ids);
            }else{
                resultIntent.putExtra("shopId",shopId);
            }
            resultIntent.putExtra("names",names);
            setResult(Constant.SHOP_RETURN,resultIntent);
        }
    }

    private void showShops(final List<ShopBean> list){
        ShopAdapter.setData(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shopAdapter  = new ShopAdapter(getApplicationContext(),list,isSimple);

                shopAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        Log.d(TAG, "onChanged: registerDataSetObserver");
                        super.onChanged();
                        selectShops = shopAdapter.getShopChecked();
                        for(ShopBean shopBean :shopCheckedList){
                            if(!selectShops.contains(shopBean)){
                                shopCheckedList.remove(shopBean);
                            }
                        }
                    }
                });
                shopAdapter.setShopChecked(shopCheckedList);
                shop_expandListView.setAdapter(shopAdapter);
                if(!isSimple){
                    for (int i = 0; i < shopAdapter.getGroupCount(); i++){
                        shop_expandListView.expandGroup(i);
                    }
                }
                shopAdapter.notifyDataSetChanged();
            }
        });
        //索引
        sidebar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                int position = shopAdapter.getGroupPositionForSection(index.charAt(0));
                if(position != -1){
                    ShopBean shopBean = shopAdapter.getGroup(position);
                    if(shopBean.getSortLetters().equals(index)){
                        shop_expandListView.setSelection(position);
                        shop_expandListView.setSelectedGroup(position);
                    }
                }


            }
        });
    }

    private void getShops(){
        hud.show();
        String url = MainTabActivity.ip+ Api.getAllShops;
        shopPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,QUERY_DATA);
    }

    @Override
    public void querySuccess(final String msg, int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shopList = new ArrayList<>();
                try {
                    JSONArray parentItem = new JSONArray(msg);
                    Gson gson = new Gson();
                    for(int i=0;i<parentItem.length();i++){
                        ShopBean shop = gson.fromJson(parentItem.get(i).toString(),ShopBean.class);
                        if(isSimple){
                            if(shopId.equals(shop.getId()+"")){
                                shop.setChecked(true);
                                shopCheckedList.add(shop);
                            }
                            for(ShopBean childShopBean : shop.getChildShops() ){
                                if(shopId.equals( childShopBean.getId()+"")){
                                    childShopBean.setChecked(true);
                                    shopCheckedList.add(childShopBean);
                                }
                            }
                        }else{
                            for(int j = 0;j < shopArray.length ;j ++){
                                if(shopArray[j].equals(shop.getId()+"")){
                                    shop.setChecked(true);
                                    shopCheckedList.add(shop);
                                }
                                for(ShopBean childShopBean : shop.getChildShops() ){
                                    if(shopArray[j].equals( childShopBean.getId()+"")){
                                        childShopBean.setChecked(true);
                                        shopCheckedList.add(childShopBean);
                                    }
                                }
                            }
                        }
                        String pinyin = characterParser.getSelling(shop.getName());
                        String sortString = pinyin.substring(0, 1).toUpperCase();
                        // 正则表达式，判断首字母是否是英文字母
                        if (sortString.matches("[A-Z]")) {
                            shop.setSortLetters(sortString.toUpperCase());
                        } else {
                            shop.setSortLetters("#");
                        }
                        shopList.add(shop);
                    }
                    showShops(shopList);
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(),"异常:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                scheduleDismiss();
            }
        });
    }

    @Override
    public void queryFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scheduleDismiss();
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        resultReturn();
        this.finish();
        super.onBackPressed();
    }
}
