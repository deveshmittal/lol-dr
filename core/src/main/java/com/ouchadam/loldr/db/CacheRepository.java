package com.ouchadam.loldr.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.SimpleDate;
import com.ouchadam.loldr.feed.PostProvider;
import com.ouchadam.loldr.post.PostSummarySimpleDateFormatter;
import com.ouchadam.loldr.sql.DB;

import rx.Observable;
import rx.functions.Func1;

public class CacheRepository {

    private final ContentResolver contentResolver;
    private final PostSummarySimpleDateFormatter postSummarySimpleDateFormatter;

    public CacheRepository(ContentResolver contentResolver, PostSummarySimpleDateFormatter postSummarySimpleDateFormatter) {
        this.contentResolver = contentResolver;
        this.postSummarySimpleDateFormatter = postSummarySimpleDateFormatter;
    }

    public Observable<DataSource<Ui.PostSummary>> subreddit(String subreddit) {
        return Observable.just(subreddit)
                .map(queryForSubreddit());
    }

    private Func1<String, DataSource<Ui.PostSummary>> queryForSubreddit() {
        return new Func1<String, DataSource<Ui.PostSummary>>() {
            @Override
            public PostProvider.PostSummarySource call(String subreddit) {
                Cursor result = contentResolver.query(null, null, null, null, null);
                if (result.moveToFirst()) {
                    return new PostProvider.PostSummarySource();
                }

                throw new RuntimeException("empty db");
            }
        };
    }

    public Func1<? super Data.Feed, DataSource<Ui.PostSummary>> saveSubreddit(final String subreddit) {
        return new Func1<Data.Feed, DataSource<Ui.PostSummary>>() {
            @Override
            public DataSource<Ui.PostSummary> call(Data.Feed feed) {

                int postCount = feed.getPosts().size();
                ContentValues[] bulkValues = new ContentValues[postCount];
                for (int index = 0; index < postCount; index++) {
                    Data.Post post = feed.getPosts().get(index);
                    ContentValues values = new ContentValues();

                    DB.PostSummary.setTitle(post.getTitle(), values);
                    DB.PostSummary.setCommentCount(post.getCommentCount(), values);
                    DB.PostSummary.setRedditId(post.getId(), values);
                    DB.PostSummary.setSubreddit(post.getSubreddit(), values);
                    DB.PostSummary.setHoursAgoLabel(postSummarySimpleDateFormatter.format(SimpleDate.from(post.getCreatedDate())), values);

                    bulkValues[index] = values;
                }

                contentResolver.bulkInsert(null, bulkValues);
                return queryForSubreddit().call(subreddit);
            }
        };
    }
}
