package com.ouchadam.loldr;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BindableViewHolder<T>  extends RecyclerView.ViewHolder {

    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T data, int position);

}
