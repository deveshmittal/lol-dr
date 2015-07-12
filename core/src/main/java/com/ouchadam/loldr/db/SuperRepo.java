package com.ouchadam.loldr.db;

import android.content.Context;

import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;
import com.ouchadam.loldr.post.PostSummarySimpleDateFormatter;

import rx.Observable;

public class SuperRepo {

    private final Repository api;
    private final CacheRepository cache;

    public static SuperRepo newInstance(TokenProvider tokenProvider, Context context) {
        PostSummarySimpleDateFormatter dateFormatter = PostSummarySimpleDateFormatter.newInstance(context.getResources());
        CacheRepository cache = new CacheRepository(context.getContentResolver(), dateFormatter);

        return new SuperRepo(Repository.newInstance(tokenProvider), cache);
    }

    public SuperRepo(Repository api, CacheRepository cache) {
        this.api = api;
        this.cache = cache;
    }

    public Observable<Data.Comments> comments(String subreddit, String postId) {
        return api.comments(subreddit, postId);
    }

    public Observable<Feed> subreddit(String subreddit) {
        return Observable.concat(
                cache.subreddit(subreddit),
                api.subreddit(subreddit).map(cache.saveSubreddit(subreddit))
        );
    }

    public Observable<Feed> subreddit(String subreddit, String afterId) {
        return api.subreddit(subreddit, afterId).map(cache.saveSubreddit(subreddit));
    }

    public Observable<Data.Subscriptions> defaultSubscriptions() {
        return api.defaultSubscriptions();
    }

    public Observable<Data.Subscriptions> userSubscriptions() {
        return api.userSubscriptions();
    }

}
