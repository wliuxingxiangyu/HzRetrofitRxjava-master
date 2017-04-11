package com.mi.hz.hzretrofit.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mi.hz.hzretrofit.R;
import com.mi.hz.hzretrofit.rxjavademo.CallBack;
import com.mi.hz.hzretrofit.rxjavademo.Response;
import com.mi.hz.hzretrofit.rxjavademo.User;
import com.mi.hz.hzretrofit.rxjavademo.UserDAO;
import com.mi.hz.hzretrofit.rxjavademo.UserHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mi on 17-4-11.
 */

public class ActivityRxjava extends Activity implements View.OnClickListener {
    private TextView textView;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);


        textView = (TextView) findViewById(R.id.tv_hello);

        findViewById(R.id.btn_post).setOnClickListener(this);
        findViewById(R.id.btn_rxjava_post).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                postNormal(1);
                break;

            case R.id.btn_rxjava_post:
                PostRxjva(1);
                break;
        }
    }

    /**
     * 普通网络请求
     *
     * @param userid
     */
    private void postNormal(int userid) {
        UserHttp http = new UserHttp();
        http.post("http://server.com/user", userid, new CallBack<String>() {
            @Override
            public void onSuccess(String result) { // 主线程 回调
                User user = new UserDAO().parseStr(result);
                textView.setText(user.getName());
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    private void PostRxjva(final int userid) {
        final Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(userid));// 请求参数

        //第一步.创建被观察者 observable。计划表OnSubscribe存入被观察者 对象中。
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {  //create创造事件序列
            @Override
            public void call(Subscriber<? super String> subscriber) {
//                logThread("create call(Subscriber)");

                UserHttp client = new UserHttp();
                Response response = client.post("http://kkmike999.com/user", userid);// 同步请求

                if (response.getStatus() == 200) {
                    // 请求成功
                    Log.d("hz--2","subscribe--call()成功--response.getResult().toString()="+response.getResult().toString());
//                    subscriber.onNext(response.getResult());
//                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("请求失败"));
                }
            }
        }).subscribeOn(Schedulers.newThread());// call()方法在 新线程 Thread1回调

        //第二步.被观察者 observable。
        observable
                .map(new Func1<String, JSONObject>() { //map将 string 转换为 JSONObject。
                    @Override
                    public JSONObject call(String result) {// Thread1回调
//                        logThread("map call(String)");

                        try {
                            Log.d("hz--3","subscribe--call()--result.toString()="+result.toString());
                            return new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return new JSONObject();
                    }
                })

                .observeOn(Schedulers.newThread()) // 切换线程Thread2
                .map(new Func1<JSONObject, User>() { //map将 JSONObject 转换为 User。
                    @Override
                    public User call(JSONObject json) {// Thread2回调
//                        logThread("map call(JSONObject)");
                        Log.d("hz--4","subscribe--call()--json.toString()="+json.toString());//-------
                        UserDAO userDao = new UserDAO();
                        User user = userDao.parseJO(json);
                        return user;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())   // 切换回mainThread
                .subscribe(new Subscriber<User>() { //“被观察者observable” 订阅subscribe “观察者Subscriber”，让观察者 在 主线程中更新UI。

                    @Override
                    public void onStart() {//订阅之前的调用。清数据。
                        Log.d("hz--1","subscribe--onStart()");
                        progressDialog = ProgressDialog.show(ActivityRxjava.this, "自动关闭对话框", "Working,,,,,,1秒", true, false);
                        progressDialog.show();
                    }

                    @Override
                    public void onNext(User user) {

//                        logThread("onNext(User)");
                        Log.d("hz--5","subscribe--onNext()--user.getName()="+user.getName());
                        textView.setText(user.getName());
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("hz--6","subscribe--onCompleted()");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("hz--","subscribe--onError()");
                    }
                });
    }

    /***
     * 输出线程名
     * @param method
     */
    private void logThread(String method) {
        Log.i("hz--"+method, Thread.currentThread().toString());
    }
}
