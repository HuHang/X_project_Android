package com.iftech.car.trace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.iftech.car.R;
import com.iftech.car.trace.bean.DeviceInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/4
 * description: 监控界面RecycleView适配器
 **/
public class DeviceInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<DeviceInfoBean> list = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;

    public DeviceInfoAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DeviceInfoBean> list){
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,DeviceInfoBean deviceInfoBean);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DeviceInfoAdapter.ViewHolder) {
            final DeviceInfoBean deviceInfoBean = list.get(position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position,deviceInfoBean);
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

            ((ViewHolder) holder).IMeiTV.setText(Html.fromHtml("<font color='#9B9B9B'>设备编码：</font>"+deviceInfoBean.getImei()));
            ((ViewHolder) holder).IMeiNum.setText(Html.fromHtml("<font color='#9B9B9B'>车架编号：</font>"+deviceInfoBean.getVin()));
            ((ViewHolder) holder).shopTypeTV.setText(Html.fromHtml("<font color='#9B9B9B'>商户类型：</font>"+deviceInfoBean.getCurrentShopTypeDisplay()));
            ((ViewHolder) holder).shopNameTV.setText(Html.fromHtml("<font color='#9B9B9B'>商户名称：</font>"+deviceInfoBean.getCurrentShopName()));
            if(deviceInfoBean.getWorkStateDisplay().equals("离线")){
                ((ViewHolder) holder).workStateTV.setText(Html.fromHtml("工作状态：<font color='#BF1F1E'>"+deviceInfoBean.getWorkStateDisplay()+"</font>"));
            }else{
                ((ViewHolder) holder).workStateTV.setText(Html.fromHtml("工作状态：<font color='#9B9B9B'>"+deviceInfoBean.getWorkStateDisplay()+"</font>"));
            }
            ((ViewHolder) holder).signTypeTV.setText("信号类型："+deviceInfoBean.getSingleType());
            ((ViewHolder) holder).singStrongTV.setText("信号强度："+deviceInfoBean.getSignalLevelDisplay());
            ((ViewHolder) holder).electricCount.setText(deviceInfoBean.getBatteryPercent());
            ((ViewHolder) holder).timeTV.setText("获取时间："+deviceInfoBean.getSignalTime().substring(0,19).replace("T"," "));

            if (deviceInfoBean.getVin().length() == 0){
                ((ViewHolder) holder).useStateTV .setText("闲置");
            }else {
                ((ViewHolder) holder).useStateTV .setText(deviceInfoBean.getFenceStateDisplay());
            }
            if(deviceInfoBean.getFenceStateDisplay().equals("围栏内")){
                ((ViewHolder) holder).useStateTV.setTextColor(context.getResources().getColor(R.color.gray_deep));
            }else if(deviceInfoBean.getFenceStateDisplay().equals("围栏外")){
                ((ViewHolder) holder).useStateTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }

            if(deviceInfoBean.getBattery()<5){
                ((ViewHolder) holder).battery_img.setImageResource(R.drawable.icon_battery0);
            }else if(deviceInfoBean.getBattery()<21){
                ((ViewHolder) holder).battery_img.setImageResource(R.drawable.icon_battery1);

            }else if(deviceInfoBean.getBattery()<50){
                ((ViewHolder) holder).battery_img.setImageResource(R.drawable.icon_battery2);

            }else if(deviceInfoBean.getBattery()==50){
                ((ViewHolder) holder).battery_img.setImageResource(R.drawable.icon_battery3);

            }else if(deviceInfoBean.getBattery()<100){
                ((ViewHolder) holder).battery_img.setImageResource(R.drawable.icon_battery4);

            }else{
                ((ViewHolder) holder).battery_img.setImageResource(R.drawable.icon_battery5);
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

        View view = LayoutInflater.from(context).inflate(R.layout.item_deviceinfo, parent, false);
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

        TextView IMeiTV;
        TextView useStateTV;
        TextView IMeiNum;
        TextView shopTypeTV;
        TextView shopNameTV;
        TextView electricCount;
        TextView workStateTV;
        TextView signTypeTV;
        TextView singStrongTV;
        TextView timeTV;
        ImageView battery_img;

        public ViewHolder(View convertView) {
            super(convertView);
            IMeiTV = (TextView)convertView.findViewById(R.id.IMeiTV);
            useStateTV = (TextView)convertView.findViewById(R.id.useStateTV);
            IMeiNum = (TextView)convertView.findViewById(R.id.IMeiNum);
            shopTypeTV = (TextView)convertView.findViewById(R.id.shopTypeTV);
            shopNameTV = (TextView)convertView.findViewById(R.id.shopNameTV);
            electricCount = (TextView)convertView.findViewById(R.id.electricCount);
            workStateTV = (TextView)convertView.findViewById(R.id.workStateTV);
            signTypeTV = (TextView)convertView.findViewById(R.id.signTypeTV);
            singStrongTV = (TextView)convertView.findViewById(R.id.singStrongTV);
            timeTV = (TextView)convertView.findViewById(R.id.timeTV);
            battery_img = (ImageView) convertView.findViewById(R.id.battery_img);
        }
    }
}
