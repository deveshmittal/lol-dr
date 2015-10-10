package com.ouchadam.loldr.data.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ouchadam.loldr.data.Data;

import java.lang.reflect.Type;

class PostDeserializer implements JsonDeserializer<Data.Post> {

    @Override
    public Data.Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject postJson = json.getAsJsonObject();
        return new Post(
                postJson.get("id").getAsString(),
                postJson.get("title").getAsString(),
                postJson.get("author").getAsString(),
                postJson.get("subreddit").getAsString(),
                postJson.get("score").getAsInt(),
                postJson.get("num_comments").getAsInt(),
                postJson.get("created_utc").getAsLong(),
                postJson.get("saved").getAsBoolean(),
                postJson.get("thumbnail").getAsString(),
                postJson.get("url").getAsString(),
                postJson.get("selftext").getAsString()
        );
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
        private final String url;
        private final String content;

        public Post(String id, String title, String author, String subreddit, int score, int commentCount,
                    long createdUtcTimeStamp, boolean saved, String thumbnail, String url, String content) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.subreddit = subreddit;
            this.score = score;
            this.commentCount = commentCount;
            this.createdUtcTimeStamp = createdUtcTimeStamp;
            this.saved = saved;
            this.thumbnail = thumbnail;
            this.url = url;
            this.content = content;
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

        @Override
        public String getExternalLink() {
            return url;
        }

        @Override
        public String getContent() {
            return content;
        }

    }

}
