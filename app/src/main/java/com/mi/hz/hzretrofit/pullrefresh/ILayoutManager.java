package com.mi.hz.hzretrofit.pullrefresh;

import android.support.v7.widget.RecyclerView;

import com.mi.hz.hzretrofit.model.BaseListAdapter;

/**
 * Created by mi on 17-4-8.
 */

public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
