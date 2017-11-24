package com.iftech.car.inventory.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iftech.car.R;
import com.iftech.car.trace.ui.fragment.IndexFragment;
import com.iftech.car.trace.ui.fragment.MessageFragment;
import com.iftech.car.trace.ui.fragment.UserCenterFragment;

/**
 * 首页菜单
 * **/
public class InventoryTabActivity extends AppCompatActivity{
    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;
    @InjectView(R.id.indexButton)
    RadioButton indexButton;
    @InjectView(R.id.messageButton)
    RadioButton messageButton;
    @InjectView(R.id.userCenterButton)
    RadioButton userCenterButton;
    @InjectView(R.id.fragment_content)
    FrameLayout frameLayout;

    IndexFragment indexFragment = new IndexFragment();
    MessageFragment messageFragment = new MessageFragment();
    UserCenterFragment userCenterFragment = new UserCenterFragment();

    public static String username ;
    public static String password;
    public static String ip;

    public final static String TAG = "MainTabActivity";
    public static InventoryTabActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tab);
        instance = this;
        ButterKnife.inject(this);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        ip = getIntent().getStringExtra("ip");
        Log.d(TAG,username+password+ip);
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_content,indexFragment,"index");
        transaction.add(R.id.fragment_content,messageFragment,"message");
        transaction.add(R.id.fragment_content,userCenterFragment,"userCenter");
        transaction.show(indexFragment).hide(messageFragment).hide(userCenterFragment).commit();
//        transaction.replace(R.id.fragment_content,indexFragment,"index").commit();
        initView();
        initData();
    }

    public void initView(){
        //
        Drawable myImage = null;
        myImage = getResources().getDrawable(R.drawable.icon_tab_index_selected);
        myImage.setBounds(0, 0,70, 70);
        indexButton.setCompoundDrawables(null,myImage,null,null);
        myImage = getResources().getDrawable(R.drawable.icon_tab_message_nomal);
        myImage.setBounds(0, 0,70, 70);
        messageButton.setCompoundDrawables(null,myImage,null,null);
        myImage = getResources().getDrawable(R.drawable.icon_tab_user_nomal);
        myImage.setBounds(0, 0,70, 70);
        userCenterButton.setCompoundDrawables(null,myImage,null,null);
        indexButton.setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for( int i = 0; i < group.getChildCount(); i++){
                    RadioButton radioButton = (RadioButton)group.getChildAt(i);
                    Drawable myImage = null;
                    int currentId = radioButton.getId();
                    if(currentId == checkedId){
                        if(currentId == indexButton.getId()){
                            myImage = getResources().getDrawable(R.drawable.icon_tab_index_selected);
                        }else if(currentId == messageButton.getId()){
                            myImage = getResources().getDrawable(R.drawable.icon_tab_message_selected);
                        }else if(currentId == userCenterButton.getId()){
                            myImage = getResources().getDrawable(R.drawable.icon_tab_user_selected);
                        }
                        radioButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        myImage.setBounds(0, 0,70, 70);
                        radioButton.setCompoundDrawables(null, myImage, null, null);
                    }else{
                        if(currentId == indexButton.getId()){
                            myImage = getResources().getDrawable(R.drawable.icon_tab_index_nomal);
                        }else if(currentId == messageButton.getId()){
                            myImage = getResources().getDrawable(R.drawable.icon_tab_message_nomal);
                        }else if(currentId == userCenterButton.getId()){
                            myImage = getResources().getDrawable(R.drawable.icon_tab_user_nomal);
                        }
                        radioButton.setTextColor(getResources().getColor(R.color.gray));
                        myImage.setBounds(0, 0, 70,70);
                        radioButton.setCompoundDrawables(null, myImage, null, null);
                    }
                }
            }
        });
    }

    private void initData(){

    }

    @OnClick({R.id.indexButton,R.id.messageButton,R.id.userCenterButton})
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.indexButton:
                transaction.show(indexFragment).hide(messageFragment).hide(userCenterFragment).commit();
//                transaction.replace(R.id.fragment_content,indexFragment,"index").commit();
                break;
            case R.id.messageButton:
                transaction.show(messageFragment).hide(indexFragment).hide(userCenterFragment).commit();
//                transaction.replace(R.id.fragment_content,messageFragment,"message").commit();
                break;
            case R.id.userCenterButton:
                transaction.show( userCenterFragment).hide(messageFragment).hide(indexFragment).commit();
//                transaction.replace(R.id.fragment_content,userCenterFragment,"userCenter").commit();
                break;
        }
    }
}
