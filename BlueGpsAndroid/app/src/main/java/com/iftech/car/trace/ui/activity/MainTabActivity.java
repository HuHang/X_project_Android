package com.iftech.car.trace.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iftech.car.R;
import com.iftech.car.trace.ui.fragment.IndexFragment;
import com.iftech.car.trace.ui.fragment.MessageFragment;
import com.iftech.car.trace.ui.fragment.UserCenterFragment;
import com.iftech.car.widget.MyTabBar;

/**
 * 首页菜单
 * **/
public class MainTabActivity extends AppCompatActivity{
    @InjectView(R.id.fragment_content)
    FrameLayout frameLayout;
    @InjectView(R.id.tabBar)
    MyTabBar tabBar;

    private IndexFragment indexFragment = new IndexFragment();
    private MessageFragment messageFragment = new MessageFragment();
    private UserCenterFragment userCenterFragment = new UserCenterFragment();

    public static String username ;
    public static String password;
    public static String ip;
    private long firstTime = 0;
    public final static String TAG = "MainTabActivity";
    public static MainTabActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tab);
        instance = this;
        ButterKnife.inject(this);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        ip = getIntent().getStringExtra("ip");
        initView();
    }

    public void initView(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_content,indexFragment,"index");
        transaction.add(R.id.fragment_content,messageFragment,"message");
        transaction.add(R.id.fragment_content,userCenterFragment,"userCenter");
        transaction.show(indexFragment).hide(messageFragment).hide(userCenterFragment).commit();

        tabBar.setOnItemMenuClick(new MyTabBar.OnItemMenuClick() {

            @Override
            public void onThisClick(int eachItem) {
                if(eachItem == 0){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(indexFragment).hide(messageFragment).hide(userCenterFragment).commit();
                }else if(eachItem == 1){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(messageFragment).hide(indexFragment).hide(userCenterFragment).commit();
                }else if(eachItem == 2){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show( userCenterFragment).hide(messageFragment).hide(indexFragment).commit();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(MainTabActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                System.exit(0);//否则退出程序
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
