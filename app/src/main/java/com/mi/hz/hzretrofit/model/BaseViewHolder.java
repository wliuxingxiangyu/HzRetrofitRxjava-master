package com.mi.hz.hzretrofit.model;

import android.view.View;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mi on 17-4-8.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, getAdapterPosition());
            }
        });
    }

    public abstract void onBindViewHolder(int position);
    public abstract void onItemClick(View view, int position);
}
