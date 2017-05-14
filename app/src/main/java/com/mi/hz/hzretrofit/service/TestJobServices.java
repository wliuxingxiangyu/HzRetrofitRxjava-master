package com.mi.hz.hzretrofit.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

/**
 * Created by lenovo on 17/5/14.
 */
public class TestJobServices extends JobService{
    private final static String TAG = "TestJobServices";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("hz--",TAG+"onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("hz--",TAG+"onStopJob");
        return false;
    }

}
