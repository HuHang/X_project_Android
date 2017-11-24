package com.iftech.car.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * created by tanghuosong 2017/5/8
 * description:
 **/
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isAll = true;

    public SpacesItemDecoration(int space , boolean isAll) {
        this.space = space;
        this.isAll = isAll;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if(isAll){
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;
        }else{
            outRect.top = space;
            outRect.bottom = space;
        }
    }
}