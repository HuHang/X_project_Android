package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.bean.ResultBean;
import com.iftech.car.trace.presenter.BindDevicePresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.BindDeviceView;
import com.iftech.car.utils.HttpConnect;
import com.iftech.car.utils.PermissionUtils;
import com.iftech.car.utils.SpUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iftech.car.utils.PermissionUtils.PERMISSIONCODE;

/**
 * 绑定设备界面
 * **/
public class BindDeviceActivity extends AppCompatActivity implements TakePhoto.TakeResultListener,InvokeListener ,BindDeviceView{

    private final static String TAG = "BindDeviceActivity";

    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;

    @InjectView(R.id.scanVinImg)
    ImageView scanVinImg;
    @InjectView(R.id.scanIMeiImg)
    ImageView scanIMeiImg;
    @InjectView(R.id.VinET)
    AutoCompleteTextView vinET;
    @InjectView(R.id.IMeiET)
    TextView iMeiET;
    @InjectView(R.id.image1)
    ImageView image1;
    @InjectView(R.id.image2)
    ImageView image2;
    @InjectView(R.id.ll_progress_bar)
    LinearLayout ll_progress_bar;
    @InjectView(R.id.progress_bar)
    ImageView progress_bar;
    Animation animation;

    private boolean isVin = false;
    private String shopId;

    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private final static int CHECK_VIN = 1006;
    private final static int BIND_DEVICE = 1007;
    private final static int SEARCH_IMEI_VIN = 1008;
    private IBasePresenter bindDevicePresenter;

    private boolean isFirst = true;
    private boolean isShowToase = true;
    private List<String > list;
    private ArrayAdapter <String> arrayAdapter = null;
    private AutoCompleteTextView editText;
    private String images1;
    private String images2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
        bindDevicePresenter = new BindDevicePresenter(this);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this);
        if(!"".equals(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.shopName))){
            toolbar_title.setText(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.shopName));
        }else{
            toolbar_title.setText(R.string.selectShop);
        }
        initView();
        initEvents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 其实这里什么都不要做
        super.onConfigurationChanged(newConfig);
    }
    private void initView(){
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        vinET.setThreshold(1);
        list = new ArrayList<>();
        shopId = SpUtils.getStringSp(this,Constant.userInfoSP,Constant.shopId);
        back.setVisibility(View.VISIBLE);
        toolbar_subtitle.setVisibility(View.GONE);
        PermissionUtils.checkPermission(this,0);

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
                Intent intent = new Intent(BindDeviceActivity.this,ShopActivity.class);
                intent.putExtra("isSimple",true);
                intent.putExtra("shopId",shopId);
                startActivityForResult(intent,Constant.SHOP_RETURN);
            }
        });

        vinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryVinOrImei(s.toString(),false);
                editText = vinET;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iMeiET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isShowToase){
                    if(TextUtils.isEmpty(vinET.getText().toString())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BindDeviceActivity.this,"请先输入或扫描VIN号",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        String url = MainTabActivity.ip+Api.checkVin+"?shopId="+shopId+"&imei="+iMeiET.getText().toString()+"&vin="+vinET.getText().toString();
                        bindDevicePresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,CHECK_VIN);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void queryVinOrImei(String s, boolean isImei){
        String url ="?shopId="+"&searchStr="+s;
        if(isImei){
            url = MainTabActivity.ip + Api.queryImei+url;
        }else {
            url = MainTabActivity.ip + Api.queryVin+url;
        }
        bindDevicePresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,SEARCH_IMEI_VIN);
    }

    @OnClick({R.id.scanVinImg,R.id.scanIMeiImg,R.id.toolbar_title,R.id.IMeiET})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.toolbar_title:
                Intent intent0 = new Intent(BindDeviceActivity.this,ShopActivity.class);
                startActivity(intent0);
                break;
            case R.id.scanIMeiImg:
            case R.id.IMeiET:
                isVin = false;
                PermissionUtils.checkPermission(this,0);
                Intent intent = new Intent(BindDeviceActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQUEST_SUCCESS);
                break;
            case R.id.scanVinImg:
                isVin = true;
                PermissionUtils.checkPermission(this,0);
                Intent intent1 = new Intent(BindDeviceActivity.this, CaptureActivity.class);
                startActivityForResult(intent1, Constant.REQUEST_SUCCESS);
                break;
        }
    }

    private TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        CompressConfig compressConfig=new CompressConfig.Builder().setMaxSize(300*1024).create();
        takePhoto.onEnableCompress(compressConfig,true);
        return takePhoto;
    }

    private void tackPhoto(){
        try{
            File file=new File(Environment.getExternalStorageDirectory(), "/com.iftech.gps/images/"+System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            Uri imageUri = Uri.fromFile(file);
            getTakePhoto().onPickFromCapture(imageUri);
            if(isFirst){
                Toast.makeText(getApplicationContext(),"请拍摄近景",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"请拍摄远景",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"启动摄像机失败！",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void uploadImages(){
        ll_progress_bar.setVisibility(View.VISIBLE);
        progress_bar.setAnimation(animation);
        HttpConnect httpConnect = new HttpConnect();
        String url = MainTabActivity.ip+Api.bindVin+"?shopId="+shopId+"&imei="+iMeiET.getText().toString()+"&vin="+vinET.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("images1",images1);
        map.put("images2",images2);
        httpConnect.uploadFiles(url, null, map, MainTabActivity.username, MainTabActivity.password, new HttpConnect.HttpConnectCallBack() {
            @Override
            public void onFinish(final String response) {
                Log.d(TAG,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ResultBean resultBean = new Gson().fromJson(response,ResultBean.class);
                            isShowToase = false;
                            vinET.setText("");
                            iMeiET.setText("");
                            image1.setVisibility(View.GONE);
                            image2.setVisibility(View.GONE);
                            isShowToase = true;
                        Toast.makeText(BindDeviceActivity.this,resultBean.getContent(),Toast.LENGTH_SHORT).show();
                        ll_progress_bar.setVisibility(View.GONE);
                        progress_bar.clearAnimation();
                    }
                });
            }

            @Override
            public void onError(final String response) {
                Log.d(TAG,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"异常:"+response,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
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
                    if(isVin){
                        vinET.setText(result.trim());
                    }else{
                        iMeiET.setText(result.trim());
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(BindDeviceActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (resultCode == Constant.SHOP_RETURN){
            if(data != null){
                toolbar_title.setText(data.getStringExtra("names"));
                shopId = data.getStringExtra("shopId");
            }
        }
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
//        KLog.d(result.getImages().size());
//        KLog.debug("拍照成功"+result.getImage().getCompressPath());
        if(isFirst){
            images1 = result.getImage().getCompressPath();
            isFirst = false;
            image1.setVisibility(View.VISIBLE);
            Glide.with(BindDeviceActivity.this).load(new File(result.getImage().getCompressPath())).into(image1);
            tackPhoto();
        }else{
            images2 = result.getImage().getCompressPath();
            isFirst  = true;
            image2.setVisibility(View.VISIBLE);
            Glide.with(BindDeviceActivity.this).load(new File(result.getImage().getCompressPath())).into(image2);
            uploadImages();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void querySuccess(final String msg, final int type) {
        Log.d(TAG,msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case SEARCH_IMEI_VIN:
                            list.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(msg);
                                for(int i = 0; i<jsonArray.length(); i++){
                                    list.add(jsonArray.get(i).toString());
                                }
                                if(arrayAdapter!=null){
                                    arrayAdapter.clear();
                                }
                                arrayAdapter = new ArrayAdapter(BindDeviceActivity.this,R.layout.infowindow_vin_imei_item,list);
                                editText.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                            }catch (JSONException  e){
                                Toast.makeText(BindDeviceActivity.this,"异常:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        break;
                    case CHECK_VIN:
                        ResultBean resultBean = new Gson().fromJson(msg,ResultBean.class);
                        if(resultBean.isSuccess()){
                            tackPhoto();
                        }else{
                            Toast.makeText(BindDeviceActivity.this,resultBean.getContent(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BindDeviceActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
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
}
