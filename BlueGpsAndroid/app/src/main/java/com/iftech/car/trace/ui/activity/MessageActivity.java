package com.iftech.car.trace.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.trace.adapter.MessageDetailsAdapter;
import com.iftech.car.trace.bean.MessageBean;
import com.iftech.car.trace.bean.MsgTypeCountBean;
import com.iftech.car.trace.bean.ResultBean;
import com.iftech.car.trace.presenter.MessageActivityPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.view.MessageActivityView;
import com.iftech.car.utils.NormalDialog;
import com.iftech.car.utils.SpacesItemDecoration;
import com.iftech.car.widget.EmptyRecyclerView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017-05-02
 * description: 某类消息界面
 * */

public class MessageActivity extends AppCompatActivity implements MessageActivityView ,OnRefreshListener, OnLoadMoreListener {

    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;

    @InjectView(R.id.switchMenu)
    SegmentControlView switchMenu;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @InjectView(R.id.swipe_target)
    EmptyRecyclerView recyclerView;
    @InjectView(R.id.id_empty_view)
    RelativeLayout id_empty_view;
    @InjectView(R.id.empty_tv)
    TextView empty_tv;
    @InjectView(R.id.leftLines)
    View leftLines;
    @InjectView(R.id.rightLines)
    View rightLines;

    @InjectView(R.id.ll_progress_bar)
    LinearLayout ll_progress_bar;
    @InjectView(R.id.progress_bar)
    ImageView progress_bar;
    Animation animation;

    private boolean isRead = false;
    private int keyValue = 0;
    private int pageIndex = 0;
    private List<MessageBean> list;
    private MessageDetailsAdapter adapter;
    private IBasePresenter messageActivityPresenter;
    private final static String TAG = "MessageActivity";
    private NormalDialog normalDialog =  new NormalDialog(MessageActivity.this);

    private final static int QUERY_DATA = 3000;
    private final static int READ_ONE = 1000;
    private final static int READ_ONE_TYPE = 2000;
    private MsgTypeCountBean msgTypeCountBean;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this);
        messageActivityPresenter = new MessageActivityPresenter(this);
         msgTypeCountBean = (MsgTypeCountBean) getIntent().getSerializableExtra("msgTypeCountBean");
        toolbar_title.setText(msgTypeCountBean.getKeyStr());
        toolbar_subtitle.setText("全部已读");
        keyValue = msgTypeCountBean.getKeyValue();
        initView();
        initEvents();
    }

    private void initView(){
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);

        list = new ArrayList<>();
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        back.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,1));
//        recyclerView.addItemDecoration(new SpacesItemDecoration(0,false));
        empty_tv.setText("暂时没有消息");
        recyclerView.setEmptyView(id_empty_view);
        adapter  = new MessageDetailsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        autoRefresh();
    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchMenu.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                if(newSelectedIndex == 0){
                    toolbar_subtitle.setVisibility(View.VISIBLE);
                    leftLines.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    rightLines.setBackgroundColor(getResources().getColor(R.color.white));
                    isRead = false;
                }else{
                    toolbar_subtitle.setVisibility(View.GONE);
                    leftLines.setBackgroundColor(getResources().getColor(R.color.white));
                    rightLines.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isRead = true;
                }
                list.clear();
                pageIndex = 0;
                getData();
            }
        });

        toolbar_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.showNormalDialog("确定将所有消息都标记为已读？");
                normalDialog.setDialogListener(new NormalDialog.DialogListener() {
                    @Override
                    public void enSureClock() {
                        ll_progress_bar.setVisibility(View.VISIBLE);
                        progress_bar.setAnimation(animation);
                        readMsg(READ_ONE_TYPE,msgTypeCountBean.getKeyValue()+"");
                    }
                });
            }
        });

        adapter.setOnItemClickListener(new MessageDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, final MessageBean messageBean) {
                if(!messageBean.isReaded()){
                    normalDialog.showNormalDialog("确定将此消息标记为已读吗？");
                    normalDialog.setDialogListener(new NormalDialog.DialogListener() {
                        @Override
                        public void enSureClock() {
                            ll_progress_bar.setVisibility(View.VISIBLE);
                            progress_bar.setAnimation(animation);
                            MessageActivity.this.position = position;
                            readMsg(READ_ONE,messageBean.getId()+"");
                        }
                    });
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    @Override
    public void onRefresh() {
        list.clear();
        pageIndex = 0;
        getData();
    }

    @Override
    public void onLoadMore() {
        pageIndex = pageIndex + 1;
        getData();
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void readMsg(int type,String parameter){
        String url = MainTabActivity.ip;
        if(type == READ_ONE){
            url = url+ Api.readOneMsg+"?id="+parameter;
        }else{
            url = url +Api.readOneTypeMsg+"?type="+parameter;
        }
        messageActivityPresenter.queryPost(url,MainTabActivity.username,MainTabActivity.password,type);
    }

    private void getData(){
        String url = MainTabActivity.ip+ Api.getUnReadAlarmMsgWithPage+"?type="+keyValue+"&pageIndex="+pageIndex+"&isRead="+isRead;
        messageActivityPresenter.queryGet(url,MainTabActivity.username,MainTabActivity.password,QUERY_DATA);
    }

    @Override
    public void querySuccess(final String msg, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case QUERY_DATA:
                        try{
                            Gson gson = new Gson();
                            JSONArray jsonArray = new JSONArray(msg);
                            for(int i = 0 ; i < jsonArray.length(); i++){
                                MessageBean messageBean = gson.fromJson(jsonArray.get(i).toString(),MessageBean.class);
                                list.add(messageBean);
                            }
                            adapter.setData(list);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        swipeToLoadLayout.setRefreshing(false);
                        swipeToLoadLayout.setLoadingMore(false);
                        break;
                    case READ_ONE:
                    case READ_ONE_TYPE:
                        final ResultBean resultBean = new Gson().fromJson(msg,ResultBean.class);
                        if(resultBean.isSuccess()){
                            if(type == READ_ONE){
                                adapter.notifyItemRemoved(position);
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(0,list.size()-position);
                            }else{
                                list.clear();
                                adapter.setData(list);
                            }
                        }else {
                            Toast.makeText(MessageActivity.this,resultBean.getContent(),Toast.LENGTH_LONG).show();
                        }
                        ll_progress_bar.setVisibility(View.GONE);
                        progress_bar.clearAnimation();
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
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                Toast.makeText(MessageActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
