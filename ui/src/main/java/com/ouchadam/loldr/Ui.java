package com.ouchadam.loldr;

public final class Ui {

    private Ui() {
        throw new IllegalAccessError("Just a holder not a real class");
    }

    public interface PostSummary {

        String getId();

        String getTitle();

        String getScore();

        String getAuthor();

        String getImageUrl();

        String getTime();

        String getSubreddit();

        String getCommentCount();

    }

    public interface Comment {

        String getId();

        String getBody();

        String getAuthor();

        int getDepth();

        boolean isMore();

    }

    public interface Subscription {

        String getId();

        String getName();

    }
}
