package com.ouchadam.loldr.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.BindableViewHolder;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class CommentViewHolder extends BindableViewHolder<Ui.Comment> {

    public static final int POSITION_KEY = R.id.tag_feed_position;

    private final View rootView;
    private final TextView bodyView;
    private final TextView authorView;
    private final View indentationView;

    static CommentViewHolder inflateComment(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        TextView authorView = (TextView) view.findViewById(R.id.post_comment_author);
        View indentationView = view.findViewById(R.id.comment_indentation);

        view.setOnClickListener(postClickListener);

        return new CommentViewHolder(view, bodyView, authorView, indentationView);
    }

    static CommentViewHolder inflateMore(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_more_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        View indentationView = view.findViewById(R.id.comment_indentation);

        view.setOnClickListener(postClickListener);

        return new CommentViewHolder(view, bodyView, null, indentationView);
    }

    private CommentViewHolder(View itemView, TextView bodyView, TextView authorView, View indentationView) {
        super(itemView);
        this.rootView = itemView;
        this.bodyView = bodyView;
        this.authorView = authorView;
        this.indentationView = indentationView;
    }

    @Override
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
        if (depth > 0) {
            indentationView.setVisibility(View.VISIBLE);
            int depthOffset = rootView.getResources().getDimensionPixelSize(R.dimen.comment_indent) * depth;
            rootView.setPadding(depthOffset, rootView.getPaddingTop(), rootView.getPaddingRight(), rootView.getPaddingBottom());
        } else {
            indentationView.setVisibility(View.GONE);
            rootView.setPadding(0, rootView.getPaddingTop(), rootView.getPaddingRight(), rootView.getPaddingBottom());
        }
    }

}
