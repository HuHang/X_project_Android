package com.iftech.car.trace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.iftech.car.R;
import com.iftech.car.trace.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * created by tanghuosong 2017/5/4
 * description: 消息RecycleView适配器
 **/
public class MessageDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<MessageBean> list = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;

    public MessageDetailsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MessageBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,MessageBean messageBean);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageDetailsAdapter.ViewHolder) {

            final MessageBean messageBean = list.get(position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position,messageBean);
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
            ((ViewHolder) holder).VinTV.setText("车架号："+messageBean.getVin());
            ((ViewHolder) holder).fenceStateTV.setText(messageBean.getAlarmFenceStateDisplay());
            ((ViewHolder) holder).IMeiTV.setText("设备编码："+messageBean.getImei());
            if(!messageBean.getAlarmCurrentShopType().equals("0")){
                ((ViewHolder) holder).shopNameTV.setText("所属4S店："+messageBean.getAlarmCurrentShopName()+"("+messageBean.getAlarmCurrentShopTypeDisplay()+")");
            }else{
                ((ViewHolder) holder).shopNameTV.setText("所属4S店："+messageBean.getAlarmCurrentShopName());
            }
//            ((ViewHolder) holder).shopNameTV.setText("报警商户："+messageBean.getAlarmCurrentShopName());
            ((ViewHolder) holder).parentShopNameTV.setText("报警商户："+messageBean.getAlarmShopName());
            ((ViewHolder) holder).carTypeTV.setText("车辆型号："+messageBean.getCarType());
            ((ViewHolder) holder).timeTV.setText("报警时间："+messageBean.getCreatedAt().substring(0,19).replace("T"," "));
            if(messageBean.getAlarmFenceStateDisplay().equals("围栏内")){
                ((ViewHolder) holder).fenceStateTV.setTextColor(context.getResources().getColor(R.color.gray_deep));
            }else{
                ((ViewHolder) holder).fenceStateTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_message_inside, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView VinTV;
        TextView fenceStateTV;
        TextView IMeiTV;
        TextView shopTypeTV;
        TextView shopNameTV;
        TextView parentShopNameTV;
        TextView carTypeTV;
        TextView timeTV;

        public ViewHolder(View convertView) {
            super(convertView);
            VinTV = (TextView)convertView.findViewById(R.id.VinTV);
            fenceStateTV = (TextView)convertView.findViewById(R.id.fenceStateTV);
            IMeiTV = (TextView)convertView.findViewById(R.id.IMeiTV);
            shopTypeTV = (TextView)convertView.findViewById(R.id.shopTypeTV);
            shopNameTV = (TextView)convertView.findViewById(R.id.shopNameTV);
            parentShopNameTV = (TextView)convertView.findViewById(R.id.parentShopNameTV);
            carTypeTV = (TextView)convertView.findViewById(R.id.carTypeTV);
            timeTV = (TextView)convertView.findViewById(R.id.timeTV);
        }
    }
}
