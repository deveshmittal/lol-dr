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

class PostDetailsDeserializer implements JsonDeserializer<Data.PostDetails> {

    private final PostDeserializer postDeserializer;

    public PostDetailsDeserializer(PostDeserializer postDeserializer) {
        this.postDeserializer = postDeserializer;
    }

    @Override
    public Data.PostDetails deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Data.Post post = postDeserializer.deserialize(getPostDetailsRootJson(json), typeOfT, context);

        JsonObject commentsRootJson = json.getAsJsonArray().get(1).getAsJsonObject();
        JsonArray rootCommentThread = commentsRootJson.get("data").getAsJsonObject().get("children").getAsJsonArray();

        List<Data.Comment> comments = new ArrayList<>();
        for (JsonElement jsonElement : rootCommentThread) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            comments.addAll(recurseOverAllComments(asJsonObject, new ArrayList<Data.Comment>(), 0));
        }

        return new PostDetails(post, new Comments(comments));
    }

    private JsonElement getPostDetailsRootJson(JsonElement json) {
        return json.getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray().get(0).getAsJsonObject().get("data");
    }

    private List<Data.Comment> recurseOverAllComments(JsonObject commentThread, List<Data.Comment> comments, int depth) {
        String kind = commentThread.get("kind").getAsString();
        if (kind.equals("more")) {
            // TODO these type of comments require a different api call /api/morechildren taking the child ids
            Data.Comment moreComment = moreComment(commentThread.get("data").getAsJsonObject(), depth);
            comments.add(moreComment);
            return comments;
        }

        JsonObject commentJson = commentThread.get("data").getAsJsonObject();
        comments.add(parseComment(commentJson, depth));

        JsonElement repliesRoot = commentJson.get("replies");
        if (repliesRoot.isJsonPrimitive()) {
            return comments;
        }

        JsonArray replies = repliesRoot.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();

        for (JsonElement reply : replies) {
            comments.addAll(recurseOverAllComments(reply.getAsJsonObject(), new ArrayList<Data.Comment>(), depth + 1));
        }

        return comments;
    }

    private Data.Comment moreComment(JsonObject data, int depth) {
        String id = data.get("id").getAsString();
        String name = data.get("name").getAsString();
        return new MoreComment(id, name, depth);
    }

    private Comment parseComment(JsonObject jsonComment, int depth) {
        String commentId = jsonComment.get("id").getAsString().trim();
        String commentBody = jsonComment.get("body").getAsString().trim();
        String commentName = jsonComment.get("name").getAsString().trim();
        long commentTimestamp = jsonComment.get("created_utc").getAsLong();
        String commentAuthor = jsonComment.get("author").getAsString().trim();

        return new Comment(commentId, commentBody, commentName, commentTimestamp, commentAuthor, depth);
    }

    private static class MoreComment implements Data.Comment {

        private final String id;
        private final String name;
        private final int depth;

        public MoreComment(String id, String name, int depth) {
            this.id = id;
            this.name = name;
            this.depth = depth;
        }

        @Override
        public String getBody() {
            throw new IllegalAccessError("This is a more comment! no body brah");
        }

        @Override
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getAuthor() {
            throw new IllegalAccessError("This is a more comment! no author brah");
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public boolean isMore() {
            return true;
        }
    }

    private static class Comments implements Data.Comments {

        private final List<Data.Comment> comments;

        public Comments(List<Data.Comment> comments) {
            this.comments = comments;
        }

        @Override
        public List<Data.Comment> getComments() {
            return comments;
        }
    }

    private static class Comment implements Data.Comment {

        private final String id;
        private final String body;
        private final String name;
        private final long createdUtc;
        private final String commentAuthor;
        private final int depth;

        public Comment(String id, String body, String name, long createdUtc, String commentAuthor, int depth) {
            this.id = id;
            this.body = body;
            this.name = name;
            this.createdUtc = createdUtc;
            this.commentAuthor = commentAuthor;
            this.depth = depth;
        }

        @Override
        public String getBody() {
            return body;
        }

        @Override
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getCreatedUtc() {
            return createdUtc;
        }

        @Override
        public String getAuthor() {
            return commentAuthor;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public boolean isMore() {
            return false;
        }
    }

    private static class PostDetails implements Data.PostDetails {

        private final Data.Post post;
        private final Data.Comments comments;

        private PostDetails(Data.Post post, Data.Comments comments) {
            this.post = post;
            this.comments = comments;
        }

        @Override
        public Data.Post getPost() {
            return post;
        }

        @Override
        public Data.Comments getComments() {
            return comments;
        }
    }

}
