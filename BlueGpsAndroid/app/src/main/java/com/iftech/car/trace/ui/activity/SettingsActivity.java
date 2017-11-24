package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iftech.car.R;
import com.iftech.car.common.Constant;
import com.iftech.car.utils.DataCleanManager;
import com.iftech.car.utils.NormalDialog;
import com.iftech.car.utils.SpUtils;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * 设置界面
 * **/
public class SettingsActivity extends AppCompatActivity {
    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.cacheTV)
    TextView cacheTV;
    @InjectView(R.id.layout)
    RelativeLayout layout;
    @InjectView(R.id.rl1)
    LinearLayout rl1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        toolbar_title.setText(R.string.settings);

        initView();
        initEvents();
    }


    private void initView(){
        try{
            cacheTV.setText(DataCleanManager.getTotalCacheSize(getContext()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog normalDialog = new NormalDialog(SettingsActivity.this);
                normalDialog.showNormalDialog("退出登录将会清空密码，确认退出?");
                normalDialog.setDialogListener(new NormalDialog.DialogListener() {
                    @Override
                    public void enSureClock() {
                        SpUtils.setStringSp(getContext(), Constant.userInfoSP,Constant.userPass,"");
                        Intent intent2  = new Intent(SettingsActivity.this,LoginActivity.class);
                        startActivity(intent2);
                        SettingsActivity.this.finish();
                        MainTabActivity.instance.finish();
                    }
                });

            }
        });

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog normalDialog = new NormalDialog(SettingsActivity.this);
                normalDialog.showNormalDialog("清空缓存将会清除应用信息，确认清空?");
                normalDialog.setDialogListener(new NormalDialog.DialogListener() {
                    @Override
                    public void enSureClock() {
                        DataCleanManager.clearAllCache(getApplicationContext());
                        initView();
                    }
                });
            }
        });
    }
}
