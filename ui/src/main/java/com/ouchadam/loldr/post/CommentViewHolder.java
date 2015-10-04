package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class CommentViewHolder extends RecyclerView.ViewHolder {

    public static final int POSITION_KEY = R.id.tag_feed_position;

    static final int VIEW_TYPE_MORE = 100;
    static final int VIEW_TYPE_COMMENT = 50;

    private final View rootView;
    private final TextView bodyView;
    private final TextView authorView;

    static CommentViewHolder inflateComment(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        TextView authorView = (TextView) view.findViewById(R.id.post_comment_author);

        view.setOnClickListener(postClickListener);

        return new CommentViewHolder(view, bodyView, authorView);
    }

    static CommentViewHolder inflateMore(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_more_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        view.setOnClickListener(postClickListener);

        return new CommentViewHolder(view, bodyView, null);
    }

    private CommentViewHolder(View itemView, TextView bodyView, TextView authorView) {
        super(itemView);
        this.rootView = itemView;
        this.bodyView = bodyView;
        this.authorView = authorView;
    }

    public void bind(Ui.Comment comment, int position) {
        setPosition(position);
        setDepth(comment.getDepth());

        if (!comment.isMore()) {
            setBody(comment.getBody());
            setAuthor(comment.getAuthor());
        }
    }

    private void setBody(String title) {
        bodyView.setText(title);
        rootView.setContentDescription(title);
    }

    private void setAuthor(String time) {
        authorView.setText(time);
    }

    private void setPosition(int position) {
        rootView.setTag(POSITION_KEY, position);
    }

    private void setDepth(int depth) {
        rootView.setPadding(depth * 40, rootView.getPaddingTop(), rootView.getPaddingRight(), rootView.getPaddingBottom());
    }

}
