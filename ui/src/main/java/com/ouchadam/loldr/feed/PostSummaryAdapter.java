package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;

import java.util.HashSet;
import java.util.Set;

class PostSummaryAdapter<T extends DataSource<Ui.PostSummary>> extends RecyclerView.Adapter<PostSummaryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Presenter.Listener wrappedListener;
    private Set<Ui.PostSummary> seenSummaries = new HashSet<>();

    private final SourceProvider<Ui.PostSummary, T> dataSource;

    PostSummaryAdapter(LayoutInflater layoutInflater, final Presenter.Listener listener, SourceProvider<Ui.PostSummary, T> dataSource) {
        this.layoutInflater = layoutInflater;
        this.dataSource = dataSource;
        wrappedListener = new Presenter.Listener() {
            @Override
            public void onClick(Ui.PostSummary postSummary) {
                seenSummaries.add(postSummary);
                notifyDataSetChanged();
                listener.onClick(postSummary);
            }

            @Override
            public void onClickSave(Ui.PostSummary postSummary) {
                listener.onClickSave(postSummary);
            }

            @Override
            public void onClickLinkFrom(Ui.PostSummary postSummary) {
                notifyDataSetChanged();
                listener.onClick(postSummary);
                listener.onClickLinkFrom(postSummary);
            }

            @Override
            public void onNextPageRequest() {
                listener.onNextPageRequest();
            }
        };
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater, wrappedListener);
    }

    @Override
    public void onBindViewHolder(PostSummaryViewHolder viewHolder, int position) {
        Ui.PostSummary postSummary = dataSource.get(position);
        viewHolder.bind(postSummary, seenSummaries.contains(postSummary));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void notifyDataSourceChanged(T dataSource) {
        int previousSize = getItemCount();

        this.dataSource.swap(dataSource);
        notifyItemRangeInserted(previousSize, dataSource.size());
    }

}
