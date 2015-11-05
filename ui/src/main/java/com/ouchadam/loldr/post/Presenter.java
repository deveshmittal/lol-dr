package com.ouchadam.loldr.post;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.ui.R;

public class Presenter {

    private final PostDetailsAdapter adapter;
    private final TextView titleView;

    static Presenter onCreate(Activity activity, PostDetailsSourceProvider p, CommentSourceProvider commentProvider, final Listener listener) {
        activity.setContentView(R.layout.activity_post);

        TextView titleView = (TextView) activity.findViewById(R.id.toolbar_title);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTitleClicked();
            }
        });

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        PostDetailsAdapter adapter = new PostDetailsAdapter(new ViewHolderFactory(layoutInflater), commentProvider, p);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        return new Presenter(adapter, titleView);
    }

    private Presenter(PostDetailsAdapter adapter, TextView titleView) {
        this.adapter = adapter;
        this.titleView = titleView;
    }

    public void presentPostDetails(DataSource<Ui.PostDetails> postDetailsSource) {
        adapter.notifyPostSourceChanged(postDetailsSource);
    }

    public void presentComments(DataSource<Ui.Comment> commentSource) {
        adapter.notifyCommentSourceChanged(commentSource);
    }

    public void setTitle(String subreddit) {
        titleView.setText(subreddit);
    }

    public interface Listener {
        void onCommentClicked(Ui.Comment comment);

        void onTitleClicked();
    }

    interface CommentSourceProvider<T extends DataSource<Ui.Comment>> extends SourceProvider<Ui.Comment, T> {

    }

    interface PostDetailsSourceProvider<T extends DataSource<Ui.PostDetails>> extends SourceProvider<Ui.PostDetails, T> {

    }

}
