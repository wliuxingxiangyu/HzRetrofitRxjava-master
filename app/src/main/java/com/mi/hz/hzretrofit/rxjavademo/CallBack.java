package com.mi.hz.hzretrofit.rxjavademo;

/**
 * Created by mi on 17-4-11.
 */

public interface CallBack<T> {

    void onSuccess(T result);

    void onFailure(String msg);
}
