package com.ouchadam.loldr.feed;

import android.content.res.Resources;

import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.SimpleDate;
import com.ouchadam.loldr.post.PostSummarySimpleDateFormatter;

import java.util.ArrayList;
import java.util.List;

class MarshallerFactory {

    private final PostSummarySimpleDateFormatter postSummarySimpleDateFormatter;

    static MarshallerFactory newInstance(Resources resources) {
        PostSummarySimpleDateFormatter postSummarySimpleDateFormatter = PostSummarySimpleDateFormatter.newInstance(resources);
        return new MarshallerFactory(postSummarySimpleDateFormatter);
    }

    MarshallerFactory(PostSummarySimpleDateFormatter postSummarySimpleDateFormatter) {
        this.postSummarySimpleDateFormatter = postSummarySimpleDateFormatter;
    }

    public Marshaller<List<Ui.PostSummary>, List<Data.Post>> posts() {
        return marshallList(post());
    }

    private <T, F> Marshaller<List<T>, List<F>> marshallList(final Marshaller<T, F> marshaller) {
        return new Marshaller<List<T>, List<F>>() {
            @Override
            public List<T> marshall(List<F> from) {
                List<T> marshalledItems = new ArrayList<>(from.size());
                for (F f : from) {
                    marshalledItems.add(marshaller.marshall(f));
                }
                return marshalledItems;
            }
        };
    }

    private Marshaller<Ui.PostSummary, Data.Post> post() {
        return new Marshaller<Ui.PostSummary, Data.Post>() {
            @Override
            public Ui.PostSummary marshall(final Data.Post post) {
                return new Ui.PostSummary() {
                    @Override
                    public String getId() {
                        return post.getId();
                    }

                    @Override
                    public String getTitle() {
                        return post.getTitle();
                    }

                    @Override
                    public String getScore() {
                        return String.valueOf(post.getScore());
                    }

                    @Override
                    public String getAuthor() {
                        return post.getAuthor();
                    }

                    @Override
                    public String getImageUrl() {
                        return post.getThumbnailUrl();
                    }

                    @Override
                    public String getTime() {
                        SimpleDate date = SimpleDate.from(post.getCreatedDate());
                        return postSummarySimpleDateFormatter.format(date);
                    }

                    @Override
                    public String getSubreddit() {
                        return post.getSubreddit();
                    }

                    @Override
                    public String getCommentCount() {
                        return String.valueOf(post.getCommentCount());
                    }

                    @Override
                    public String getExternalLink() {
                        return post.getExternalLink();
                    }
                };
            }
        };
    }

    public interface Marshaller<T, F> {
        T marshall(F from);
    }

}
