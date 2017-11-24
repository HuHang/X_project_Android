package com.iftech.car.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.iftech.car.R;

/***
 * created by tanghuosong 2017/5/27
 * description: 自定义TabBar
 ***/
public class MyTabBar extends RelativeLayout{

    private int [] textViews = {R.string.index,R.string.message,R.string.user_center};
    private int [] iconNormal = { R.drawable.icon_tab_index_nomal, R.drawable.icon_tab_message_nomal, R.drawable.icon_tab_user_nomal };
    private int [] iconSelect = { R.drawable.icon_tab_index_selected, R.drawable.icon_tab_message_selected, R.drawable.icon_tab_user_selected };
    private int selectColor = R.color.colorPrimary;
    private int defaultColor = R.color.gray_deep;

    private LinearLayout selectLineLayout;
    private int selectTag = 0;

    private OnItemMenuClick onItemMenuClick;

    public void setOnItemMenuClick(OnItemMenuClick onItemMenuClick) {
        this.onItemMenuClick = onItemMenuClick;
    }

    public MyTabBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

          /* 显示的item个数*/
        int count = textViews.length;

        LinearLayout linearLayoutRoot = new LinearLayout(context);
        linearLayoutRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayoutRoot.setBackgroundColor(Color.WHITE);
        addView(linearLayoutRoot);

        for(int i=0 ; i <count;i++) {

        /* 实例化子布局 */
            final LinearLayout linearLayoutChild = new LinearLayout(context);
            linearLayoutChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1));
            linearLayoutChild.setGravity(Gravity.CENTER);
            linearLayoutChild.setOrientation(LinearLayout.VERTICAL);
            linearLayoutChild.setWeightSum(1);
            linearLayoutChild.setTag(i);

        /* 实例化图标 */
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(context,26),dip2px(context,26));
            lp.setMargins(0, 5, 0, 0);
            imageView.setLayoutParams(lp);
            imageView.setImageResource(iconNormal[i]);
            imageView.setTag("image_"+i);
            linearLayoutChild.addView(imageView);

        /* 实例化文字 */
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            TextView textView = new TextView(context);
            textView.setText(textViews[i]);
            textView.setTextSize(14);
            textView.setTextColor(defaultColor);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(lp);
            textView.setTag("text_"+i);
            linearLayoutChild.addView(textView);
        /* item 的点击事件 */
            linearLayoutChild.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelect(v);
                    onItemMenuClick.onThisClick((int)v.getTag());
                }
            });
        /* 将该布局添加到根布局中 */
            linearLayoutRoot.addView(linearLayoutChild);
        }

        /* 设置默认选中项，即第一项 */
        selectLineLayout = (LinearLayout) (linearLayoutRoot.findViewWithTag(selectTag));
        /* 设置选中的图片 */
        ImageView imageView = (ImageView)(selectLineLayout.findViewWithTag("image_"+selectLineLayout.getTag()));
        imageView.setImageResource(iconSelect[(int)selectLineLayout.getTag()]);
        /* 设置选中的字体颜色 */
        TextView textView = (TextView)(selectLineLayout.findViewWithTag("text_"+selectLineLayout.getTag()));
        textView.setTextColor(Color.RED);

    }

    private void setSelect(View view){
        if(selectTag != (int) view.getTag()){
            /* 将上一次选中的图片颜色回复为未选中状态
               设置选中的图片 */
                ImageView imageView1 = (ImageView)(selectLineLayout.findViewWithTag("image_"+selectTag));
                imageView1.setImageResource(iconNormal[selectTag]);
            /* 设置选中的字体颜色 */
                TextView textView1 = (TextView)(selectLineLayout.findViewWithTag("text_"+selectTag));
                textView1.setTextColor(getContext().getResources().getColor(defaultColor));

            /* 重置选中的Tag */
                selectTag = (int) view.getTag();
            /* 设置选中的图片 */
                ImageView imageView = (ImageView)(view.findViewWithTag("image_"+view.getTag()));
                imageView.setImageResource(iconSelect[(int)view.getTag()]);
            /* 设置选中的字体颜色 */
                TextView textView = (TextView)(view.findViewWithTag("text_"+view.getTag()));
                textView.setTextColor(getContext().getResources().getColor(selectColor));
            /* 将选择的项存入全局变量，用于下一次点击更新控件*/
                selectLineLayout = (LinearLayout) view;
        }
    }

    public interface OnItemMenuClick{
        void onThisClick(int eachItem);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
