package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ouchadam.loldr.BindableViewHolder;
import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;

class PostDetailsAdapter extends RecyclerView.Adapter<BindableViewHolder<?>> {

    private final ViewHolderFactory viewHolderFactory;
    private final SourceProvider<Ui.Comment, DataSource<Ui.Comment>> commentSource;
    private final SourceProvider<Ui.PostDetails, DataSource<Ui.PostDetails>> postSource;

    PostDetailsAdapter(ViewHolderFactory viewHolderFactory, SourceProvider<Ui.Comment, DataSource<Ui.Comment>> commentSource,
                       SourceProvider<Ui.PostDetails, DataSource<Ui.PostDetails>> postSource) {
        this.viewHolderFactory = viewHolderFactory;
        this.commentSource = commentSource;
        this.postSource = postSource;
    }

    @Override
    public BindableViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return viewHolderFactory.create(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(BindableViewHolder viewHolder, int position) {
        viewHolderFactory.bindViewHolder(viewHolder, position, postSource, commentSource);
    }

    @Override
    public int getItemViewType(int position) {
        return viewHolderFactory.getItemViewType(position, postSource, commentSource);
    }

    @Override
    public int getItemCount() {
        return commentSource.size() + postSource.size();
    }

    public void notifyCommentSourceChanged(DataSource<Ui.Comment> dataSource) {
        this.commentSource.swap(dataSource);
        notifyDataSetChanged();
    }

    public void notifyPostSourceChanged(DataSource<Ui.PostDetails> dataSource) {
        this.postSource.swap(dataSource);
        notifyDataSetChanged();
    }
}
