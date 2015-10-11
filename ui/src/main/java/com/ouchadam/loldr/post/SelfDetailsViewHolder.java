package com.ouchadam.loldr.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.BindableViewHolder;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class SelfDetailsViewHolder extends BindableViewHolder<Ui.PostDetails> {

    private final View rootView;
    private final TextView bodyView;

    static SelfDetailsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_self_details, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_details_body);

        view.setOnClickListener(postClickListener);

        return new SelfDetailsViewHolder(view, bodyView);
    }

    private SelfDetailsViewHolder(View itemView, TextView bodyView) {
        super(itemView);
        this.rootView = itemView;
        this.bodyView = bodyView;
    }

    @Override
    public void bind(Ui.PostDetails postDetails, int position) {
        bodyView.setText(postDetails.getBody());
    }

}
