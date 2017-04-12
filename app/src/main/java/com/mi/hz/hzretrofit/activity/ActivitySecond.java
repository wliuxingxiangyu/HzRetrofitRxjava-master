package com.mi.hz.hzretrofit.activity;

import android.content.Intent;
import android.database.Observable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi.hz.hzretrofit.ApiService;
import com.mi.hz.hzretrofit.R;
import com.mi.hz.hzretrofit.model.BaseListAdapter;
import com.mi.hz.hzretrofit.model.BaseModel;
import com.mi.hz.hzretrofit.model.BaseViewHolder;
import com.mi.hz.hzretrofit.model.Bean;
import com.mi.hz.hzretrofit.pullrefresh.ILayoutManager;
import com.mi.hz.hzretrofit.pullrefresh.PullRefreshLayout;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.bumptech.glide.Glide;
import com.mi.hz.hzretrofit.LoggingInterceptor;

/**
 * Created by mi on 17-4-8.
 */

public class ActivitySecond extends BaseListActivity<Bean> {
    private int random;
    private int page = 1;
    private static final String TAG = "ActivitySecond";
    private String mType;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//    }


    @Override
    protected void setUpTitle(int titleResId) {
        titleResId = R.string.action_bar_get_normal_title;
        super.setUpTitle(titleResId);
    }

    @Override
    protected void setUpData() {
        Intent in =getIntent();
        mType=in.getStringExtra("type");
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sample_list_item, parent, false);
        return new SampleViewHolder(view);
    }


    @Override
    protected ILayoutManager getLayoutManager() {
//        random = new Random().nextInt(3);
//        switch (random) {
//            case 0:
//                return new MyLinearLayoutManager(getApplicationContext());
//            case 1:
//                return new MyGridLayoutManager(getApplicationContext(), 3);
//            case 2:
//                return new MyStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        }
        return super.getLayoutManager();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        if (random == 0) {
            return super.getItemDecoration();
        } else {
            return null;
        }
    }


    @Override
    public void onRefresh(final int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRefreshLayout.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        useCall(action);
    }

    private void useCall(final int action){
//        OkHttpClient okHttpClient = new OkHttpClient();
        File cacheFile = new File(this.getCacheDir(), "data/data/hzcahe／hz.xml");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
//        okHttpClient.setCache(cache);
//        okHttpClient.cache();
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new LoggingInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("hz--","设置缓存--this.getCacheDir()="+this.getCacheDir());

        ApiService apiService = retrofit.create(ApiService.class);

        Call<BaseModel<ArrayList<Bean>>> call = apiService.listBeanNormal(mType,20, page++);
        Log.d("hz--",TAG+",page="+page+",mType="+mType);

        call.enqueue(new Callback<BaseModel<ArrayList<Bean>>>() {
                         @Override
                         public void onResponse(Call<BaseModel<ArrayList<Bean>>> call, Response<BaseModel<ArrayList<Bean>>> response) {
                             if (action == PullRefreshLayout.ACTION_PULL_TO_REFRESH) {
                                 Log.d("hz--", TAG + ",加载--刷新--mDataList.clear()");
                                 mDataList.clear();
                             }

                             if (response.body().results == null || response.body().results.size() == 0) {
                                 recycler.enableLoadMore(false);
                             } else {
                                 Log.d("hz--", TAG + ",加载更多-response.body().results=" + response.body().results.toString());
                                 recycler.enableLoadMore(true);
                                 mDataList.addAll(response.body().results);
                                 adapter.notifyDataSetChanged();
                             }

                             recycler.onRefreshCompleted();
                         }

                         @Override
                         public void onFailure(Call<BaseModel<ArrayList<Bean>>> call, Throwable t) {
                             recycler.onRefreshCompleted();
                         }
                     }
        );

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public class SampleViewHolder extends BaseViewHolder {

        ImageView mSampleListItemImg;
        TextView mSampleListItemWho;
        TextView mSampleListItemDes;

        public SampleViewHolder(View itemView) {
            super(itemView);
            mSampleListItemImg = (ImageView) itemView.findViewById(R.id.item_img);
            mSampleListItemWho = (TextView) itemView.findViewById(R.id.item_who);
            mSampleListItemDes = (TextView) itemView.findViewById(R.id.item_des);

        }

        @Override
        public void onBindViewHolder(int position) {
            mSampleListItemWho.setText(mDataList.get(position).who);
            mSampleListItemDes.setText(mDataList.get(position).desc);
            String url = mDataList.get(position).url;
            if(url != null && url.endsWith(".jpg")){
                mSampleListItemImg.setVisibility(View.VISIBLE);
                Glide.with(mSampleListItemImg.getContext())
                        .load(mDataList.get(position).url)
                        .centerCrop()
                        .placeholder(R.color.app_primary_color)
                        .crossFade()
                        .into(mSampleListItemImg);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            Uri uri = Uri.parse(mDataList.get(position).url);
            Log.d("hz--", TAG + ",mDataList.get(position).url=" + mDataList.get(position).url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }

    }
}
