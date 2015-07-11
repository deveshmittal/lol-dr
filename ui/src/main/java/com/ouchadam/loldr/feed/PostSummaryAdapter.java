package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;

class PostSummaryAdapter<T extends DataSource<PostSummary>> extends RecyclerView.Adapter<PostSummaryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Presenter.Listener listener;

    private final SourceProvider<PostSummary, T> dataSource;

    PostSummaryAdapter(LayoutInflater layoutInflater, Presenter.Listener listener, SourceProvider<PostSummary, T> dataSource) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.dataSource = dataSource;
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostSummary postSummary = dataSource.get((Integer) view.getTag(PostSummaryViewHolder.POSITION_KEY));
                listener.onPostClicked(postSummary);
            }
        });
    }

    @Override
    public void onBindViewHolder(PostSummaryViewHolder viewHolder, int position) {
        PostSummary postSummary = dataSource.get(position);

        viewHolder.setPosition(position);
        viewHolder.setTitle(postSummary.getTitle());
        viewHolder.setTime(postSummary.getTime());
        viewHolder.setCommentsCount(postSummary.getCommentCount());
        viewHolder.setSubreddit(postSummary.getSubreddit());
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
