package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.post.Presenter.Listener;

class PostDetailsAdapter<T extends DataSource<Ui.Comment>> extends RecyclerView.Adapter {

    public static final int DETAILS_OFFSET = 1;
    private final LayoutInflater layoutInflater;
    private final Listener listener;

    private SourceProvider<Ui.Comment, T> dataSource;

    PostDetailsAdapter(SourceProvider<Ui.Comment, T> dataSource, LayoutInflater layoutInflater, Listener listener) {
        this.dataSource = dataSource;
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
                Ui.Comment comment = dataSource.get((Integer) view.getTag(CommentViewHolder.POSITION_KEY));
                listener.onCommentClicked(comment);
            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            DetailsViewHolder detailsViewHolder = (DetailsViewHolder) viewHolder;
            detailsViewHolder.bind();
        } else {
            int truePosition = getTruePosition(position);
            Ui.Comment comment = dataSource.get(truePosition);
            CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;
            commentViewHolder.bind(comment, truePosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DetailsViewHolder.VIEW_TYPE_DETAILS;
        } else {
            return dataSource.get(getTruePosition(position)).isMore() ? CommentViewHolder.VIEW_TYPE_MORE : CommentViewHolder.VIEW_TYPE_COMMENT;
        }
    }

    private int getTruePosition(int position) {
        return position - DETAILS_OFFSET;
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void notifyDataSourceChanged(T dataSource) {
        this.dataSource.swap(dataSource);
        notifyDataSetChanged();
    }
}
