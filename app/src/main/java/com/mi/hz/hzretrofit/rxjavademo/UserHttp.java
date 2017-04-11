package com.mi.hz.hzretrofit.rxjavademo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by mi on 17-4-11.
 */

public class UserHttp {

    /**
     * 本方法模拟网络请求，并不是真正请求http
     *
     * @param url
     * @param callBack
     */
    public void post(String url, int userid, final CallBack<String> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟2秒网络请求
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // 主线程回调
                    @Override
                    public void run() {
                        if (callBack != null)
                            callBack.onSuccess("{\"ret\":200,\"user\":{\"userid\":1,\"name\":\"username-post-normal\"}}");
                    }
                }, 2000);
            }
        }).start();
    }

    /**
     * 同步请求User数据（模拟）
     *
     * @param url
     * @param userid
     */
    public Response post(String url, int userid) {
        Response response = new Response();
        response.setStatus(200);
        response.setResult("{ \"userid\"   :2,   \"name\"  :  \"username-post-rxjava\"   }");
        Log.d("hz--","response.getResult()="+response.getResult());
        return response;
    }
}
