package com.ouchadam.loldr.data.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ouchadam.loldr.data.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class FeedDeserializer implements JsonDeserializer<Data.Feed> {

    @Override
    public Data.Feed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject dataJson = json.getAsJsonObject().get("data").getAsJsonObject();
        String afterId = dataJson.get("after").getAsString();
        JsonArray postsJson = dataJson.get("children").getAsJsonArray();

        List<Data.Post> posts = new ArrayList<>(postsJson.size());

        for (JsonElement postRootJson : postsJson) {
            JsonObject postJson = postRootJson.getAsJsonObject().get("data").getAsJsonObject();

            Data.Post post = new Post(
                    postJson.get("id").getAsString(),
                    postJson.get("title").getAsString(),
                    postJson.get("author").getAsString(),
                    postJson.get("subreddit").getAsString(),
                    postJson.get("score").getAsInt(),
                    postJson.get("num_comments").getAsInt(),
                    postJson.get("created_utc").getAsLong(),
                    postJson.get("saved").getAsBoolean(),
                    postJson.get("thumbnail").getAsString());

            posts.add(post);
        }

        return new Feed(posts, afterId);
    }

    private static class Post implements Data.Post {

        private final String id;
        private final String title;
        private final String author;
        private final String subreddit;
        private final int score;
        private final int commentCount;
        private final long createdUtcTimeStamp;
        private final boolean saved;
        private final String thumbnail;

        public Post(String id, String title, String author, String subreddit, int score, int commentCount, long createdUtcTimeStamp, boolean saved, String thumbnail) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.subreddit = subreddit;
            this.score = score;
            this.commentCount = commentCount;
            this.createdUtcTimeStamp = createdUtcTimeStamp;
            this.saved = saved;
            this.thumbnail = thumbnail;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSubreddit() {
            return subreddit;
        }

        @Override
        public int getScore() {
            return score;
        }

        @Override
        public int getCommentCount() {
            return commentCount;
        }

        @Override
        public long getCreatedDate() {
            return createdUtcTimeStamp;
        }

        @Override
        public String getAuthor() {
            return author;
        }

        @Override
        public boolean isSaved() {
            return saved;
        }

        @Override
        public String getThumbnailUrl() {
            return thumbnail;
        }

    }

    private static class Feed implements Data.Feed {

        private final List<Data.Post> posts;
        private final String afterId;

        private Feed(List<Data.Post> posts, String afterId) {
            this.posts = posts;
            this.afterId = afterId;
        }

        @Override
        public List<Data.Post> getPosts() {
            return posts;
        }

        @Override
        public String afterId() {
            return afterId;
        }
    }
}
