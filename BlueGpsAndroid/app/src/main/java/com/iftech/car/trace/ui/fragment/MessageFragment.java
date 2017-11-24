package com.iftech.car.trace.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.iftech.car.R;
import com.iftech.car.common.Api;
import com.iftech.car.trace.adapter.MessageOutsideAdapter;
import com.iftech.car.trace.bean.MsgTypeCountBean;
import com.iftech.car.trace.bean.ResultBean;
import com.iftech.car.trace.presenter.MessageFragmentPresenter;
import com.iftech.car.trace.presenter.iface.IBasePresenter;
import com.iftech.car.trace.ui.activity.MainTabActivity;
import com.iftech.car.trace.ui.activity.MessageActivity;
import com.iftech.car.trace.ui.view.MessageFragmentView;
import com.iftech.car.utils.SpacesItemDecoration;
import com.iftech.car.widget.EmptyRecyclerView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息界面
 *, PullLoadMoreRecyclerView.PullLoadMoreListener
 * */
public class MessageFragment extends Fragment implements MessageFragmentView, OnRefreshListener, OnLoadMoreListener {

    private final static String TAG = "MessageFragment";

    @InjectView(R.id.title)
    Toolbar title;
    @InjectView(R.id.toolbar_title)
    TextView toolbar_title;
    @InjectView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;

    @InjectView(R.id.switchMenu)
    SegmentControlView switchMenu;
    @InjectView(R.id.swipe_target)
//    RecyclerView recyclerView;
    EmptyRecyclerView recyclerView;
    @InjectView(R.id.id_empty_view)
    RelativeLayout id_empty_view;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @InjectView(R.id.leftLines)
    View leftLines;
    @InjectView(R.id.rightLines)
    View rightLines;

    private List<MsgTypeCountBean> list;
    private MessageOutsideAdapter adapter ;

    private IBasePresenter messagePresenter;
    private boolean isFavorite = true;
    private int position1;

    private final static int QUERY_DATA = 10127;
    private final static int SUBSCRIBE_MSG =10128;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_message, container, false);
        Log.d(TAG,"onCreate");
        ButterKnife.inject(this, view);
        toolbar_title.setText(getString(R.string.message));
        toolbar_subtitle.setVisibility(View.GONE);
        messagePresenter = new MessageFragmentPresenter(this);
        initView();
        initEvents();
        return view;
    }

    private void initView(){
        list = new ArrayList<>();
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0,false));
        recyclerView.setEmptyView(id_empty_view);
        adapter = new MessageOutsideAdapter(getContext());
        recyclerView.setAdapter(adapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        autoRefresh();
    }

    private void initEvents(){
        switchMenu.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                if(newSelectedIndex == 0){
                    leftLines.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    rightLines.setBackgroundColor(getResources().getColor(R.color.white));
                    isFavorite = true;
                }else{
                    leftLines.setBackgroundColor(getResources().getColor(R.color.white));
                    rightLines.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isFavorite = false;
                }
                list.clear();
                getData();
//                swipeToLoadLayout.setRefreshing(true);
            }
        });

        adapter.setOnClickListener(new MessageOutsideAdapter.OnClickListener() {
            @Override
            public void onFavoriteClick(View view, int position) {
                position1 = position;
                MsgTypeCountBean msgTypeCountBean = list.get(position);
                int alarmType = msgTypeCountBean.getKeyValue();
                if(msgTypeCountBean.isFavorite()){
                    messagePresenter.queryPost(MainTabActivity.ip+Api.unSubScripAlarm+"?alarmType="+alarmType,
                            MainTabActivity.username,MainTabActivity.password,SUBSCRIBE_MSG);
                }else{
                    messagePresenter.queryPost(MainTabActivity.ip+Api.subScribeAlarm+"?alarmType="+alarmType,
                            MainTabActivity.username,MainTabActivity.password,SUBSCRIBE_MSG);
                }
            }

            @Override
            public void onItemClick(View view, int position, MsgTypeCountBean msgTypeCountBean) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("msgTypeCountBean",msgTypeCountBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        list.clear();
        getData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(! hidden){
            autoRefresh();
        }
    }
    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void getData(){
        if(isFavorite){
            messagePresenter.queryGet(MainTabActivity.ip+ Api.getAllFavoriteAndCount,
                    MainTabActivity.username,MainTabActivity.password,QUERY_DATA);
        }else{
            messagePresenter.queryGet(MainTabActivity.ip+Api.getAlarmMsgTypeCount,
                    MainTabActivity.username,MainTabActivity.password,QUERY_DATA);
        }
    }

    @Override
    public void querySuccess(final String msg,final int type) {
        Log.d(TAG, msg);
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (type){
                        case QUERY_DATA:
                            try {
                                Gson gson = new Gson();
                                JSONArray array = new JSONArray(msg);
                                for (int i = 0; i<array.length(); i++){
                                    MsgTypeCountBean msgTypeCountBean = gson.fromJson(array.get(i).toString(),MsgTypeCountBean.class);
                                    list.add(msgTypeCountBean);
                                }
                                adapter.addData(list);
                            }catch (JSONException e){
                                Toast.makeText(getActivity(),"异常:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
                            break;
                        case SUBSCRIBE_MSG:
                            ResultBean resultBean = new Gson().fromJson(msg,ResultBean.class);
                            if(resultBean.isSuccess()){
                                if(isFavorite){
                                    adapter.removeData(position1);
                                }else{
                                    if(list.get(position1).isFavorite()){
                                        list.get(position1).setFavorite(false);
                                    }else{
                                        list.get(position1).setFavorite(true);
                                    }
                                    adapter.notifyItemChanged(position1);
                                }
                            }
                            Toast.makeText(getActivity(),resultBean.getContent(),Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }else{
            return;
        }
    }

    @Override
    public void queryFail(final String msg) {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeToLoadLayout.setLoadingMore(false);
                    swipeToLoadLayout.setRefreshing(false);
                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            return;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"destroy");
        isFavorite = true;
        super.onDestroy();
    }
}
