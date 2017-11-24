package com.iftech.car.trace.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.bean.UserInfoBean;
import com.iftech.car.utils.SpUtils;

/**
 * 个人中心界面
 * **/
public class UserInfoActivity extends AppCompatActivity {

    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.userLoginId)
    TextView userLoginId;
    @InjectView(R.id.userName)
    TextView userName;
    @InjectView(R.id.roleTypeDisplay)
    TextView roleTypeDisplay;
    @InjectView(R.id.userEmail)
    TextView userEmail;
    @InjectView(R.id.userPhone)
    TextView userPhone;
    @InjectView(R.id.dealer)
    TextView dealer;
    @InjectView(R.id.bank)
    TextView bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.inject(this);
        toolbar_title.setText(getString(R.string.user_center));
        initView();
        initEvents();
    }


    private void initView(){
        String userDetails = SpUtils.getStringSp(this, Constant.userInfoSP,Constant.userInfo);
        UserInfoBean userInfoBean = new Gson().fromJson(userDetails,UserInfoBean.class);
        userLoginId.setText(userInfoBean.getUserInfo().getLoginId());
        userName.setText(userInfoBean.getUserInfo().getName());
        roleTypeDisplay.setText(userInfoBean.getUserInfo().getRoleTypeDisplay());
        userEmail.setText(userInfoBean.getUserInfo().getEmail());
        userPhone.setText(userInfoBean.getUserInfo().getPhone());
        dealer.setText(userInfoBean.getUserInfo().getShopName());
        bank.setText(userInfoBean.getUserInfo().getBankName());
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
