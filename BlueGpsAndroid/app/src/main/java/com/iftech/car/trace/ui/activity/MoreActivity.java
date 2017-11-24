package com.iftech.car.trace.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iftech.car.R;

public class MoreActivity extends AppCompatActivity {
    @InjectView(R.id.title)
    Toolbar toolbar;
    @InjectView(R.id.back)
    TextView back;
    @InjectView(R.id.toolbar_title)
    TextView title_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.inject(this);
        title_text.setText(R.string.more);
        initView();
        initEvents();
    }

    private void initView(){

    }

    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
