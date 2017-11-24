package com.iftech.car.trace.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.bean.UserInfoBean;
import com.iftech.car.trace.ui.activity.SettingsActivity;
import com.iftech.car.trace.ui.activity.UserInfoActivity;
import com.iftech.car.utils.SpUtils;

/**
 * 我的界面
 * **/
public class UserCenterFragment extends Fragment {

    private final static String TAG = "UserCenterFragment";

    @InjectView(R.id.title)
    Toolbar title;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;

    @InjectView(R.id.rl2)
    LinearLayout rl2;
    @InjectView(R.id.rl4)
    LinearLayout rl4;
    @InjectView(R.id.versionTV)
    TextView versionTV;

    @InjectView(R.id.userInfo)
    TextView userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_center,container,false);
        ButterKnife.inject(this, view);
        toolbar_title.setText(getString(R.string.user_center));
        toolbar_subtitle.setVisibility(View.GONE);
        initView();
        Log.d(TAG,"onCreateView");
        return view;
    }

    private void initView(){
        UserInfoBean infoBean = new Gson().fromJson(SpUtils.getStringSp(getContext(),Constant.userInfoSP,Constant.userInfo),UserInfoBean.class);
        userInfo.setText(infoBean.getUserInfo().getLoginId()+"\n"+infoBean.getUserInfo().getRoleTypeDisplay());
        try{
            // 获取package manager的实例
            PackageManager packageManager = getContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getContext().getPackageName(),0);
            String version = packInfo.versionName;
            versionTV.setText(version);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl2,R.id.rl4})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl2:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl4:
                Intent intent1 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent1);
                break;
        }
    }

}
