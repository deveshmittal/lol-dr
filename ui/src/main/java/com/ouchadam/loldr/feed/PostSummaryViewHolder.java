package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class PostSummaryViewHolder extends RecyclerView.ViewHolder {

    private final View rootView;
    private final TextView titleView;
    private final TextView authorAndSubreddit;
    private final TextView scoreAndCommentCount;
    private final ImageView thumbnailView;
    private final Presenter.Listener postClickListener;

    static PostSummaryViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, Presenter.Listener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_feed_post_summary, parent, false);

        TextView titleView = (TextView) view.findViewById(R.id.feed_post_summary_text_title);
        TextView authorAndSubreddit = (TextView) view.findViewById(R.id.feed_post_summary_text_author_and_subreddit);
        TextView scoreAndCommentCount = (TextView) view.findViewById(R.id.feed_post_summary_text_score_and_comment_count);
        ImageView thumbnailView = (ImageView) view.findViewById(R.id.feed_post_summary_image_thumbnail);

        return new PostSummaryViewHolder(view, titleView, authorAndSubreddit, scoreAndCommentCount, thumbnailView, postClickListener);
    }

    private PostSummaryViewHolder(View itemView, TextView titleView, TextView authorAndSubreddit,
                                  TextView scoreAndCommentCount, ImageView thumbnailView, Presenter.Listener postClickListener) {
        super(itemView);
        this.rootView = itemView;
        this.titleView = titleView;
        this.authorAndSubreddit = authorAndSubreddit;
        this.scoreAndCommentCount = scoreAndCommentCount;
        this.thumbnailView = thumbnailView;
        this.postClickListener = postClickListener;
    }

    public void bind(final Ui.PostSummary postSummary) {
        setClickListener(postSummary);
        setThumbnailClickListener(postSummary);
        setTitle(postSummary.getTitle());
        authorAndSubreddit.setText(postSummary.getAuthor() + " in " + postSummary.getSubreddit());
        scoreAndCommentCount.setText(postSummary.getScore() + " points" + " | " + postSummary.getCommentCount() + " comments | " + postSummary.getTime());
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
    }

    private void setTitle(String title) {
        titleView.setText(title);
        rootView.setContentDescription(title);
    }

}
