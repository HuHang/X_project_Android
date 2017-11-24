package com.iftech.car.trace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.iftech.car.R;
import com.iftech.car.trace.bean.ReportBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanghuosong on 2017/4/28.
 */
public class IndexRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ReportBean> data = new ArrayList<>();
    private Context context;

    private final static int ONE = 1;
    private final static int TOW = 2;
    private final static int THREE = 3;
    private final static int FOUR = 4;
    private final static int FIVE = 5;
    private final static int SIX  = 6;
    private final static int SEVEN = 7;
    private final static int EIGHT = 8;
    private final static int NINE = 9;
    private final static int TEN = 10;

    public interface OnItemClickListener {
        void onItemClick(View view, int position,ReportBean reportBean);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(data.size()>0){
            return data.get(position).getChartType();
        }else{
            return 0;
        }
    }

    public IndexRecycleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ReportBean> lists){
        this.data = lists;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder= null;
        switch (viewType){
            case ONE:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_first, parent, false);
                viewHolder =  new FirstViewHolder(view);
                break;
            case TOW:
            case FIVE:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_second_five, parent, false);
                viewHolder =  new SecondFiveViewHolder(view);
                break;
            case THREE :
                view = LayoutInflater.from(context).inflate(R.layout.item_index_three, parent, false);
                viewHolder = new ThreeViewHolder(view);
                break;
            case FOUR:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_four_six, parent, false);
                viewHolder = new FourViewHolder(view);
                break;
            case SIX:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_serven_eleven, parent, false);
                viewHolder = new SevenElevenViewHolder(view);
                break;
            case SEVEN:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_eight_twelve, parent, false);
                viewHolder = new EightTwelveViewHolder(view);
                break;
            case EIGHT:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_nine, parent, false);
                viewHolder = new NineViewHolder(view);
                break;
            case NINE:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_ten, parent, false);
                viewHolder = new TenViewHolder(view);
                break;
            case TEN:
                view = LayoutInflater.from(context).inflate(R.layout.item_index_default, parent, false);
                viewHolder = new DefaultViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ReportBean reportBean = data.get(position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position,reportBean);
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
        if (holder instanceof FirstViewHolder) {
            ((FirstViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((FirstViewHolder)holder).footerTV.setText(reportBean.getFooter());
            for(int i = 0;i<reportBean.getSummary().size(); i ++){
                if(reportBean.getSummary().get(i).getType().equals("enterstock")){
                    ((FirstViewHolder) holder).enterStock.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("outstock")){
                    ((FirstViewHolder) holder).outStock.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("instock")){
                    ((FirstViewHolder) holder).inStock.setText(reportBean.getSummary().get(i).getData());
                }
            }
        }else if (holder instanceof SecondFiveViewHolder){
            ((SecondFiveViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((SecondFiveViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i =0 ;i <reportBean.getSummary().size();i++){
                if(reportBean.getSummary().get(i).getType().equals("privateSale") || reportBean.getSummary().get(i).getType().equals("bankCount")){
                    ((SecondFiveViewHolder) holder).privateSell.setText(reportBean.getSummary().get(i).getData());
                    ((SecondFiveViewHolder) holder).privateSellLabel.setText(reportBean.getSummary().get(i).getTypeDisplay());
                }else if(reportBean.getSummary().get(i).getType().equals("privateMove") || reportBean.getSummary().get(i).getType().equals("headBankCount")){
                    ((SecondFiveViewHolder) holder).privateMove.setText(reportBean.getSummary().get(i).getData());
                    ((SecondFiveViewHolder) holder).privateMoveLabel.setText(reportBean.getSummary().get(i).getTypeDisplay());
                }else if(reportBean.getSummary().get(i).getType().equals("unNormal") || reportBean.getSummary().get(i).getType().equals("branchBankCount")){
                    ((SecondFiveViewHolder) holder).abnormal.setText(reportBean.getSummary().get(i).getData());
                    ((SecondFiveViewHolder) holder).abnormalLabel.setText(reportBean.getSummary().get(i).getTypeDisplay());
                }else if(reportBean.getSummary().get(i).getType().equals("supervisorAbsence") || reportBean.getSummary().get(i).getType().equals("provinceCount")){
                    ((SecondFiveViewHolder) holder).absentCount.setText(reportBean.getSummary().get(i).getData());
                    ((SecondFiveViewHolder) holder).absentLabel.setText(reportBean.getSummary().get(i).getTypeDisplay());
                }else if(reportBean.getSummary().get(i).getType().equals("fiveDateNoOutStock")|| reportBean.getSummary().get(i).getType().equals("cityCount")){
                    ((SecondFiveViewHolder) holder).absentFiveCount.setText(reportBean.getSummary().get(i).getData());
                    ((SecondFiveViewHolder) holder).absentFiveLabel.setText(reportBean.getSummary().get(i).getTypeDisplay());
                }else if(reportBean.getSummary().get(i).getType().equals("zeroStockZeroOrder") || reportBean.getSummary().get(i).getType().equals("privateMove")){
                    ((SecondFiveViewHolder) holder).zeroLabel.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    ((SecondFiveViewHolder) holder).zeroCount.setText(reportBean.getSummary().get(i).getData());
                }
                if(getItemViewType(position) == FIVE){
                    ((SecondFiveViewHolder) holder).lines.setVisibility(View.GONE);
                    ((SecondFiveViewHolder) holder).thirdLl.setVisibility(View.GONE);
                    ((SecondFiveViewHolder) holder).titleImg.setImageResource(R.drawable.icon_deshb_financial);
                }else if(getItemViewType(position) == TOW){
                    ((SecondFiveViewHolder) holder).lines.setVisibility(View.VISIBLE);
                    ((SecondFiveViewHolder) holder).thirdLl.setVisibility(View.VISIBLE);
                    ((SecondFiveViewHolder) holder).titleImg.setImageResource(R.drawable.icon_deshb_financial);
                }
            }
        }else if (holder instanceof  ThreeViewHolder){
            ((ThreeViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((ThreeViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i = 0;i<reportBean.getSummary().size(); i ++){
                if(reportBean.getSummary().get(i).getType().equals("orderCount")){
                    ((ThreeViewHolder) holder).orderCount.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("invoiceCount")){
                    ((ThreeViewHolder) holder).invoiceCount.setText(Html.fromHtml(reportBean.getSummary().get(i).getTypeDisplay()+" <font color='#F7C28D'>"+reportBean.getSummary().get(i).getData()+"</font>"));
                }else if(reportBean.getSummary().get(i).getType().equals("clearCount")){
                    ((ThreeViewHolder) holder).clearCount.setText(Html.fromHtml(reportBean.getSummary().get(i).getTypeDisplay()+" <font color='#F7C28D'>"+reportBean.getSummary().get(i).getData()+"</font>"));
                }else if(reportBean.getSummary().get(i).getType().equals("balanceCount")){
                    ((ThreeViewHolder) holder).balanceCount.setText(Html.fromHtml(reportBean.getSummary().get(i).getTypeDisplay()+" <font color='#F7C28D'>"+reportBean.getSummary().get(i).getData()+"</font>"));
                }
            }
        }else if(holder instanceof FourViewHolder){
            ((FourViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((FourViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i = 0;i<reportBean.getSummary().size(); i ++){
                if(reportBean.getSummary().get(i).getType().equals("carCount") || reportBean.getSummary().get(i).getType().equals("shopCount")){
                    ((FourViewHolder) holder).secondLine.setText(reportBean.getSummary().get(i).getData());
                    ((FourViewHolder) holder).thirdLine.setText(reportBean.getSummary().get(i).getTypeDisplay());
                }else if(reportBean.getSummary().get(i).getType().equals("carTypeCount") || reportBean.getSummary().get(i).getType().equals("brandCount")){
                    ((FourViewHolder) holder).fourth_one_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    ((FourViewHolder) holder).fourth_one_right.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("provinceCount") || reportBean.getSummary().get(i).getType().equals("provinceCount")){
                    ((FourViewHolder) holder).fourth_tow_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    ((FourViewHolder) holder).fourth_tow_right.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("cityCount")|| reportBean.getSummary().get(i).getType().equals("cityCount")){
                    ((FourViewHolder) holder).fourth_three_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    ((FourViewHolder) holder).fourth_three_right.setText(reportBean.getSummary().get(i).getData());
                }
            }
        }else if(holder instanceof SevenElevenViewHolder){
            ((SevenElevenViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((SevenElevenViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i = 0;i<reportBean.getSummary().size(); i ++){
                if(reportBean.getHeaderId() == 7){
                    if(reportBean.getSummary().get(i).getType().equals("newInfo")){
                        ((SevenElevenViewHolder) holder).secondLine.setText(reportBean.getSummary().get(i).getData());
                        ((SevenElevenViewHolder) holder).thirdLine.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    }
                }else if(reportBean.getHeaderId() ==11){
                    if(reportBean.getSummary().get(i).getType().equals("deviceCount")){
                        ((SevenElevenViewHolder) holder).secondLine.setText(reportBean.getSummary().get(i).getData());
                        ((SevenElevenViewHolder) holder).thirdLine.setText("设备（个）");
                    }
                }
            }
        }else if(holder instanceof  EightTwelveViewHolder){
            ((EightTwelveViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((EightTwelveViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i = 0; i<reportBean.getSummary().size();i++){
                if(reportBean.getHeaderId()  == 8){
                    if(reportBean.getSummary().get(i).getType().equals("protocolCount")){
                        ((EightTwelveViewHolder) holder).left_top.setText(reportBean.getSummary().get(i).getData());
                        ((EightTwelveViewHolder) holder).left_bottom.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    }else if(reportBean.getSummary().get(i).getType().equals("signButNoEnter")){
                        ((EightTwelveViewHolder) holder).right_top_right.setText(reportBean.getSummary().get(i).getData());
                        ((EightTwelveViewHolder) holder).right_top_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    }else if(reportBean.getSummary().get(i).getType().equals("protocalNoRecycle")){
                        ((EightTwelveViewHolder) holder).right_bottom_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                        ((EightTwelveViewHolder) holder).right_bottom_right.setText(reportBean.getSummary().get(i).getData());
                    }
                }else if(reportBean.getHeaderId() == 12){
                    if(reportBean.getSummary().get(i).getType().equals("supervisorCount")){
                        ((EightTwelveViewHolder) holder).left_top.setText(reportBean.getSummary().get(i).getData());
                        ((EightTwelveViewHolder) holder).left_bottom.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    }else if(reportBean.getSummary().get(i).getType().equals("localCount")){
                        ((EightTwelveViewHolder) holder).right_top_right.setText(reportBean.getSummary().get(i).getData());
                        ((EightTwelveViewHolder) holder).right_top_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    }else if(reportBean.getSummary().get(i).getType().equals("nonLocalCount")){
                        ((EightTwelveViewHolder) holder).right_bottom_left.setText(reportBean.getSummary().get(i).getTypeDisplay());
                        ((EightTwelveViewHolder) holder).right_bottom_right.setText(reportBean.getSummary().get(i).getData());
                    }
                    ((EightTwelveViewHolder) holder).left_top.setTextColor(context.getResources().getColor(R.color.light_blue));
                }
            }
        }else if(holder instanceof NineViewHolder){
            ((NineViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((NineViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i= 0; i<reportBean.getSummary().size(); i++){
                if(reportBean.getSummary().get(i).getType().equals("videoUnFinished")){
                    ((NineViewHolder) holder).big_circle_title.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    ((NineViewHolder) holder).big_persent.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("IPQCUnFinished")){
                    ((NineViewHolder) holder).small_circle_title.setText(reportBean.getSummary().get(i).getTypeDisplay());
                    ((NineViewHolder) holder).small_persent.setText(reportBean.getSummary().get(i).getData());
                }
            }
        }else if(holder instanceof TenViewHolder){
            ((TenViewHolder) holder).titleTV.setText(reportBean.getHeader());
            ((TenViewHolder) holder).footerTV.setText(reportBean.getFooter());
            for(int i= 0; i<reportBean.getSummary().size(); i++){
                if(reportBean.getSummary().get(i).getType().equals("bankBillCount")){
                    ((TenViewHolder) holder).bankBillCount.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("supervisionFee")){
                    ((TenViewHolder) holder).supervisionFee.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("currentMonthEnterShop")){
                    ((TenViewHolder) holder).currentMonthEnterShop.setText(reportBean.getSummary().get(i).getData());
                }else if(reportBean.getSummary().get(i).getType().equals("currentMonthRevokeShop")){
                    ((TenViewHolder) holder).currentMonthRevokeShop.setText(reportBean.getSummary().get(i).getData());
                }
            }
        }else{
            return;
//            Glide.with(context).load(reportBean.getDefaultUrl());
        }
    }

    class FirstViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView enterStock;
        TextView outStock;
        TextView inStock;
        TextView footerTV;
        public FirstViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            enterStock = (TextView) convertView.findViewById(R.id.enterStock);
            outStock = (TextView) convertView.findViewById(R.id.outStock);
            inStock = (TextView) convertView.findViewById(R.id.inStock);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
        }
    }

    class SecondFiveViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        ImageView titleImg;
        TextView privateSell;
        TextView privateSellLabel;
        TextView privateMove;
        TextView privateMoveLabel;
        TextView abnormal;
        TextView abnormalLabel;
        TextView absentLabel;
        TextView absentCount;
        TextView absentFiveLabel;
        TextView absentFiveCount;
        TextView zeroLabel;
        TextView zeroCount;
        TextView footerTV;
        View lines;
        LinearLayout thirdLl;
        public SecondFiveViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            titleImg = (ImageView) convertView.findViewById(R.id.titleImg);
            privateSell = (TextView) convertView.findViewById(R.id.privateSell);
            privateSellLabel = (TextView) convertView.findViewById(R.id.privateSellLabel);
            privateMove = (TextView) convertView.findViewById(R.id.privateMove);
            privateMoveLabel = (TextView) convertView.findViewById(R.id.privateMoveLabel);
            abnormal = (TextView) convertView.findViewById(R.id.abnormal);
            abnormalLabel = (TextView) convertView.findViewById(R.id.abnormalLabel);
            absentLabel = (TextView) convertView.findViewById(R.id.absentLabel);
            absentCount = (TextView) convertView.findViewById(R.id.absentCount);
            absentFiveLabel = (TextView) convertView.findViewById(R.id.absentFiveLabel);
            absentFiveCount = (TextView) convertView.findViewById(R.id.absentFiveCount);
            zeroLabel = (TextView) convertView.findViewById(R.id.zeroLabel);
            zeroCount = (TextView) convertView.findViewById(R.id.zeroCount);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
            lines = convertView.findViewById(R.id.lines);
            thirdLl = (LinearLayout) convertView.findViewById(R.id.thirdLl);
        }
    }

    class ThreeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView orderCount;
        TextView orderCountLabel;
        TextView clearCount;
        TextView invoiceCount;
        TextView balanceCount;
        TextView footerTV;
        public ThreeViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            orderCount = (TextView) convertView.findViewById(R.id.orderCount);
            orderCountLabel = (TextView) convertView.findViewById(R.id.orderCountLabel);
            clearCount = (TextView) convertView.findViewById(R.id.clearCount);
            invoiceCount = (TextView) convertView.findViewById(R.id.invoiceCount);
            balanceCount = (TextView) convertView.findViewById(R.id.balanceCount);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
        }
    }

    class FourViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView footerTV;
        TextView secondLine;
        TextView thirdLine;
        TextView fourth_one_left;
        TextView fourth_one_right;
        TextView fourth_tow_left;
        TextView fourth_tow_right;
        TextView fourth_three_left;
        TextView fourth_three_right;
        public FourViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            secondLine = (TextView) convertView.findViewById(R.id.secondLine);
            thirdLine = (TextView) convertView.findViewById(R.id.thirdLine);
            fourth_one_left = (TextView) convertView.findViewById(R.id.fourth_one_left);
            fourth_one_right = (TextView) convertView.findViewById(R.id.fourth_one_right);
            fourth_tow_left = (TextView) convertView.findViewById(R.id.fourth_tow_left);
            fourth_tow_right = (TextView) convertView.findViewById(R.id.fourth_tow_right);
            fourth_three_left = (TextView) convertView.findViewById(R.id.fourth_three_left);
            fourth_three_right = (TextView) convertView.findViewById(R.id.fourth_three_right);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
        }
    }

    class SevenElevenViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView footerTV;
        TextView secondLine;
        TextView thirdLine;
        public SevenElevenViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            secondLine = (TextView) convertView.findViewById(R.id.secondLine);
            thirdLine = (TextView) convertView.findViewById(R.id.thirdLine);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
        }
    }

    class EightTwelveViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView footerTV;
        TextView left_top;
        TextView left_bottom;
        TextView right_top_left;
        TextView right_top_right;
        TextView right_bottom_left;
        TextView right_bottom_right;
        public EightTwelveViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
            left_top = (TextView) convertView.findViewById(R.id.left_top);
            left_bottom = (TextView) convertView.findViewById(R.id.left_bottom);
            right_top_right = (TextView) convertView.findViewById(R.id.right_top_right);
            right_bottom_left = (TextView) convertView.findViewById(R.id.right_bottom_left);
            right_top_left = (TextView) convertView.findViewById(R.id.right_top_left);
            right_bottom_right = (TextView) convertView.findViewById(R.id.right_bottom_right);
        }
    }

    class NineViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView footerTV;
        TextView big_circle_title;
        TextView big_persent;
        TextView big_status;
        TextView small_circle_title;
        TextView small_persent;
        TextView small_status;
        public NineViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            big_circle_title = (TextView) convertView.findViewById(R.id.big_circle_title);
            big_persent = (TextView) convertView.findViewById(R.id.big_persent);
            big_status = (TextView) convertView.findViewById(R.id.big_status);
            small_circle_title = (TextView) convertView.findViewById(R.id.small_circle_title);
            small_persent = (TextView) convertView.findViewById(R.id.small_persent);
            small_status = (TextView) convertView.findViewById(R.id.small_status);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
        }
    }

    class TenViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView footerTV;
        TextView bankBillCount;
        TextView supervisionFee;
        TextView currentMonthEnterShop;
        TextView currentMonthRevokeShop;
        public TenViewHolder(View convertView) {
            super(convertView);
            titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            bankBillCount = (TextView) convertView.findViewById(R.id.bankBillCount);
            supervisionFee = (TextView) convertView.findViewById(R.id.supervisionFee);
            currentMonthEnterShop = (TextView) convertView.findViewById(R.id.currentMonthEnterShop);
            currentMonthRevokeShop = (TextView) convertView.findViewById(R.id.currentMonthRevokeShop);
            footerTV = (TextView) convertView.findViewById(R.id.footerTV);
        }
    }

    class DefaultViewHolder extends RecyclerView.ViewHolder{
        ImageView default_img;
        public DefaultViewHolder(View convertView){
            super(convertView);
            default_img = (ImageView) convertView.findViewById(R.id.default_img);
        }
    }
}
