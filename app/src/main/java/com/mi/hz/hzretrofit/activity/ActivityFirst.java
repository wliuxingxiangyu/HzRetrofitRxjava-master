package com.mi.hz.hzretrofit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mi.hz.hzretrofit.R;

public class ActivityFirst extends Activity implements View.OnClickListener {
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        findViewById(R.id.btn_start_load_welfare).setOnClickListener(this);
        findViewById(R.id.btn_start_load_android).setOnClickListener(this);
        Button buttonNormal =(Button)findViewById(R.id.btn_start_load_all);
        buttonNormal.setText("btn_get_by_callback");
        buttonNormal.setOnClickListener(this);

        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(ActivityFirst.this, ActivityRxjava.class);
                startActivity(in);
            }
        });

        Button buttonRx =(Button)findViewById(R.id.btn_get_rxjava);
        buttonRx.setText("btn_get_by_rxjava");
        buttonRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(ActivityFirst.this, ActivitySecondRxjava.class);
                in.putExtra("type", "all");
                startActivity(in);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_load_welfare:
                mType = "福利";
                break;

            case R.id.btn_start_load_android:
                mType = "Android";
                break;

            case R.id.btn_start_load_all:
                mType = "all";
                break;
        }

        Intent in = new Intent();
        in.setClass(ActivityFirst.this, ActivitySecond.class);
        in.putExtra("type", mType);
        startActivity(in);
    }
}
