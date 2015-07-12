package com.ouchadam.loldr.feed;

import android.database.AbstractCursor;
import android.database.Cursor;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.sql.DB;

public class PostProvider implements Presenter.PostSourceProvider {

    private DataSource<Ui.PostSummary> postSummarySource = new PostSummarySource();

    @Override
    public void swap(DataSource<Ui.PostSummary> source) {
        this.postSummarySource.close();
        this.postSummarySource = source;
    }

    @Override
    public Ui.PostSummary get(int position) {
        return postSummarySource.get(position);
    }

    @Override
    public void close() {
        postSummarySource.close();
    }

    @Override
    public int size() {
        return postSummarySource.size();
    }

    public static class PostSummarySource implements DataSource<Ui.PostSummary> {

        private Cursor cursor = new AbstractCursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public String getString(int i) {
                return null;
            }

            @Override
            public short getShort(int i) {
                return 0;
            }

            @Override
            public int getInt(int i) {
                return 0;
            }

            @Override
            public long getLong(int i) {
                return 0;
            }

            @Override
            public float getFloat(int i) {
                return 0;
            }

            @Override
            public double getDouble(int i) {
                return 0;
            }

            @Override
            public boolean isNull(int i) {
                return false;
            }
        };

        public PostSummarySource() {
        }

        public PostSummarySource(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public int size() {
            return cursor.getCount();
        }

        @Override
        public Ui.PostSummary get(int position) {
            cursor.moveToPosition(position);
            return new Ui.PostSummary() {
                @Override
                public String getId() {
                    return DB.PostSummary.getRedditId(cursor);
                }

                @Override
                public String getTitle() {
                    return DB.PostSummary.getTitle(cursor);
                }

                @Override
                public String getTime() {
                    return DB.PostSummary.getHoursAgoLabel(cursor);
                }

                @Override
                public String getSubreddit() {
                    return DB.PostSummary.getSubredditLabel(cursor);
                }

                @Override
                public String getCommentCount() {
                    return String.valueOf(DB.PostSummary.getCommentCount(cursor));
                }
            };
        }

        @Override
        public void close() {
            cursor.close();
        }

    }
}
