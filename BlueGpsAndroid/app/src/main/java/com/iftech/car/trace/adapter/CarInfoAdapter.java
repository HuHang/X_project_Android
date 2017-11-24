package com.iftech.car.trace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.iftech.car.R;
import com.iftech.car.trace.bean.CarInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/4
 * description: 监控界面RecycleView适配器
 **/
public class CarInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<CarInfoBean> list = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;

    public CarInfoAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CarInfoBean> list){
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, CarInfoBean carInfoBean);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CarInfoAdapter.ViewHolder) {
            final CarInfoBean carInfoBean = list.get(position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position,carInfoBean);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }


            if (carInfoBean.getImei().length() == 0){
                ((ViewHolder) holder).locationTV.setText("未绑定");
            }else {
                ((ViewHolder) holder).locationTV.setText(carInfoBean.getFenceStateDisplay());
            }

            ((ViewHolder) holder).vinTV.setText(Html.fromHtml("<font color='#9B9B9B'>车架编号：</font>"+carInfoBean.getVin()));
            ((ViewHolder) holder).shopTypeTV.setText(Html.fromHtml("<font color='#9B9B9B'>商户类型：</font>"+carInfoBean.getCurrentShopTypeDisplay()));
            ((ViewHolder) holder).shopNameTV.setText(Html.fromHtml("<font color='#9B9B9B'>商户名称：</font>"+carInfoBean.getCarCurrentShopName()));
            ((ViewHolder) holder).bankTV.setText(Html.fromHtml("<font color='#9B9B9B'>所属银行：</font>"+carInfoBean.getBankName()));
            ((ViewHolder) holder).carBrandTV.setText("品牌："+carInfoBean.getBrand());
            ((ViewHolder) holder).carTypeTV.setText("车型："+carInfoBean.getCarType());
            ((ViewHolder) holder).carColorTV.setText("颜色："+carInfoBean.getCarColor());
            ((ViewHolder) holder).timeTV.setText("获取时间："+carInfoBean.getSignalTime().substring(0,19).replace("T"," "));
            if(carInfoBean.getFenceStateDisplay().equals("围栏内")) {
                ((ViewHolder) holder).locationTV.setTextColor(context.getResources().getColor(R.color.gray_deep));
            }else if(carInfoBean.getFenceStateDisplay().equals("围栏外")) {
                ((ViewHolder) holder).locationTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_carinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView vinTV;
        TextView locationTV;
        TextView shopTypeTV;
        TextView shopNameTV;
        TextView bankTV;
        TextView carBrandTV;
        TextView carTypeTV;
        TextView carColorTV;
        TextView timeTV;

        public ViewHolder(View convertView) {
            super(convertView);
            vinTV = (TextView)convertView.findViewById(R.id.vinTV);
            locationTV = (TextView) convertView.findViewById(R.id.locationTV);
            shopTypeTV = (TextView)convertView.findViewById(R.id.shopTypeTV);
            shopNameTV = (TextView)convertView.findViewById(R.id.shopNameTV);
            bankTV = (TextView)convertView.findViewById(R.id.bankTV);
            carBrandTV = (TextView)convertView.findViewById(R.id.carBrandTV);
            carTypeTV = (TextView)convertView.findViewById(R.id.carTypeTV);
            carColorTV = (TextView)convertView.findViewById(R.id.carColorTV);
            timeTV = (TextView)convertView.findViewById(R.id.timeTV);
        }
    }
}
