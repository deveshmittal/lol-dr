package com.ouchadam.loldr.data;

import com.google.gson.GsonBuilder;
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
                .setConverter(new GsonConverter(new DeserializerFactory().feed(new GsonBuilder()).create()))
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

    interface Api {
        @GET("/api/v1/me")
        Observable<Data.Feed> getMe();

        @GET("/r/{subreddit}/hot")
        Observable<Data.Feed> getSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit);

        @GET("/")
        Observable<Data.Feed> getFrontPage(@Query("limit") int limit);

    }

}
