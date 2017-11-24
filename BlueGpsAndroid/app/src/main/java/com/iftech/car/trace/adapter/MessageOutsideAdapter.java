package com.iftech.car.trace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.iftech.car.R;
import com.iftech.car.trace.bean.MsgTypeCountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanghuosong on 2017/4/28.
 */
public class MessageOutsideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MsgTypeCountBean> data = new ArrayList<>();
    private Context context;

    public interface OnClickListener {
        void onFavoriteClick(View view, int position);

        void onItemClick(View view, int position, MsgTypeCountBean msgTypeCountBean);

    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<MsgTypeCountBean> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public MessageOutsideAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MsgTypeCountBean> list){
        this.data = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据position判断要加载的界面，可以是多个页面的item
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_outside, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final MsgTypeCountBean msgTypeCountBean = data.get(position);

            if (onClickListener != null) {
                ((ViewHolder) holder).cancel_favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onClickListener.onFavoriteClick(holder.itemView, position);
                    }
                });
                ((ViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onItemClick(v,position,msgTypeCountBean);
                    }
                });
            }

            ((ViewHolder) holder).message_type.setText(msgTypeCountBean.getKeyStr());
            if(msgTypeCountBean.isFavorite()){
                ((ViewHolder) holder).cancel_favorite.setText("取消");
            }else{
                ((ViewHolder) holder).cancel_favorite.setText("订阅");
            }
            switch (msgTypeCountBean.getKeyValue()){
                case 14:
                case 2:
                case 15:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_battery);
                    break;
                case 4:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_in);
                    break;
                case 5:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_out);
//                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_craw);
                    break;
                case 3:
                case 6:
                case 9:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_move);
                    break;
                case 10:
                case 11:
                case 13:
                case 18:
                case 23:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_gps);
                    break;
                case 1:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_fault);
                    break;
                case 0:
                case 22:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_other);
                    break;
                case 12:
                case 16:
                case 17:
                case 19:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_device);
                    break;
                default:
                    ((ViewHolder) holder).typeImg.setImageResource(R.drawable.icon_other);
                    break;

            }
//            BadgeView badgeView = new BadgeView(context);
////            BadgeView badgeView = BadgeFactory.createCircle(context).setBadgeCount(msgTypeCountBean.getCount()).bind(((ViewHolder)holder).mark);
//            badgeView.setTargetView(((ViewHolder)holder).mark);
//            badgeView.setBadgeCount(msgTypeCountBean.getCount());
            if(msgTypeCountBean.getCount() == 0){
                ((ViewHolder)holder).mark.setVisibility(View.GONE);
//                badgeView.unbind();
//                badgeView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }else {
//                badgeView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ((ViewHolder) holder).mark.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).mark.setText(msgTypeCountBean.getCount()+"");
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView message_type;
        ImageView typeImg;
        TextView cancel_favorite;
        TextView mark;
        public ViewHolder(View convertView) {
            super(convertView);
            message_type = (TextView) convertView.findViewById(R.id.message_type);
            typeImg = (ImageView) convertView.findViewById(R.id.typeImg);
            cancel_favorite = (TextView) convertView.findViewById(R.id.cancel_favorite);
            mark = (TextView) convertView.findViewById(R.id.mark);
        }
    }
}
