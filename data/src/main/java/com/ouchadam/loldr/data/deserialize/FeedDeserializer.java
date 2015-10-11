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

    private final PostDeserializer postDeserializer;

    public FeedDeserializer(PostDeserializer postDeserializer) {
        this.postDeserializer = postDeserializer;
    }

    @Override
    public Data.Feed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject dataJson = json.getAsJsonObject().get("data").getAsJsonObject();
        String afterId = dataJson.get("after").getAsString();
        JsonArray postsJson = dataJson.get("children").getAsJsonArray();

        List<Data.Post> posts = new ArrayList<>(postsJson.size());

        for (JsonElement postRootJson : postsJson) {
            JsonObject postJson = postRootJson.getAsJsonObject().get("data").getAsJsonObject();
            Data.Post post = postDeserializer.deserialize(postJson, typeOfT, context);
            posts.add(post);
        }

        return new Feed(posts, afterId);
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
