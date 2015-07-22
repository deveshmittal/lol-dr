package com.ouchadam.loldr.data;

import java.util.List;

public final class Data {

    private Data() {
        throw new IllegalAccessError("Just a holder not a real class");
    }

    public interface Post {

        String getId();

        String getTitle();

        String getSubreddit();

        int getScore();

        int getCommentCount();

        long getCreatedDate();

        String getAuthor();

        boolean isSaved();

        String getThumbnailUrl();

        String getExternalLink();
    }

    public interface Feed {

        List<Data.Post> getPosts();

        String afterId();

    }

    public interface Comment {

        String getId();

        String getBody();

        String getAuthor();

        int getDepth();

        boolean isMore();
    }

    public interface Comments {

        List<Data.Comment> getComments();

    }

    public interface Subscriptions {

        List<Subreddit> getSubscribedSubreddits();

    }

    public interface Subreddit {

        String getId();

        String getName();

    }
}
