package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.ui.R;

final class DetailsViewHolder extends RecyclerView.ViewHolder {

    static final int VIEW_TYPE_DETAILS = 110;

    private final View rootView;
    private final TextView bodyView;
    private final TextView authorView;

    static DetailsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        TextView authorView = (TextView) view.findViewById(R.id.post_comment_author);

        view.setOnClickListener(postClickListener);

        return new DetailsViewHolder(view, bodyView, authorView);
    }

    private DetailsViewHolder(View itemView, TextView bodyView, TextView authorView) {
        super(itemView);
        this.rootView = itemView;
        this.bodyView = bodyView;
        this.authorView = authorView;
    }

    public void bind() {

    }
}
