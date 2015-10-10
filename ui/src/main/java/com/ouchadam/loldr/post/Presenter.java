package com.ouchadam.loldr.post;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

public class Presenter {

    private final PostDetailsAdapter adapter;

    static Presenter onCreate(Activity activity, PostDetailsSourceProvider p, CommentSourceProvider commentProvider, Listener listener) {
        activity.setContentView(R.layout.activity_post);

        PostDetailsAdapter adapter = new PostDetailsAdapter(commentProvider, p, activity.getLayoutInflater(), listener);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        return new Presenter(adapter);
    }

    private Presenter(PostDetailsAdapter adapter) {
        this.adapter = adapter;
    }

    public void presentPostDetails(DataSource<Ui.PostDetails> postDetailsSource) {
        adapter.notifyPostSourceChanged(postDetailsSource);
    }

    public void presentComments(DataSource<Ui.Comment> commentSource) {
        adapter.notifyCommentSourceChanged(commentSource);
    }

    public interface Listener {
        void onCommentClicked(Ui.Comment comment);
    }

    interface CommentSourceProvider<T extends DataSource<Ui.Comment>> extends SourceProvider<Ui.Comment, T> {

    }

    interface PostDetailsSourceProvider<T extends DataSource<Ui.PostDetails>> extends SourceProvider<Ui.PostDetails, T> {

    }

}
