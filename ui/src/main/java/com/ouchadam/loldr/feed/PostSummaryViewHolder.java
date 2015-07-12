package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class PostSummaryViewHolder extends RecyclerView.ViewHolder {

    private final View rootView;
    private final TextView titleView;
    private final TextView timeView;
    private final TextView commentsView;
    private final TextView subredditView;
    private final TextView authorView;
    private final TextView scoreView;
    private final ImageView thumbnailView;
    private final View saveView;
    private final Presenter.Listener postClickListener;

    static PostSummaryViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, Presenter.Listener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_feed_post_summary, parent, false);

        TextView titleView = (TextView) view.findViewById(R.id.feed_post_summary_text_title);
        TextView timeView = (TextView) view.findViewById(R.id.feed_post_summary_text_time);
        TextView commentsView = (TextView) view.findViewById(R.id.feed_post_summary_text_comments);
        TextView subredditView = (TextView) view.findViewById(R.id.feed_post_summary_text_subreddit);
        TextView authorView = (TextView) view.findViewById(R.id.feed_post_summary_text_author);
        TextView scoreView = (TextView) view.findViewById(R.id.feed_post_summary_text_score);
        ImageView thumbnailView = (ImageView) view.findViewById(R.id.feed_post_summary_image_thumbnail);
        View saveView = view.findViewById(R.id.feed_post_summary_image_save);

        return new PostSummaryViewHolder(view, titleView, timeView, commentsView, subredditView,
                authorView, scoreView, thumbnailView, saveView, postClickListener);
    }

    private PostSummaryViewHolder(View itemView, TextView titleView, TextView timeView, TextView commentsView,
                                  TextView subredditView, TextView authorView, TextView scoreView, ImageView thumbnailView, View saveView, Presenter.Listener postClickListener) {
        super(itemView);
        this.rootView = itemView;
        this.titleView = titleView;
        this.timeView = timeView;
        this.commentsView = commentsView;
        this.subredditView = subredditView;
        this.authorView = authorView;
        this.scoreView = scoreView;
        this.thumbnailView = thumbnailView;
        this.saveView = saveView;
        this.postClickListener = postClickListener;
    }

    public void bind(final Ui.PostSummary postSummary) {
        setClickListener(postSummary);
        setThumbnailClickListener(postSummary);
        setTitle(postSummary.getTitle());
        setTime(postSummary.getTime());
        setCommentsCount(postSummary.getCommentCount());
        setSubreddit(postSummary.getSubreddit());
        authorView.setText(postSummary.getAuthor());
        scoreView.setText(postSummary.getScore());
        setThumbnail(postSummary.getImageUrl());
    }

    private void setThumbnailClickListener(final Ui.PostSummary postSummary) {
        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListener.onClickLinkFrom(postSummary);
            }
        });
    }

    private void setThumbnail(final String imageUrl) {
        if ("self".equals(imageUrl)) {
            thumbnailView.setVisibility(View.GONE);
            return;
        }

        if (imageUrl == null || imageUrl.isEmpty() ||  "default".equals(imageUrl)) {
            thumbnailView.setImageResource(R.drawable.ic_link);
        } else {
            thumbnailView.setImageResource(R.drawable.ic_image_placeholder);
        }
        thumbnailView.setVisibility(View.VISIBLE);
    }

    private void setClickListener(final Ui.PostSummary postSummary) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListener.onClick(postSummary);
            }
        });
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListener.onClickSave(postSummary);
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
