package com.iftech.car.trace.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iftech.car.R;
import com.iftech.car.trace.bean.ShopBean;
import com.iftech.car.utils.CharacterParser;
import com.iftech.car.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tanghuosong on 2017/3/17.
 * description:
 */
public class ShopAdapter extends BaseExpandableListAdapter {

    private List<ShopBean> shopList = new ArrayList<>();
    private LayoutInflater inflater;
    private boolean isSimple;
    private List<ShopBean> shopChecked = new ArrayList<>();
    ParentViewHolder parentViewHolder;
    ChildViewHolder childViewHolder;

    public ShopAdapter(Context context, List<ShopBean> shops, boolean simple){
        this.shopList = shops;
        this.inflater = LayoutInflater.from(context);
        this.isSimple = simple;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return shopList.get(groupPosition).getChildShops().size();
    }

    @Override
    public int getGroupCount() {
        return shopList.size();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ShopBean parentShop = getGroup(groupPosition);
        if(convertView==null){
            parentViewHolder = new ParentViewHolder();
            convertView = inflater.inflate( R.layout.item_shop_parent, null);
            parentViewHolder.parent_shop_name = (TextView) convertView.findViewById(R.id.parent_shop_name);
            parentViewHolder.parent_shop_address = (TextView) convertView.findViewById(R.id.parent_shop_address);
            parentViewHolder.parent_shop_add = (ImageView) convertView.findViewById(R.id.parent_shop_add);
            parentViewHolder.parent_shop_bank = (TextView) convertView.findViewById(R.id.parent_shop_bank);
            parentViewHolder.catalog = (TextView) convertView.findViewById(R.id.catalog);
            parentViewHolder.arrowRight = (ImageView) convertView.findViewById(R.id.arrowRight);
            parentViewHolder.cb_layout = (LinearLayout)convertView.findViewById(R.id.cb_layout);
            convertView.setTag(parentViewHolder);
        }else{
            parentViewHolder = (ParentViewHolder)convertView.getTag();
        }
        parentViewHolder.parent_shop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShopList(parentShop,groupPosition,true);
            }
        });
        if(parentShop.isChecked()){
            parentViewHolder.parent_shop_add.setImageResource(R.drawable.icon_select);
        }else{
            parentViewHolder.parent_shop_add.setImageResource(R.drawable.icon_unselect);
        }
        if(isExpanded){
            parentViewHolder.arrowRight.setImageResource(R.drawable.arrow_down_red);
        }else{
            parentViewHolder.arrowRight.setImageResource(R.drawable.arrow_right_red);
        }
        /*-设置字母导航栏---------------*/
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(groupPosition);
        // group判断展开的是第几个
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (groupPosition == getGroupPositionForSection(section)) {
            parentViewHolder.catalog.setVisibility(View.VISIBLE);
            parentViewHolder.catalog.setText(shopList.get(groupPosition).getSortLetters());
            parentViewHolder.cb_layout.setVisibility(View.VISIBLE);
        } else {
            parentViewHolder.catalog.setVisibility(View.GONE);
            parentViewHolder.cb_layout.setVisibility(View.GONE);
        }
        if(getChildrenCount(groupPosition)==0){
            parentViewHolder.arrowRight.setVisibility(View.GONE);
        }else{
            parentViewHolder.arrowRight.setVisibility(View.VISIBLE);
        }
        parentViewHolder.parent_shop_name.setText(parentShop.getName()+"("+getChildrenCount(groupPosition)+")");
        parentViewHolder.parent_shop_address.setText(parentShop.getAddress());
        parentViewHolder.parent_shop_bank.setText(parentShop.getAllBankPath());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ShopBean childShop = getChild(groupPosition,childPosition);
        if(convertView == null){
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.item_shop_child_,null);
            childViewHolder.child_shop_type = (TextView) convertView.findViewById(R.id.child_shop_type);
            childViewHolder.child_shop_name = (TextView) convertView.findViewById(R.id.child_shop_name);
            childViewHolder.child_shop_address = (TextView) convertView.findViewById(R.id.child_shop_address);
            childViewHolder.child_shop_add = (ImageView) convertView.findViewById(R.id.child_shop_add);
            childViewHolder.topLine = convertView.findViewById(R.id.topLine);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if(childPosition == 0){
            childViewHolder.topLine.setVisibility(View.VISIBLE);
        }else{
            childViewHolder.topLine.setVisibility(View.GONE);
        }
        if((childShop.getShopType())==2){
            childViewHolder.child_shop_type.setText("二库");
        }else{
            childViewHolder.child_shop_type.setText("二网");
        }

        childViewHolder.child_shop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShopList(childShop,groupPosition,false);
            }
        });
        if(childShop.isChecked()){
            childViewHolder.child_shop_add.setImageResource(R.drawable.icon_select);
        }else{
            childViewHolder.child_shop_add.setImageResource(R.drawable.icon_unselect);
        }
        childViewHolder.child_shop_name.setText(childShop.getName());
        childViewHolder.child_shop_address.setText(childShop.getAddress());
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
    public ShopBean getChild(int groupPosition, int childPosition) {
        return shopList.get(groupPosition).getChildShops().get(childPosition);
    }
    @Override
    public ShopBean getGroup(int groupPosition) {
        return shopList.get(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    public List<ShopBean> getShopChecked() {
        return shopChecked;
    }

    public void setShopChecked(List<ShopBean> shopChecked) {
        this.shopChecked = shopChecked;
    }

    // 设置已选项
    public void setShopList(ShopBean shop,int groupPosition,boolean isParent){
        if(isSimple){  // 单选
            for(ShopBean item : shopChecked){
                item.setChecked(false);
            }
            shopChecked.clear();
            shop.setChecked(true);
            shopChecked.add(shop);
        }else{ // 多选
            if(isParent){ //组点击
                if(shop.isChecked()){
                    shop.setChecked(false);
                    shopChecked.remove(shop);
                    List<ShopBean> childShops =  shopList.get(groupPosition).getChildShops();
                    for(ShopBean shopBean : childShops){
                        shopBean.setChecked(false);
                        shopChecked.remove(shopBean);
                    }
                }else{
                    shop.setChecked(true);
                    shopChecked.add(shop);
                    List<ShopBean> childShops =  shopList.get(groupPosition).getChildShops();
                    for(ShopBean shopBean : childShops){
                        shopBean.setChecked(true);
                        shopChecked.add(shopBean);
                    }
                }
            }else{ // 子点击
                if(shop.isChecked()){
                    shop.setChecked(false);
                    shopChecked.remove(shop);
                }else{
                    shop.setChecked(true);
                    shopChecked.add(shop);
                }
            }
        }
        ShopAdapter.this.notifyDataSetChanged();

    }


    final static class ParentViewHolder{
        private TextView parent_shop_name;
        private TextView parent_shop_address;
        private TextView parent_shop_bank;
        private ImageView parent_shop_add;
        private TextView catalog;
        private ImageView arrowRight;
        private LinearLayout cb_layout;
    }

    final static class ChildViewHolder{
        private TextView child_shop_type;
        private TextView child_shop_name;
        private TextView child_shop_address;
        private ImageView child_shop_add;
        private View topLine;
    }

    public int getGroupPositionForSection( int section) {
        for(int i = 0;i<getGroupCount();i++){
            String sortStr = shopList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int getGroupPosition) {
        // 获取数据位置
        return shopList.get(getGroupPosition).getSortLetters().charAt(0);
    }

    /**
     * 按照字母排序返回
     * */
    public static List<ShopBean> setData(List<ShopBean> date) {
        List<ShopBean> mSortList = new ArrayList<>();
        CharacterParser characterParser = new CharacterParser();
        PinyinComparator pinyinComparator = new PinyinComparator();
        for (int i = 0; i < date.size(); i++) {
            ShopBean shop = date.get(i);
            ShopBean sortModel = new ShopBean();
            sortModel.setName(shop.getName());
            sortModel.setAddress(shop.getAddress());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        // 按照字母排序
        Collections.sort(date,pinyinComparator);
        return mSortList;
    }
}
