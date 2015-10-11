package com.ouchadam.loldr.post;

import android.graphics.Typeface;
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
import com.ouchadam.loldr.BindableViewHolder;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class LinkDetailsViewHolder extends BindableViewHolder<Ui.PostDetails> {

    private final View root;
    private final TextView titleView;
    private final TextView authorSubreddit;
    private final TextView scoreCommentCount;
    private final ImageView thumbnailView;

    static LinkDetailsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View root = layoutInflater.inflate(R.layout.view_post_link_details, parent, false);

        TextView titleView = (TextView) root.findViewById(R.id.post_details_link_title);
        TextView authorSubreddit = (TextView) root.findViewById(R.id.post_details_link_author_subreddit);
        TextView scoreCommentCount = (TextView) root.findViewById(R.id.post_details_link_score_comment_count);
        ImageView thumbnailView = (ImageView) root.findViewById(R.id.post_details_link_thumbnail);

        root.setOnClickListener(postClickListener);

        return new LinkDetailsViewHolder(root, titleView, authorSubreddit, scoreCommentCount, thumbnailView);
    }

    private LinkDetailsViewHolder(View root, TextView titleView, TextView authorSubreddit, TextView scoreCommentCount, ImageView thumbnailView) {
        super(root);
        this.root = root;
        this.titleView = titleView;
        this.authorSubreddit = authorSubreddit;
        this.scoreCommentCount = scoreCommentCount;
        this.thumbnailView = thumbnailView;
    }

    @Override
    public void bind(Ui.PostDetails postDetails, int position) {
        Ui.PostSummary postSummary = postDetails.getPostSummary();

        titleView.setText(postSummary.getTitle());
        setScoreAndCommentCount(postSummary);
        setAuthAndSubreddit(postSummary);
        Glide.with(thumbnailView.getContext()).load(postSummary.getImageUrl()).into(thumbnailView);
    }

    private void setScoreAndCommentCount(Ui.PostSummary postSummary) {
        scoreCommentCount.setText(postSummary.getScore() + " points" + " | " + postSummary.getCommentCount() + " comments | " + postSummary.getTime());
    }

    private void setAuthAndSubreddit(Ui.PostSummary postSummary) {
        authorSubreddit.setText(createSpan(postSummary));
    }

    private Spannable createSpan(Ui.PostSummary postSummary) {
        String source = postSummary.getAuthor() + " in " + postSummary.getSubreddit();
        SpannableStringBuilder spannableStringBuilder = SpannableStringBuilder.valueOf(source);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), source.indexOf(postSummary.getSubreddit()), source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(root.getResources().getColor(android.R.color.black)), source.indexOf("in"), source.indexOf("in") + "in".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

}
