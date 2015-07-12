package com.ouchadam.loldr.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.Executor;
import com.ouchadam.loldr.UserTokenProvider;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.db.SuperRepo;

import java.util.List;

import rx.Subscriber;

public class PostActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".POST";
    private static final String EXTA_POST_ID = "postId";
    private static final String EXTRA_SUBREDDIT = "subreddit";

    private final Executor executor;

    private Presenter<CommentProvider.CommentSource> presenter;

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
        this.presenter = Presenter.onCreate(this, new CommentProvider(), null);

        String subreddit = getIntent().getStringExtra(EXTRA_SUBREDDIT);
        String postId = getIntent().getStringExtra(EXTA_POST_ID);

        executor.execute(SuperRepo.newInstance(UserTokenProvider.newInstance(this)).comments(subreddit, postId), presentResult());
    }

    private Subscriber<Data.Comments> presentResult() {
        return new Subscriber<Data.Comments>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "Something went wrong", e);
            }

            @Override
            public void onNext(Data.Comments comments) {
                List<Data.Comment> dataPosts = comments.getComments();
                presenter.present(new CommentProvider.CommentSource(dataPosts));
            }
        };
    }

}
