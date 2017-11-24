package com.iftech.car.trace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.iftech.car.R;
import com.iftech.car.trace.bean.DeviceInfoBean;
import com.iftech.car.trace.bean.MonitorBean;
import com.iftech.car.trace.bean.ShopBean;
import com.iftech.car.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorExpandListViewAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter{

    private List<MonitorBean> monitorBeans = new ArrayList<>();
    private LayoutInflater inflater;
    private PinnedHeaderExpandableListView expandableListView;

    ParentViewHolder parentViewHolder;
    ChildViewHolder childViewHolder;
    public OnclickListener onclickListener;

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public MonitorExpandListViewAdapter(Context context,PinnedHeaderExpandableListView expandableListView){
        this.inflater = LayoutInflater.from(context);
        this.expandableListView = expandableListView;
    }

    public interface OnclickListener{
        void OnChildClick(int position, DeviceInfoBean deviceInfoBean);
    }

    public void setData(List<MonitorBean> list){
        this.monitorBeans = list;
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ShopBean parentShop = monitorBeans.get(groupPosition).getShop();
        if(convertView == null){
            parentViewHolder = new ParentViewHolder();
            convertView = inflater.inflate( R.layout.item_monitor_parent, null);
            parentViewHolder.arrowRight = (ImageView) convertView.findViewById(R.id.arrowRight);
            parentViewHolder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            parentViewHolder.car_count = (TextView) convertView.findViewById(R.id.car_count);
            parentViewHolder.shop_bank = (TextView) convertView.findViewById(R.id.parent_shop_bank);
            convertView.setTag(parentViewHolder);
        }else{
            parentViewHolder = (ParentViewHolder)convertView.getTag();
        }
        parentViewHolder.shop_name.setText(parentShop.getName());
        parentViewHolder.shop_bank.setText("金融机构："+parentShop.getAllBankPath());

        if (parentShop.getShopType() == 0){
            parentViewHolder.car_count.setText("在库："+getChildrenCount(groupPosition));
            parentViewHolder.arrowRight.setImageResource(R.drawable.icon_map_4sstore);
        }else if (parentShop.getShopType() == 2){
            parentViewHolder.car_count.setText("移动："+getChildrenCount(groupPosition));
            parentViewHolder.arrowRight.setImageResource(R.drawable.icon_map_twolibrary);
        }else {
            parentViewHolder.car_count.setText("移动："+getChildrenCount(groupPosition));
            parentViewHolder.arrowRight.setImageResource(R.drawable.icon_map_twonet);
        }



//        if(parentShop.getShopType() == 2 || parentShop.getShopType() == 3){
//            parentViewHolder.shop_bank.setVisibility(View.GONE);
//        }else if(parentShop.getShopType() == 1){
//            parentViewHolder.shop_bank.setVisibility(View.VISIBLE);
//        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final DeviceInfoBean deviceInfoBean = getChild(groupPosition,childPosition);
        if(convertView == null){
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.item_monitor_child,null);
            childViewHolder.vinTV = (TextView) convertView.findViewById(R.id.vinTV);
            childViewHolder.fenceStateTV = (TextView) convertView.findViewById(R.id.fenceStateTV);
            childViewHolder.iMeiTV = (TextView) convertView.findViewById(R.id.iMeiTV);
            childViewHolder.shopTV = (TextView) convertView.findViewById(R.id.shopTV);
            childViewHolder.carBrandTV = (TextView) convertView.findViewById(R.id.carBrandTV);
            childViewHolder.carTypeTV = (TextView) convertView.findViewById(R.id.carTypeTV);
            childViewHolder.timeTV = (TextView) convertView.findViewById(R.id.timeTV);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.vinTV.setText("VIN："+deviceInfoBean.getVin());
        childViewHolder.fenceStateTV.setText(deviceInfoBean.getFenceStateDisplay());
        childViewHolder.iMeiTV.setText("IMEI："+deviceInfoBean.getImei());
        childViewHolder.shopTV.setText("当前商户："+deviceInfoBean.getCurrentShopName());
        childViewHolder.carBrandTV.setText("品牌："+deviceInfoBean.getBrand());
        childViewHolder.carTypeTV.setText("车型："+deviceInfoBean.getCarType());
        childViewHolder.timeTV .setText("定位时间："+deviceInfoBean.getSignalTime().substring(0,19).replace("T"," "));
        if(deviceInfoBean.getFenceStateDisplay().equals("围栏内")){
            childViewHolder.fenceStateTV.setTextColor(convertView.getResources().getColor(R.color.gray_deep));
        }else if(deviceInfoBean.getFenceStateDisplay().equals("围栏外")){
            childViewHolder.fenceStateTV.setTextColor(convertView.getResources().getColor(R.color.colorPrimary));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListener.OnChildClick(childPosition,deviceInfoBean);
            }
        });
        return convertView;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public DeviceInfoBean getChild(int groupPosition, int childPosition) {
        return monitorBeans.get(groupPosition).getCars().get(childPosition);
    }
    @Override
    public ShopBean getGroup(int groupPosition) {
        return monitorBeans.get(groupPosition).getShop();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return monitorBeans.get(groupPosition).getCars().size();
    }
    @Override
    public int getGroupCount() {
        return monitorBeans.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }


    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1 && !expandableListView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition,int childPosition, int alpha) {
        ShopBean shopBean = this.getGroup(groupPosition);
        ((TextView) header.findViewById(R.id.shop_name)).setText(shopBean.getName()+"("+getChildrenCount(groupPosition)+")");
        ((TextView) header.findViewById(R.id.parent_shop_bank)).setText("金融机构："+shopBean.getAllBankPath());
    }

    private HashMap<Integer, Integer> groupStatusMap = new HashMap<Integer, Integer>();

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.containsKey(groupPosition)) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

    final static class ParentViewHolder{
        private ImageView arrowRight;
        private TextView car_count;
        private TextView shop_name;
        private TextView shop_bank;
    }

    final static class ChildViewHolder{
        private TextView vinTV;
        private TextView fenceStateTV;
        private TextView shopTV;
        private TextView iMeiTV;
        private TextView carBrandTV;
        private TextView carTypeTV;
        private TextView timeTV;
    }

}
