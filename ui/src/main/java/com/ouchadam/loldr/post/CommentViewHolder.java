package com.ouchadam.loldr.post;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.BindableViewHolder;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class CommentViewHolder extends BindableViewHolder<Ui.Comment> {

    public static final int POSITION_KEY = R.id.tag_feed_position;

    private static final int DEPTH_COLOURS[] = {
            R.color.depth_one,
            R.color.depth_two,
            R.color.depth_three,
            R.color.depth_four
    };

    private final View rootView;
    private final TextView bodyView;
    private final TextView authorView;
    private final View indentationView;
    private final View content;
    private Presenter.Listener postClickListener;

    static CommentViewHolder inflateComment(ViewGroup parent, LayoutInflater layoutInflater, Presenter.Listener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        TextView authorView = (TextView) view.findViewById(R.id.post_comment_author);
        View indentationView = view.findViewById(R.id.comment_indentation);
        View content = view.findViewById(R.id.comment_content);

        return new CommentViewHolder(view, bodyView, authorView, indentationView, content, postClickListener);
    }

    static CommentViewHolder inflateMore(ViewGroup parent, LayoutInflater layoutInflater, Presenter.Listener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_more_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        View indentationView = view.findViewById(R.id.comment_indentation);
        View content = view.findViewById(R.id.comment_content);

        return new CommentViewHolder(view, bodyView, null, indentationView, content, postClickListener);
    }

    private CommentViewHolder(View itemView, TextView bodyView, TextView authorView, View indentationView, View content,
                              Presenter.Listener postClickListener) {
        super(itemView);
        this.rootView = itemView;
        this.bodyView = bodyView;
        this.authorView = authorView;
        this.indentationView = indentationView;
        this.content = content;
        this.postClickListener = postClickListener;
    }

    @Override
    public void bind(final Ui.Comment comment, int position) {
        setPosition(position);
        setDepth(comment.getDepth());

        if (!comment.isMore()) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickListener.onCommentClicked(comment);
                }
            });
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
            content.setPadding(depthOffset, content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom());

            indentationView.setBackgroundColor(getColourForDepth(depth));

        } else {
            indentationView.setVisibility(View.GONE);
            content.setPadding(0, content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom());
        }
    }

    private int getColourForDepth(int depth) {
        Resources resources = rootView.getResources();

        int normalisedDepth = (depth % DEPTH_COLOURS.length);

        int colour = DEPTH_COLOURS[normalisedDepth];
        return resources.getColor(colour);
    }

}
