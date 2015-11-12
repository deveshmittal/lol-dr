package com.ouchadam.loldr.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import com.novoda.easycustomtabs.EasyCustomTabs;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.Executor;
import com.ouchadam.loldr.LogSubscriber;
import com.ouchadam.loldr.R;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.UserTokenProvider;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.drawer.DrawerPresenter;
import com.ouchadam.loldr.drawer.SubscriptionProvider;
import com.ouchadam.loldr.post.PostActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class FeedActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".FEED";

    private static final String EXTRA_SUBREDDIT = "subreddit";
    private static final String DEFAULT_SUBREDDIT = "all";

    private final Executor executor;

    private Presenter<PostProvider.PostSummarySource> presenter;
    private Toast toast;

    private String afterId;
    private List<Data.Post> cachedPosts = new ArrayList<>();
    private Repository repository;

    private String subreddit;
    private DrawerPresenter<SubscriptionProvider.SubscriptionSource> drawerPresenter;

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
        EasyCustomTabs.initialize(this);

        this.subreddit = getSubreddit();
        this.repository = Repository.newInstance(UserTokenProvider.newInstance(this));
        PostProvider postProvider = new PostProvider();
        this.presenter = Presenter.onCreate(this, postProvider, subreddit, listener);
        presenter.setTitle(subreddit);
        this.drawerPresenter = new DrawerPresenter<>((NavigationView) findViewById(R.id.navigation_view), drawerListener, new SubscriptionProvider());

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
        public void onClick(Ui.PostSummary postSummary) {
            startActivity(PostActivity.create(postSummary.getSubreddit(), postSummary.getId()));
        }

        @Override
        public void onClickSave(Ui.PostSummary postSummary) {
            toast("TODO: save " + postSummary.getTitle());
        }

        private void toast(String message) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(FeedActivity.this, message, Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onClickLinkFrom(Ui.PostSummary postSummary) {
            EasyCustomTabs.getInstance().navigateTo(Uri.parse(postSummary.getExternalLink()), FeedActivity.this);
        }

        @Override
        public void onNextPageRequest() {
            executor.execute(repository.subreddit(subreddit, afterId), presentResult());
        }
    };

    private Subscriber<Data.Feed> presentResult() {
        return new LogSubscriber<Data.Feed>() {
            @Override
            public void onNext(Data.Feed feed) {
                FeedActivity.this.afterId = feed.afterId();
                cachedPosts.addAll(feed.getPosts());               // TODO replace this with a cursor

                List<Ui.PostSummary> summaries = MarshallerFactory.newInstance(getResources()).posts().marshall(cachedPosts);

                presenter.present(new PostProvider.PostSummarySource(summaries));
            }
        };
    }

    private Subscriber<Data.Subscriptions> updateDrawer() {
        return new LogSubscriber<Data.Subscriptions>() {
            @Override
            public void onNext(Data.Subscriptions subscriptions) {
                List<Ui.Subscription> uiSubscriptions = new ArrayList<>();
                for (final Data.Subreddit subreddit : subscriptions.getSubscribedSubreddits()) {
                    uiSubscriptions.add(
                            new Ui.Subscription() {
                                @Override
                                public String getId() {
                                    return subreddit.getId();
                                }

                                @Override
                                public String getName() {
                                    return subreddit.getName();
                                }
                            }
                    );
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

    @Override
    protected void onResume() {
        super.onResume();
        EasyCustomTabs.getInstance().connectTo(this);
    }

    @Override
    protected void onPause() {
        EasyCustomTabs.getInstance().disconnectFrom(this);
        super.onPause();
    }
}
