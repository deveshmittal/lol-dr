package com.ouchadam.loldr.feed;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    public void bind(final Ui.PostSummary postSummary, boolean seen) {
        rootView.setAlpha(seen ? .5f : 1f);
        setClickListener(postSummary);
        setThumbnailClickListener(postSummary);
        setTitle(postSummary.getTitle());
        setAuthAndSubreddit(postSummary);
        setScoreAndCommentCount(postSummary);
        setThumbnail(postSummary.getImageUrl());
    }

    private void setScoreAndCommentCount(Ui.PostSummary postSummary) {
        scoreAndCommentCount.setText(postSummary.getScore() + " points" + " | " + postSummary.getCommentCount() + " comments | " + postSummary.getTime());
    }

    private void setAuthAndSubreddit(Ui.PostSummary postSummary) {
        authorAndSubreddit.setText(createSpan(postSummary));
    }

    private Spannable createSpan(Ui.PostSummary postSummary) {
        String source = postSummary.getAuthor() + " in " + postSummary.getSubreddit();
        SpannableStringBuilder spannableStringBuilder = SpannableStringBuilder.valueOf(source);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), source.indexOf(postSummary.getSubreddit()), source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(rootView.getResources().getColor(android.R.color.black)), source.indexOf("in"), source.indexOf("in") + "in".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    private void setThumbnailClickListener(final Ui.PostSummary postSummary) {
        thumbnailView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postClickListener.onClickLinkFrom(postSummary);
                    }
                }
        );
    }

    private void setThumbnail(final String imageUrl) {
        if ("self".equals(imageUrl)) {
            thumbnailView.setVisibility(View.GONE);
            return;
        }

        if (imageUrl == null || imageUrl.isEmpty() || "default".equals(imageUrl)) {
            thumbnailView.setImageResource(R.drawable.ic_link);
        } else {
            Glide.with(thumbnailView.getContext()).load(imageUrl).into(thumbnailView);
        }
        thumbnailView.setVisibility(View.VISIBLE);
    }

    private void setClickListener(final Ui.PostSummary postSummary) {
        rootView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postClickListener.onClick(postSummary);
                    }
                }
        );
    }

    private void setTitle(String title) {
        titleView.setText(title);
        rootView.setContentDescription(title);
    }

}
