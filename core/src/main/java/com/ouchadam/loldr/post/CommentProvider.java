package com.ouchadam.loldr.post;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.data.Data;

import java.util.ArrayList;
import java.util.List;

public class CommentProvider implements Presenter.CommentSourceProvider {

    private final CommentSource commentSource = new CommentSource();

    @Override
    public void swap(DataSource<Ui.Comment> source) {
        commentSource.dataPosts.addAll(((CommentSource) source).dataPosts);
    }

    @Override
    public Ui.Comment get(int position) {
        return commentSource.get(position);
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public int size() {
        return commentSource.size();
    }

    static class CommentSource implements DataSource<Ui.Comment> {

        private final List<Data.Comment> dataPosts;

        private CommentSource() {
            this(new ArrayList<Data.Comment>());
        }

        CommentSource(List<Data.Comment> dataPosts) {
            this.dataPosts = dataPosts;
        }

        @Override
        public int size() {
            return dataPosts.size();
        }

        @Override
        public Ui.Comment get(final int position) {
            return new Ui.Comment() {
                @Override
                public String getId() {
                    return dataPosts.get(position).getId();
                }

                @Override
                public String getBody() {
                    return dataPosts.get(position).getBody();
                }

                @Override
                public String getAuthor() {
                    return dataPosts.get(position).getAuthor();
                }

                @Override
                public int getDepth() {
                    return dataPosts.get(position).getDepth();
                }

                @Override
                public boolean isMore() {
                    return dataPosts.get(position).isMore();
                }
            };
        }

        @Override
        public void close() {
            // do nothing
        }

    }

}
