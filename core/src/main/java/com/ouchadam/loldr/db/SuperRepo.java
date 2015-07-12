package com.ouchadam.loldr.db;

import android.content.Context;

import com.ouchadam.loldr.DataSource;
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

    private final Repository api;
    private final CacheRepository cache;

    private final MarshallerFactory marshallerFactory;

    public static SuperRepo newInstance(TokenProvider tokenProvider, Context context) {
        MarshallerFactory marshallerFactory = MarshallerFactory.newInstance(context.getResources());
        return new SuperRepo(Repository.newInstance(tokenProvider), new CacheRepository(context.getContentResolver()), marshallerFactory);
    }

    public SuperRepo(Repository api, CacheRepository cache, MarshallerFactory marshallerFactory) {
        this.api = api;
        this.cache = cache;
        this.marshallerFactory = marshallerFactory;
    }

    public Observable<Data.Comments> comments(String subreddit, String postId) {
        return api.comments(subreddit, postId);
    }

    public Observable<DataSource<Ui.PostSummary>> subreddit(String subreddit) {
        return Observable.concat(
                cache.subreddit(subreddit),
                api.subreddit(subreddit).map(toPostSource())
        ).first();
    }

    public Observable<DataSource<Ui.PostSummary>> subreddit(String subreddit, String afterId) {
        return api.subreddit(subreddit, afterId).map(toPostSource());
    }

    private Func1<Data.Feed, DataSource<Ui.PostSummary>> toPostSource() {
        return new Func1<Data.Feed, DataSource<Ui.PostSummary>>() {
            @Override
            public PostProvider.PostSummarySource call(Data.Feed feed) {
                List<Ui.PostSummary> summaries = marshallerFactory.posts().marshall(feed.getPosts());
                return new PostProvider.PostSummarySource(summaries);
            }
        };
    }

    public Observable<Data.Subscriptions> defaultSubscriptions() {
        return api.defaultSubscriptions();
    }

    public Observable<Data.Subscriptions> userSubscriptions() {
        return api.userSubscriptions();
    }
}
