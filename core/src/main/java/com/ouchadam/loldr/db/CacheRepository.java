package com.ouchadam.loldr.db;

import android.content.ContentResolver;

import com.ouchadam.loldr.feed.PostProvider;

import rx.Observable;
import rx.functions.Func1;

public class CacheRepository {

    private final ContentResolver contentResolver;

    public CacheRepository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Observable<PostProvider.PostSummarySource> subreddit(String subreddit) {
        return Observable.just(subreddit).map(new Func1<String, PostProvider.PostSummarySource>() {
            @Override
            public PostProvider.PostSummarySource call(String s) {
                return new PostProvider.PostSummarySource();
            }
        });
    }
}
