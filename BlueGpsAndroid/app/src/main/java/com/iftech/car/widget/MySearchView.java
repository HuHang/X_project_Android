package com.iftech.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.iftech.car.R;

/**
 * created by tanghuosong 2017/5/26
 * description: 自定义搜索框
 **/
public class MySearchView extends RelativeLayout{

    private TextView search_hit;
    private TextView search_cancel;
    private LinearLayout search_ll;
    private EditText search_et;

    public OnSearchListener onSearchListener;

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.rl_search, null));
        initSearchView(context);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    private void initSearchView(final Context context){
        search_hit = (TextView) findViewById(R.id.search_hit);
        search_ll = (LinearLayout) findViewById(R.id.search_ll);
        search_et = (EditText) findViewById(R.id.search_et);
        search_cancel = (TextView) findViewById(R.id.search_cancel);
        search_et.clearFocus();
        search_et.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    search_ll.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                    search_hit.setVisibility(View.GONE);
                    search_cancel.setVisibility(View.VISIBLE);
                }else{
                    search_ll.setGravity(Gravity.CENTER);
                    search_hit.setVisibility(View.VISIBLE);
                    search_cancel.setVisibility(View.GONE);
                }
            }
        });
        search_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
                search_et.clearFocus();
                search_ll.requestFocus();
                search_ll.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                if (isOpen) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    onSearchListener.searchClick(v);
                }
                return true;
            }
        });
    }

    public interface OnSearchListener{
        void searchClick(TextView view);
    }
}