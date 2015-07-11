package com.ouchadam.loldr.feed;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

final class Presenter<T extends DataSource<Ui.PostSummary>> {

    private final PostSummaryAdapter<T> adapter;

    static <T extends DataSource<Ui.PostSummary>> Presenter<T> onCreate(
            Activity activity,
            SourceProvider<Ui.PostSummary, T> dataSource,
            String subreddit,
            Listener listener) {

        activity.setContentView(R.layout.activity_feed);

        activity.setTitle(subreddit);

        PostSummaryAdapter<T> adapter = new PostSummaryAdapter<>(activity.getLayoutInflater(), listener, dataSource);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.feed_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PagingScrollListener(listener));

        return new Presenter<>(adapter);
    }

    private Presenter(PostSummaryAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public void present(T dataSource) {
        adapter.notifyDataSourceChanged(dataSource);
    }

    public interface Listener extends PagingListener {
        void onPostClicked(Ui.PostSummary postSummary);
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

}
