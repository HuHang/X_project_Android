package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.bean.UserInfoBean;
import com.iftech.car.trace.presenter.LoginPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.LoginView;
import com.iftech.car.utils.SpUtils;
/**
 * created by tanghuosong 2017/5/1
 * description: 登录界面
 **/
public class LoginActivity extends AppCompatActivity implements LoginView{


    @InjectView(R.id.user_name)
    EditText user_name;
    @InjectView(R.id.user_password)
    EditText user_password;
    @InjectView(R.id.loginBtn)
    Button loginBtn;
    @InjectView(R.id.moreChoice)
    TextView moreChoice;
    @InjectView(R.id.save_password_ck)
    CheckBox save_password_ck;
    private String ipPort;
    private String username ;
    private String password;

    private IBasePresenter loginPresenter;
    private final static String TAG = "LoginActivity";
    private final static int LOGIN = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        Log.d(TAG,"onCreate");
        loginPresenter = new LoginPresenter(this);
        initView();
        initEvents();
    }

    private void initView(){
        if(SpUtils.getStringSp(this, Constant.serverSp,Constant.serverIp) == null ||
                SpUtils.getStringSp(this, Constant.serverSp,Constant.serverIp).equals("")){
            SpUtils.setStringSp(this,Constant.serverSp,Constant.serverIp,"119.254.66.151");
            SpUtils.setStringSp(this,Constant.serverSp,Constant.serverPort,"80");
        }

        if(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.savePassword) != null){
            if(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.savePassword).equals("yes")){
                save_password_ck.setChecked(true);
                user_password.setText(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.userPass));
            }
        }
        user_name.setText(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.userName));
    }

    private void initEvents(){
        save_password_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpUtils.setStringSp(LoginActivity.this,Constant.userInfoSP,Constant.savePassword,"yes");
                }else{
                    SpUtils.setStringSp(LoginActivity.this,Constant.userInfoSP,Constant.savePassword,"no");
                }
            }
        });
    }

    @OnClick({R.id.loginBtn,R.id.moreChoice})
    public void onClick( View view){
        switch (view.getId()){
            case R.id.loginBtn:
                loginBtn.setClickable(false);
                loginBtn.setText("正在登录中");
                ipPort = "http://"+ SpUtils.getStringSp(this, Constant.serverSp,Constant.serverIp)+":"
                        +SpUtils.getStringSp(this, Constant.serverSp,Constant.serverPort);
                username = user_name.getText().toString().trim();
                password = user_password.getText().toString().trim();
                String url = ipPort+ Api.loginWithInfo+"?email="+username+"&pwd="+password;
                loginPresenter.queryPost(url,username,password,LOGIN);
                break;
            case R.id.moreChoice:
                Intent intent = new Intent(this,IpPortSettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void lostUsername(String msg) {
        loginBtn.setClickable(true);
        loginBtn.setText("登录");
        user_name.requestFocus();
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void lostPass(String msg) {
        loginBtn.setClickable(true);
        loginBtn.setText("登录");
        user_password.requestFocus();
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(String msg) {
        Log.d(TAG,msg);
        UserInfoBean userInfoBean = new Gson().fromJson(msg, UserInfoBean.class);
        SpUtils.setStringSp(this,Constant.userInfoSP,Constant.userName,username);
        SpUtils.setStringSp(this,Constant.userInfoSP,Constant.userPass,password);
        SpUtils.setStringSp(this,Constant.userInfoSP,Constant.userInfo,msg);
        SpUtils.setStringSp(this,Constant.userInfoSP,Constant.shopIds,"["+userInfoBean.getUserInfo().getShopId()+"]");
        SpUtils.setStringSp(this,Constant.userInfoSP,Constant.shopId,userInfoBean.getUserInfo().getShopId());
        SpUtils.setStringSp(this,Constant.userInfoSP,Constant.shopName,userInfoBean.getUserInfo().getShopName());

        Intent intent  = new Intent();
        intent.setClass(this,MainTabActivity.class);
        intent.putExtra("ip",ipPort);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        startActivity(intent);
//        loginBtn.setClickable(true);
//        loginBtn.setText("登录");
        finish();

    }

    @Override
    public void loginFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginBtn.setClickable(true);
                loginBtn.setText("登录");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
