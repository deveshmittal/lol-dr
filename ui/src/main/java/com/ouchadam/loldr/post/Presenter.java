package com.ouchadam.loldr.post;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

public class Presenter {

    private final CommentAdapter adapter;

    static Presenter onCreate(Activity activity, SourceProvider<Ui.Comment> dataSource, Listener listener) {
        activity.setContentView(R.layout.activity_post);

        CommentAdapter adapter = new CommentAdapter(dataSource, activity.getLayoutInflater(), listener);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        return new Presenter(adapter);
    }

    private Presenter(CommentAdapter adapter) {
        this.adapter = adapter;
    }

    public void present(DataSource<Ui.Comment> dataSource) {
        adapter.notifyDataSourceChanged(dataSource);
    }

    public interface Listener {
        void onCommentClicked(Ui.Comment comment);
    }

    interface CommentSourceProvider extends SourceProvider<Ui.Comment> {

    }

}
