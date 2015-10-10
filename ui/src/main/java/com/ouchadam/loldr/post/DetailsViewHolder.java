package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class DetailsViewHolder extends RecyclerView.ViewHolder {

    static final int VIEW_TYPE_DETAILS = 110;

    private final View rootView;
    private final TextView bodyView;

    static DetailsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_details, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_details_body);

        view.setOnClickListener(postClickListener);

        return new DetailsViewHolder(view, bodyView);
    }

    private DetailsViewHolder(View itemView, TextView bodyView) {
        super(itemView);
        this.rootView = itemView;
        this.bodyView = bodyView;
    }

    public void bind(Ui.PostDetails postDetails) {
        bodyView.setText(postDetails.getBody());
    }

}
