package com.iftech.car.trace.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iftech.car.R;
import com.iftech.car.common.Constant;
import com.iftech.car.utils.SpUtils;

public class IpPortSettingActivity extends AppCompatActivity {

    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;

    @InjectView(R.id.IP_ET)
    EditText IP_ET;
    @InjectView(R.id.Port_ET)
    EditText Port_ET;
    @InjectView(R.id.save_ip_port_btn)
    Button save_ip_port_btn;

    String TAG = "IpPortSettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_port_setting);
        ButterKnife.inject(this);
        toolbar_title.setText(getString(R.string.settingIpPort));
        Log.d(TAG,"onCreate");
        initView();
        initEvents();
    }

    private void initView(){
        IP_ET.setText(SpUtils.getStringSp(this, Constant.serverSp,Constant.serverIp));
        Port_ET.setText(SpUtils.getStringSp(this, Constant.serverSp,Constant.serverPort));
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save_ip_port_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIpPort();
            }
        });
    }

    private void saveIpPort(){
        String ip = IP_ET.getText().toString().trim();
        String port = Port_ET.getText().toString().trim();
        if(TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)){
            Toast.makeText(getApplicationContext(),"请输入完整的IP地址和端口号！",Toast.LENGTH_SHORT).show();
            return;
        }
        SpUtils.setStringSp(this, Constant.serverSp,Constant.serverIp,ip);
        SpUtils.setStringSp(this, Constant.serverSp,Constant.serverPort,port);
        Toast.makeText(this,"保存成功！",Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
