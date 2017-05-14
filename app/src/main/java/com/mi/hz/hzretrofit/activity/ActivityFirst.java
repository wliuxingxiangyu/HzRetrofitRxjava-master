package com.mi.hz.hzretrofit.activity;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mi.hz.hzretrofit.R;
import com.mi.hz.hzretrofit.service.TestJobServices;

public class ActivityFirst extends Activity implements View.OnClickListener {
    private String mType;
    private final static String TAG = "ActivityFirst";
    private final static long WAKE_INTERVAL = 2*60*1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        findViewById(R.id.btn_start_load_welfare).setOnClickListener(this);
        findViewById(R.id.btn_start_load_android).setOnClickListener(this);
        Button buttonNormal = (Button) findViewById(R.id.btn_start_load_all);
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

        Button buttonRx = (Button) findViewById(R.id.btn_get_rxjava);
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

        Button jobScheduleBtn = (Button) findViewById(R.id.btn_jobSchedule);
        jobScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setJobSchdule();
            }
        });

    }

    public void setJobSchdule() {
        Log.i(TAG, "hz--开启 JobService 定时");
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        JobInfo.Builder builder = new JobInfo.Builder(1024, new ComponentName(getPackageName(), TestJobServices.class.getName()));
        builder.setPeriodic(WAKE_INTERVAL);
//        builder.setRequiresCharging(true);
        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        int schedule = jobScheduler.schedule(builder.build());
        if (schedule <= 0) {
            Log.w(TAG, "schedule error！");
        }
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
