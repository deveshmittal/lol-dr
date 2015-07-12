package com.ouchadam.loldr.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

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

    public Observable<Feed> subreddit(String subreddit) {
        return Observable.just(subreddit)
                .map(queryForSubreddit(AfterId.MISSING))
                .onErrorResumeNext(Observable.<Feed>empty());
    }

    private Func1<String, Feed> queryForSubreddit(final AfterId afterId) {
        return new Func1<String, Feed>() {
            @Override
            public Feed call(String subreddit) {
                Cursor result = contentResolver.query(
                        ContentProvider.POSTS,
                        null,
                        DB.Columns.PostSummary.SubredditKey + "=?",
                        new String[]{subreddit},
                        null
                );
                if (result.moveToFirst()) {
                    return new Feed(new PostProvider.PostSummarySource(result), afterId);
                }
                throw new RuntimeException("empty db for " + subreddit);
            }
        };
    }

    public Func1<? super Data.Feed, Feed> saveSubreddit(final String subreddit) {
        return new Func1<Data.Feed, Feed>() {
            @Override
            public Feed call(Data.Feed feed) {
                int postCount = feed.getPosts().size();
                ContentValues[] postValues = createPostContentValues(feed, postCount, subreddit);

                contentResolver.bulkInsert(ContentProvider.POSTS, postValues);

                return queryForSubreddit(new AfterId(feed.afterId())).call(subreddit);
            }
        };
    }

    private ContentValues[] createPostContentValues(Data.Feed feed, int postCount, String subredditKey) {
        ContentValues[] postValues = new ContentValues[postCount];

        for (int index = 0; index < postCount; index++) {
            Data.Post post = feed.getPosts().get(index);
            ContentValues values = new ContentValues();

            DB.PostSummary.setTitle(post.getTitle(), values);
            DB.PostSummary.setCommentCount(post.getCommentCount(), values);
            DB.PostSummary.setRedditId(post.getId(), values);
            DB.PostSummary.setSubredditKey(subredditKey, values);
            DB.PostSummary.setSubredditLabel(post.getSubreddit(), values);
            DB.PostSummary.setHoursAgoLabel(postSummarySimpleDateFormatter.format(SimpleDate.from(post.getCreatedDate())), values);
            DB.PostSummary.setAfterId(feed.afterId(), values);
            DB.PostSummary.setAuthor(post.getAuthor(), values);
            DB.PostSummary.setImageUrl(post.getThumbnailUrl(), values);
            DB.PostSummary.setScore(post.getScore(), values);

            postValues[index] = values;
        }
        return postValues;
    }
}
