package com.ouchadam.loldr.data;

import com.ouchadam.loldr.data.deserialize.DeserializerFactory;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public class Repository {

    private static final String ENDPOINT = "https://oauth.reddit.com/";

    private final Api api;

    public static Repository newInstance(TokenProvider tokenProvider) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new AuthInteceptor(tokenProvider));
        OkClient okClient = new OkClient(okHttpClient);

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(new DeserializerFactory().create()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(okClient)
                .build();

        return new Repository(retrofit.create(Api.class));
    }

    Repository(Api api) {
        this.api = api;
    }

    public Observable<Data.Feed> frontPage() {
        return api.getFrontPage(100);
    }

    public Observable<Data.Feed> subreddit(String subredditName) {
        return api.getSubreddit(subredditName, 100);
    }

    public Observable<Data.Feed> subreddit(String subredditName, String afterId) {
        return api.getSubreddit(subredditName, 100, afterId);
    }

    public Observable<Data.PostDetails> postDetails(String subredditName, String postId) {
        return api.getPostDetails(subredditName, postId);
    }

    public Observable<Data.Subscriptions> defaultSubscriptions() {
        return api.getDefaultSubscriptions();
    }

    public Observable<Data.Subscriptions> userSubscriptions() {
        return api.getUserSubscriptions();
    }

    interface Api {
        @GET("/api/v1/me")
        Observable<Data.Feed> getMe();

        @GET("/r/{subreddit}/hot")
        Observable<Data.Feed> getSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit);

        @GET("/r/{subreddit}/hot")
        Observable<Data.Feed> getSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit, @Query("after") String afterId);

        @GET("/r/{subreddit}/comments/{postId}")
        Observable<Data.PostDetails> getPostDetails(@Path("subreddit") String subreddit, @Path("postId") String postId);

        @GET("/")
        Observable<Data.Feed> getFrontPage(@Query("limit") int limit);

        @GET("/subreddits/mine?limit=100&where=subscriber")
        Observable<Data.Subscriptions> getUserSubscriptions();

        @GET("/subreddits/default?limit=100")
        Observable<Data.Subscriptions> getDefaultSubscriptions();

    }

}
