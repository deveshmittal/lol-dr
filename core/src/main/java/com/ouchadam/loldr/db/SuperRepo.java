package com.ouchadam.loldr.db;

import android.content.ContentResolver;
import android.content.Context;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;
import com.ouchadam.loldr.feed.MarshallerFactory;
import com.ouchadam.loldr.feed.PostProvider;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class SuperRepo {

    private final Repository repository;

    private final ContentResolver contentResolver;
    private final MarshallerFactory marshallerFactory;

    public static SuperRepo newInstance(TokenProvider tokenProvider, Context context) {
        return new SuperRepo(Repository.newInstance(tokenProvider), context.getContentResolver(), MarshallerFactory.newInstance(context.getResources()));
    }

    public SuperRepo(Repository repository, ContentResolver contentResolver, MarshallerFactory marshallerFactory) {
        this.repository = repository;
        this.contentResolver = contentResolver;
        this.marshallerFactory = marshallerFactory;
    }

    public Observable<Data.Comments> comments(String subreddit, String postId) {
        return repository.comments(subreddit, postId);
    }

    public Observable<PostProvider.PostSummarySource> subreddit(String subreddit) {
        return repository.subreddit(subreddit).map(toPostSource());
    }

    public Observable<PostProvider.PostSummarySource> subreddit(String subreddit, String afterId) {
        return repository.subreddit(subreddit, afterId).map(toPostSource());
    }

    private Func1<Data.Feed, PostProvider.PostSummarySource> toPostSource() {
        return new Func1<Data.Feed, PostProvider.PostSummarySource>() {
            @Override
            public PostProvider.PostSummarySource call(Data.Feed feed) {
                List<Ui.PostSummary> summaries = marshallerFactory.posts().marshall(feed.getPosts());
                return new PostProvider.PostSummarySource(summaries);
            }
        };
    }

    public Observable<Data.Subscriptions> defaultSubscriptions() {
        return repository.defaultSubscriptions();
    }

    public Observable<Data.Subscriptions> userSubscriptions() {
        return repository.userSubscriptions();
    }
}
