package com.ouchadam.loldr.drawer;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;

public class DrawerPresenter<T extends DataSource<Ui.Subscription>> {

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_NAME = "name";

    private final NavigationView navigationView;
    private final Listener listener;
    private final SourceProvider<Ui.Subscription, T> dataSource;

    public DrawerPresenter(NavigationView navigationView, Listener listener, SourceProvider<Ui.Subscription, T> dataSource) {
        this.navigationView = navigationView;
        this.listener = listener;
        this.dataSource = dataSource;
    }

    public void present(T source) {
        dataSource.swap(source);
        navigationView.getMenu().clear();
        navigationView.setNavigationItemSelectedListener(onMenuClicked);

        navigationView.getMenu().add("Search");

        for (int index = 0; index < dataSource.size(); index++) {
            Ui.Subscription subscription = dataSource.get(index);
            Intent intent = toFeedIntent(subscription);
            navigationView.getMenu().add(subscription.getName()).setIntent(intent);
        }
    }

    private final NavigationView.OnNavigationItemSelectedListener onMenuClicked = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Intent intent = menuItem.getIntent();

            if (intent != null && intent.hasExtra(EXTRA_NAME)) {
                listener.onSubscriptionClicked(fromIntent(intent));
            } else {
                listener.onSearchClicked();
            }

            return true;
        }
    };

    private Intent toFeedIntent(Ui.Subscription subscription) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, subscription.getId());
        intent.putExtra(EXTRA_NAME, subscription.getName());
        return intent;
    }

    private Ui.Subscription fromIntent(final Intent intent) {
        return new Ui.Subscription() {
            @Override
            public String getId() {
                return intent.getStringExtra(EXTRA_ID);
            }

            @Override
            public String getName() {
                return intent.getStringExtra(EXTRA_NAME);
            }
        };
    }

    public interface Listener {
        void onSearchClicked();

        void onSubscriptionClicked(Ui.Subscription subscription);
    }

    interface SubscriptionSourceProvider<T extends DataSource<Ui.Subscription>> extends SourceProvider<Ui.Subscription, T> {
    }

}
