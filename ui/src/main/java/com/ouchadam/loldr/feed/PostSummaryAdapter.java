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
    private final Presenter.Listener listener;
    private final SourceProvider<Ui.PostSummary, T> dataSource;
    private final Set<Ui.PostSummary> seenSummaries = new HashSet<>();

    PostSummaryAdapter(LayoutInflater layoutInflater, final Presenter.Listener listener, final SourceProvider<Ui.PostSummary, T> dataSource) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.dataSource = dataSource;
    }

    public void markSeen(Ui.PostSummary postSummary) {
        seenSummaries.add(postSummary);
        notifyDataSetChanged();
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater, listener);
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
