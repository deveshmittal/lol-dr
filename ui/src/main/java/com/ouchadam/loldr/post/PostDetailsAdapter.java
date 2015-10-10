package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.post.Presenter.Listener;

class PostDetailsAdapter extends RecyclerView.Adapter {

    private static final int DETAILS_OFFSET = 1;

    private final LayoutInflater layoutInflater;
    private final Listener listener;

    private final SourceProvider<Ui.Comment, DataSource<Ui.Comment>> commentSource;
    private final SourceProvider<Ui.PostDetails, DataSource<Ui.PostDetails>> postSource;

    PostDetailsAdapter(SourceProvider<Ui.Comment, DataSource<Ui.Comment>> commentSource,
                       SourceProvider<Ui.PostDetails, DataSource<Ui.PostDetails>> postSource,
                       LayoutInflater layoutInflater, Listener listener) {
        this.commentSource = commentSource;
        this.postSource = postSource;
        this.layoutInflater = layoutInflater;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CommentViewHolder.VIEW_TYPE_COMMENT:
                return CommentViewHolder.inflateComment(viewGroup, layoutInflater, getPostClickListener());

            case CommentViewHolder.VIEW_TYPE_MORE:
                return CommentViewHolder.inflateMore(viewGroup, layoutInflater, getPostClickListener());

            case DetailsViewHolder.VIEW_TYPE_DETAILS:
                return DetailsViewHolder.inflate(viewGroup, layoutInflater, getPostClickListener());
            default:
                throw new RuntimeException("dev error");
        }
    }

    private View.OnClickListener getPostClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            DetailsViewHolder detailsViewHolder = (DetailsViewHolder) viewHolder;
            Ui.PostDetails postDetails = postSource.get(position);
            detailsViewHolder.bind(postDetails);
        } else {
            int truePosition = getTruePosition(position);
            Ui.Comment comment = commentSource.get(truePosition);
            CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;
            commentViewHolder.bind(comment, truePosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DetailsViewHolder.VIEW_TYPE_DETAILS;
        } else {
            return commentSource.get(getTruePosition(position)).isMore() ? CommentViewHolder.VIEW_TYPE_MORE : CommentViewHolder.VIEW_TYPE_COMMENT;
        }
    }

    private int getTruePosition(int position) {
        return position - DETAILS_OFFSET;
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
