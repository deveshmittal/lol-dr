package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class PostSummaryViewHolder extends RecyclerView.ViewHolder {

    private final View rootView;
    private final TextView titleView;
    private final TextView timeView;
    private final TextView commentsView;
    private final TextView subredditView;
    private final Presenter.Listener postClickListener;

    static PostSummaryViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, Presenter.Listener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_feed_post_summary, parent, false);

        TextView titleView = (TextView) view.findViewById(R.id.feed_post_summary_text_title);
        TextView timeView = (TextView) view.findViewById(R.id.feed_post_summary_timestamp);
        TextView commentsView = (TextView) view.findViewById(R.id.feed_post_summary_text_comments);
        TextView subredditView = (TextView) view.findViewById(R.id.feed_post_summary_text_subreddit);

        return new PostSummaryViewHolder(view, titleView, timeView, commentsView, subredditView, postClickListener);
    }

    private PostSummaryViewHolder(View itemView, TextView titleView, TextView timeView, TextView commentsView,
                                  TextView subredditView, Presenter.Listener postClickListener) {
        super(itemView);
        this.rootView = itemView;
        this.titleView = titleView;
        this.timeView = timeView;
        this.commentsView = commentsView;
        this.subredditView = subredditView;
        this.postClickListener = postClickListener;
    }

    public void bind(final Ui.PostSummary postSummary) {
        setClickListener(postSummary);
        setTitle(postSummary.getTitle());
        setTime(postSummary.getTime());
        setCommentsCount(postSummary.getCommentCount());
        setSubreddit(postSummary.getSubreddit());
    }

    private void setClickListener(final Ui.PostSummary postSummary) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListener.onPostClicked(postSummary);
            }
        });
    }

    private void setTitle(String title) {
        titleView.setText(title);
        rootView.setContentDescription(title);
    }

    private void setTime(String time) {
        timeView.setText(time);
    }

    private void setCommentsCount(String count) {
        commentsView.setText(count);
    }

    private void setSubreddit(String subredditName) {
        subredditView.setText(subredditName);
    }

}
