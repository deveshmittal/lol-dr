CREATE TABLE IF NOT EXISTS 'post_summary' (
                                _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                reddit_id TEXT NOT NULL,
                                title TEXT NOT NULL,
                                subreddit TEXT NOT NULL,
                                hours_ago INTEGER NOT NULL,
                                comment_count INTEGER NOT NULL,
                                UNIQUE (reddit_id) ON CONFLICT REPLACE
);
