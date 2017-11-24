package com.iftech.car.trace.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iftech.car.R;
import com.iftech.car.common.Constant;
import com.iftech.car.utils.SpUtils;
import com.iftech.car.widget.CustomVideoView;

/**
 * 启动界面界面
 * **/
public class LauncherActivity extends AppCompatActivity {

    @InjectView(R.id.videoView)
    CustomVideoView vv;
    @InjectView(R.id.jumpTV)
    TextView jumpTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        ButterKnife.inject(this);
        initView();
        initEvents();
    }

    private void initView(){
//        String isFirst = SpUtils.getStringSp(getApplicationContext(), Constant.isFirstSp,Constant.isFirst);
//        if("".equals(isFirst)){ // 说明是第一次登录
//            SpUtils.setStringSp(getApplicationContext(),Constant.isFirstSp,Constant.isFirst,"true");
//            playVideo();
//        }else{ // 不是第一次登录
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            this.finish();
//        }

        playVideo();
    }

    private void initEvents(){
        jumpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vv.isPlaying()){
                    vv.stopPlayback();
                }
                Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(intent);
                LauncherActivity.this.finish();
            }
        });
    }

    private void playVideo(){
        /*主要代码起始位置*/
        final String uri = "android.resource://" + getPackageName() + "/" + R.raw.qidong;
        vv.setVideoURI(Uri.parse(uri));
        vv.start();
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);

            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vv.setVideoURI(Uri.parse(uri));
                vv.start();
            }
        });
        /*主要代码结束位置*/
    }
}
