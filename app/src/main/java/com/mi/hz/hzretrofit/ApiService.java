package com.mi.hz.hzretrofit;

/**
 * Created by mi on 17-4-8.
 */

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.http.Headers;
import rx.Observable;

import com.mi.hz.hzretrofit.model.BaseModel;
import com.mi.hz.hzretrofit.model.Bean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public  interface ApiService {
    @Headers("Cache-Control: public, max-age=3600000")
    @GET("api/data/{type}/{pageCount}/{pageIndex}")
    Call<BaseModel<ArrayList<Bean>>> listBeanNormal(
            @Path("type") String type,
            @Path("pageCount") int pageCount,
            @Path("pageIndex") int pageIndex
    );

    @GET("api/data/{type}/{pageCount}/{pageIndex}")
    Call<BaseModel<ArrayList<Bean>>> listBeanRxjava(
            @Path("type") String type,
            @Path("pageCount") int pageCount,
            @Path("pageIndex") int pageIndex
    );
}
