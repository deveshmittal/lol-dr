package com.ouchadam.loldr.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouchadam.loldr.BindableViewHolder;
import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;

public class ViewHolderFactory {

    private static final int VIEW_TYPE_SELF_DETAILS = 1;
    private static final int VIEW_TYPE_LINK_DETAILS = 2;
    private static final int VIEW_TYPE_MORE = 20;
    private static final int VIEW_TYPE_COMMENT = 21;
    private static final int DETAILS_OFFSET = 1;
    private static final int DETAILS_POSITION = 0;

    private final LayoutInflater layoutInflater;
    private final Presenter.Listener listener;

    public ViewHolderFactory(LayoutInflater layoutInflater, Presenter.Listener listener) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
    }

    public BindableViewHolder create(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_COMMENT:
                return CommentViewHolder.inflateComment(viewGroup, layoutInflater, getPostClickListener());

            case VIEW_TYPE_MORE:
                return CommentViewHolder.inflateMore(viewGroup, layoutInflater, getPostClickListener());

            case VIEW_TYPE_SELF_DETAILS:
                return SelfDetailsViewHolder.inflate(viewGroup, layoutInflater, getPostClickListener());

            case VIEW_TYPE_LINK_DETAILS:
                return LinkDetailsViewHolder.inflate(viewGroup, layoutInflater, getPostClickListener());

            default:
                throw new RuntimeException("dev error");
        }
    }

    private View.OnClickListener getPostClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                listener.onPostClicked();
                // TODO
            }
        };
    }

    public int getItemViewType(int position, DataSource<Ui.PostDetails> postSource, DataSource<Ui.Comment> commentSource) {
        if (position == DETAILS_POSITION) {
            return postSource.get(0).isSelfPost() ? VIEW_TYPE_SELF_DETAILS : VIEW_TYPE_LINK_DETAILS;
        } else {
            return commentSource.get(getTruePosition(position)).isMore() ? VIEW_TYPE_MORE : VIEW_TYPE_COMMENT;
        }
    }

    private int getTruePosition(int position) {
        return position - DETAILS_OFFSET;
    }

    public void bindViewHolder(BindableViewHolder viewHolder, int position,
                               SourceProvider<Ui.PostDetails, DataSource<Ui.PostDetails>> postSource,
                               SourceProvider<Ui.Comment, DataSource<Ui.Comment>> commentSource) {
        if (position == DETAILS_POSITION) {
            Ui.PostDetails postDetails = postSource.get(position);
            viewHolder.bind(postDetails, position);
        } else {
            int truePosition = getTruePosition(position);
            Ui.Comment comment = commentSource.get(truePosition);
            viewHolder.bind(comment, truePosition);
        }
    }

}
