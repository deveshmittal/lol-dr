package com.ouchadam.loldr.feed;

import android.os.Bundle;
import android.util.Log;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedActivity extends BaseActivity {

    private TokenAcquirer tokenAcquirer;
    private Presenter presenter;
    private MarshallerFactory marshallerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.marshallerFactory = new MarshallerFactory();
        this.tokenAcquirer = TokenAcquirer.newInstance();
        this.presenter = Presenter.onCreate(this);

        Repository.newInstance(provider).frontPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presentResult());
    }

    private Subscriber<Data.Feed> presentResult() {
        return new Subscriber<Data.Feed>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "Something went wrong", e);
            }

            @Override
            public void onNext(Data.Feed feed) {
                List<Data.Post> dataPosts = feed.getPosts();
                List<PostSummary> uiPosts = marshallerFactory.posts().marshall(dataPosts);

                presenter.present(uiPosts);
            }
        };
    }

    private TokenProvider provider = new TokenProvider() {
        @Override
        public TokenProvider.AccessToken provideAccessToken() {
            Token token = tokenAcquirer.acquireToken(UserTokenRequest.anon()).toBlocking().first();
            return new TokenProvider.AccessToken(token.getUrlResponse());
        }
    };

}
