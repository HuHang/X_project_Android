package com.iftech.car.trace.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.common.Constant;
import com.iftech.car.trace.bean.ChartBean;
import com.iftech.car.trace.bean.UserInfoBean;
import com.iftech.car.trace.presenter.ChartPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.ChartView;
import com.iftech.car.utils.SpUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;

public class ChartActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener ,ChartView{

    @InjectView(R.id.title)
    Toolbar title;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.webView0)
    WebView webView0;
    Animation animation;

    private KProgressHUD hud;
    private ChartBean chartBean = new ChartBean();

    private IBasePresenter chartPresenter;

    private final static String TAG = "ChartActivity";
    private final static int ReportByType = 10124;
    private String token;
    private String token1 ;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.inject(this);
        toolbar_title.setText(getIntent().getExtras().getString("title"));
        chartPresenter = new ChartPresenter(this);
        url = getIntent().getExtras().getString("url");
        initView();
        initEvents();

    }

    private void initView(){
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        webView0.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        webView0.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView0.getSettings().setDomStorageEnabled(true);
        webView0.getSettings().setBuiltInZoomControls(true);
        webView0.getSettings().setBlockNetworkImage(true);
        webView0.loadUrl(url);
        webView0.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                scheduleDismiss();
            }
        });
//        getData();
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }


    private void getData(){

        UserInfoBean userInfoBean = new Gson().fromJson(SpUtils.getStringSp(this,Constant.userInfoSP,Constant.userInfo),UserInfoBean.class);
        token = userInfoBean.getUserInfo().getToken().replace(" ","");
        token1 = userInfoBean.getUserInfo().getToken().replace(" ","%20");
        String url = MainTabActivity.ip+ Api.getReportByType+"?type=enterStock&token="+token;
        Log.d(TAG,url);
        chartPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,ReportByType);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void querySuccess(final String msg, final int type) {
        Log.d(TAG,msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case ReportByType:
                        try{
                            JSONArray jsonArray  = new JSONArray(msg);
                            for(int i =0 ;i< jsonArray.length();i++){
                                chartBean = new Gson().fromJson(jsonArray.get(i).toString(),ChartBean.class);
                            }
                            webView0.loadUrl(chartBean.getData().get(9).getUrl()+token1);
                            webView0.setWebViewClient(new WebViewClient(){
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);

                                }
                            });
                        }catch (JSONException e){
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
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
