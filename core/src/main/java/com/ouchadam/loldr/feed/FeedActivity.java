package com.ouchadam.loldr.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;

import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.Executor;
import com.ouchadam.loldr.R;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.UserTokenProvider;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.db.Feed;
import com.ouchadam.loldr.db.SuperRepo;
import com.ouchadam.loldr.drawer.DrawerPresenter;
import com.ouchadam.loldr.drawer.SubscriptionProvider;
import com.ouchadam.loldr.post.PostActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class FeedActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".FEED";

    private static final String EXTRA_SUBREDDIT = "subreddit";
    private static final String DEFAULT_SUBREDDIT = "askreddit";

    private final Executor executor;

    private Presenter presenter;

    private String afterId;
    private List<Data.Post> cachedPosts = new ArrayList<>();
    private SuperRepo repository;

    private String subreddit;
    private DrawerPresenter drawerPresenter;

    public static Intent create(String subreddit) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_SUBREDDIT, subreddit);
        return intent;
    }

    public FeedActivity() {
        this.executor = new Executor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subreddit = getSubreddit();
        this.repository = SuperRepo.newInstance(UserTokenProvider.newInstance(this), this);
        PostProvider postProvider = new PostProvider();
        this.presenter = Presenter.onCreate(this, postProvider, subreddit, listener);
        this.drawerPresenter = new DrawerPresenter((NavigationView) findViewById(R.id.navigation_view), drawerListener, new SubscriptionProvider());

        executor.execute(repository.subreddit(subreddit), presentResult());

        if (UserTokenProvider.PreferenceUserProvider.newInstance(this).provideCurrentUser() == null) {
            executor.execute(repository.defaultSubscriptions(), updateDrawer());
        } else {
            executor.execute(repository.userSubscriptions(), updateDrawer());
        }
    }

    private String getSubreddit() {
        return getIntent().hasExtra(EXTRA_SUBREDDIT) ? getIntent().getStringExtra(EXTRA_SUBREDDIT) : DEFAULT_SUBREDDIT;
    }

    private final Presenter.Listener listener = new Presenter.Listener() {
        @Override
        public void onPostClicked(Ui.PostSummary postSummary) {
            startActivity(PostActivity.create(postSummary.getSubreddit(), postSummary.getId()));
        }

        @Override
        public void onNextPageRequest() {
            executor.execute(repository.subreddit(subreddit, afterId), presentResult());
        }
    };

    private Subscriber<Feed> presentResult() {
        return new Subscriber<Feed>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "Something went wrong", e);
            }

            @Override
            public void onNext(Feed feed) {
                afterId = feed.getAfterId();

                Log.e("!!!", "source is : " + feed.getDataSource().size() + " big : next id : " + afterId);

                presenter.present(feed.getDataSource());
            }
        };
    }

    private Subscriber<Data.Subscriptions> updateDrawer() {
        return new Subscriber<Data.Subscriptions>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Data.Subscriptions subscriptions) {
                List<Ui.Subscription> uiSubscriptions = new ArrayList<>();
                for (final Data.Subreddit subreddit : subscriptions.getSubscribedSubreddits()) {
                    uiSubscriptions.add(new Ui.Subscription() {
                        @Override
                        public String getId() {
                            return subreddit.getId();
                        }

                        @Override
                        public String getName() {
                            return subreddit.getName();
                        }
                    });
                }

                drawerPresenter.present(new SubscriptionProvider.SubscriptionSource(uiSubscriptions));
            }
        };
    }

    private final DrawerPresenter.Listener drawerListener = new DrawerPresenter.Listener() {
        @Override
        public void onSubscriptionClicked(Ui.Subscription subscription) {
            startActivity(FeedActivity.create(subscription.getName()));
        }
    };

}
