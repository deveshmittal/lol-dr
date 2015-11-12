package com.ouchadam.loldr.feed;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class Presenter<T extends DataSource<Ui.PostSummary>> {

    private final PostSummaryAdapter<T> adapter;
    private final TextView titleView;
    private final SwipeRefreshLayout swipeLayout;

    static <T extends DataSource<Ui.PostSummary>> Presenter<T> onCreate(
            AppCompatActivity activity,
            SourceProvider<Ui.PostSummary, T> dataSource,
            Listener listener, OnRefreshListener onRefreshListener) {

        activity.setContentView(R.layout.activity_feed);
        TextView titleView = (TextView) activity.findViewById(R.id.toolbar_title);

        PostSummaryAdapter<T> adapter = new PostSummaryAdapter<>(activity.getLayoutInflater(), listener, dataSource);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.feed_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PagingScrollListener(listener));

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.primary);
        swipeLayout.setOnRefreshListener(onRefreshListener);

        return new Presenter<>(adapter, titleView, swipeLayout);
    }

    private Presenter(PostSummaryAdapter<T> adapter, TextView titleView, SwipeRefreshLayout swipeLayout) {
        this.adapter = adapter;
        this.titleView = titleView;
        this.swipeLayout = swipeLayout;
    }

    public void present(T dataSource) {
        adapter.notifyDataSourceChanged(dataSource);
    }

    public void setTitle(String subreddit) {
        titleView.setText(subreddit);
    }

    public void markSeen(Ui.PostSummary postSummary) {
        adapter.markSeen(postSummary);
    }

    public void setLoadingFinished() {
        swipeLayout.setRefreshing(false);
    }

    public interface Listener extends PagingListener {
        void onClick(Ui.PostSummary postSummary);

        void onClickSave(Ui.PostSummary postSummary);

        void onClickLinkFrom(Ui.PostSummary postSummary);
    }

    interface PagingListener {
        void onNextPageRequest();
    }

    static class PagingScrollListener extends RecyclerView.OnScrollListener {

        private final PagingListener pagingListener;
        private int prevItemCount = 0;

        PagingScrollListener(PagingListener pagingListener) {
            this.pagingListener = pagingListener;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int itemCount = adapter.getItemCount();

            if (prevItemCount != itemCount && lastVisibleItemPosition >= (itemCount - 1) - 15) {
                prevItemCount = itemCount;
                pagingListener.onNextPageRequest();
            }
        }

    }

    interface PostSourceProvider<T extends DataSource<Ui.PostSummary>> extends SourceProvider<Ui.PostSummary, T> {

    }

    interface OnRefreshListener extends SwipeRefreshLayout.OnRefreshListener {

    }

}
