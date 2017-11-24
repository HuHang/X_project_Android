package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.bean.ResultBean;
import com.iftech.car.trace.presenter.RelievePresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.BindDeviceView;
import com.iftech.car.trace.ui.view.RelieveDeviceView;
import com.iftech.car.utils.NormalDialog;
import com.iftech.car.utils.PermissionUtils;
import com.iftech.car.utils.SpUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.iftech.car.utils.PermissionUtils.PERMISSIONCODE;

/**
 * 解绑界面
 * **/
public class RelieveDeviceActivity extends AppCompatActivity implements RelieveDeviceView,BindDeviceView{

    private final static String TAG = "RelieveDeviceActivity";

    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
//    @InjectView(R.id.toolbar_subtitle)
//    TextView toolbar_subtitle;

    @InjectView(R.id.inputValue)
    AutoCompleteTextView inputValue;
    @InjectView(R.id.scanImg)
    ImageView scanImg;
    @InjectView(R.id.commitRelieve)
    TextView commitRelieve;
    @InjectView(R.id.ll_progress_bar)
    LinearLayout ll_progress_bar;
    @InjectView(R.id.progress_bar)
    ImageView progress_bar;
    Animation animation;


    private IBasePresenter relievePresenter;
    private final static int RELIVE_DEVICE = 1005;
    private final static int SEARCH_VIN_IMEI = 1009;

    private List<String > list;
    private ArrayAdapter <String> arrayAdapter = null;

    private String shopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relieve_device);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this);
        toolbar_title.setText(getString(R.string.relieve));
        initView();
        initEvents();
    }

    private void initView(){
//        toolbar_subtitle.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        shopId = SpUtils.getStringSp(RelieveDeviceActivity.this,Constant.userInfoSP,Constant.shopId);
        inputValue.setThreshold(1);
        list = new ArrayList<>();
        relievePresenter = new RelievePresenter(this);
        PermissionUtils.checkPermission(this,0);
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 其实这里什么都不要做
        super.onConfigurationChanged(newConfig);
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.checkPermission(RelieveDeviceActivity.this,0);
                Intent intent1 = new Intent(RelieveDeviceActivity.this, CaptureActivity.class);
                startActivityForResult(intent1, Constant.REQUEST_SUCCESS);
            }
        });

        commitRelieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog normalDialog = new NormalDialog(RelieveDeviceActivity.this);
                normalDialog.showNormalDialog("确定解绑该设备吗？");
                normalDialog.setDialogListener(new NormalDialog.DialogListener() {
                    @Override
                    public void enSureClock() {
                        ll_progress_bar.setVisibility(View.VISIBLE);
                        progress_bar.setAnimation(animation);
                        String url = MainTabActivity.ip+Api.relieveVin+inputValue.getText().toString();
                        Log.d(TAG,url);
                        relievePresenter.queryPost(url,MainTabActivity.username,MainTabActivity.password,RELIVE_DEVICE);
                    }
                });
            }
        });

        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryVinOrImei(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        toolbar_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RelieveDeviceActivity.this,ShopActivity.class);
//                intent.putExtra("isSimple",true);
//                startActivityForResult(intent,Constant.SHOP_RETURN);
//            }
//        });
    }

    private void queryVinOrImei(String s){
        String url =MainTabActivity.ip+Api.queryImeiOrVin+"?shopId="+"&searchStr="+s+"&searchType=";
        Log.d(TAG,url);
        relievePresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,SEARCH_VIN_IMEI);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意了操作权限
                Log.d("权限通过","");
            } else {
                //用户拒绝了操作权限
                Log.d("权限拒绝","");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SUCCESS) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                        inputValue.setText(result.trim());
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(RelieveDeviceActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (requestCode == Constant.SHOP_RETURN){
            if (null != data) {
                shopId = data.getStringExtra("ids");
//                toolbar_title.setText(data.getStringExtra("names"));
            }
        }
    }

    @Override
    public void querySuccess(final String msg, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case RELIVE_DEVICE:
                        ResultBean resultBean = new Gson().fromJson(msg,ResultBean.class);
                        if (resultBean.isSuccess()){
                            inputValue.setText("");
                            Toast.makeText(RelieveDeviceActivity.this,"解绑成功！",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RelieveDeviceActivity.this,resultBean.getContent(),Toast.LENGTH_SHORT).show();
                        }
                        ll_progress_bar.setVisibility(View.GONE);
                        progress_bar.clearAnimation();
                        break;
                    case SEARCH_VIN_IMEI:
                        list.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(msg);
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("data");
                                for( int j = 0;j<jsonArray1.length();j++){
                                    list.add(jsonArray1.get(j).toString());
                                }
                            }
                            if(arrayAdapter!=null){
                                arrayAdapter.clear();
                            }
                            arrayAdapter = new ArrayAdapter(RelieveDeviceActivity.this,R.layout.infowindow_vin_imei_item,list);
                            inputValue.setAdapter(arrayAdapter);
                            arrayAdapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"异常:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;
                }

            }
        });
    }

    @Override
    public void queryFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_progress_bar.setVisibility(View.GONE);
                progress_bar.clearAnimation();
                Toast.makeText(RelieveDeviceActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
