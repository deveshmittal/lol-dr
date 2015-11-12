package com.ouchadam.loldr.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.Executor;
import com.ouchadam.loldr.LogSubscriber;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.UserTokenProvider;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.feed.FeedActivity;

import rx.Subscriber;

public class PostActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".POST";
    private static final String EXTA_POST_ID = "postId";
    private static final String EXTRA_SUBREDDIT = "subreddit";

    private final Executor executor;

    private Presenter presenter;
    private String subreddit;

    public static Intent create(String subreddit, String postId) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_SUBREDDIT, subreddit);
        intent.putExtra(EXTA_POST_ID, postId);
        return intent;
    }

    public PostActivity() {
        this.executor = new Executor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subreddit = getIntent().getStringExtra(EXTRA_SUBREDDIT);
        String postId = getIntent().getStringExtra(EXTA_POST_ID);

        Repository repository = Repository.newInstance(UserTokenProvider.newInstance(this));

        this.presenter = Presenter.onCreate(this, new PostDetailsProvider(), new CommentProvider(), new Presenter.Listener() {
            @Override
            public void onPostClicked(Ui.PostDetails post) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(post.getPostSummary().getExternalLink()));
                startActivity(intent);
            }

            @Override
            public void onCommentClicked(Ui.Comment comment) {
            }

            @Override
            public void onTitleClicked() {
                startActivity(FeedActivity.create(subreddit));
            }
        });

        presenter.setTitle(subreddit);

        executor.execute(repository.postDetails(subreddit, postId), presentPostDetails());
    }

    private Subscriber<Data.PostDetails> presentPostDetails() {
        return new LogSubscriber<Data.PostDetails>() {
            @Override
            public void onNext(Data.PostDetails post) {
                PostSummarySimpleDateFormatter dateFormatter = PostSummarySimpleDateFormatter.newInstance(getResources());
                presenter.presentPostDetails(new PostDetailsProvider.PostDetailsSource(dateFormatter, post.getPost()));
                presenter.presentComments(new CommentProvider.CommentSource(post.getComments().getComments()));
            }
        };
    }

}
