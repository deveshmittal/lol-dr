package com.ouchadam.loldr.db;

import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;

import rx.Observable;

public class SuperRepo {

    private final Repository repository;

    public static SuperRepo newInstance(TokenProvider tokenProvider) {
        return new SuperRepo(Repository.newInstance(tokenProvider));
    }

    public SuperRepo(Repository repository) {
        this.repository = repository;
    }

    public Observable<Data.Comments> comments(String subreddit, String postId) {
        return repository.comments(subreddit, postId);
    }

    public Observable<Data.Feed> subreddit(String subreddit) {
        return repository.subreddit(subreddit);
    }

    public Observable<Data.Feed> subreddit(String subreddit, String afterId) {
        return repository.subreddit(subreddit, afterId);
    }

    public Observable<Data.Subscriptions> defaultSubscriptions() {
        return repository.defaultSubscriptions();
    }

    public Observable<Data.Subscriptions> userSubscriptions() {
        return repository.userSubscriptions();
    }
}
