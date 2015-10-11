package com.ouchadam.loldr.post;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.data.Data;

public class PostDetailsProvider implements Presenter.PostDetailsSourceProvider<PostDetailsProvider.PostDetailsSource> {

    private PostDetailsSource detailsSource = new PostDetailsSource(null);

    @Override
    public void swap(PostDetailsSource source) {
        detailsSource = source;
    }

    @Override
    public Ui.PostDetails get(int position) {
        return detailsSource.get(position);
    }

    @Override
    public int size() {
        return detailsSource.size();
    }

    static class PostDetailsSource implements DataSource<Ui.PostDetails> {

        private final Data.Post post;

        public PostDetailsSource(Data.Post post) {
            this.post = post;
        }

        @Override
        public int size() {
            return post == null ? 0 : 1;
        }

        @Override
        public Ui.PostDetails get(final int position) {
            return new Ui.PostDetails() {
                @Override
                public String getId() {
                    return post.getId();
                }

                @Override
                public String getBody() {
                    return post.getContent();
                }

                @Override
                public boolean isSelfPost() {
                    return !getBody().isEmpty();
                }

                @Override
                public Ui.PostSummary getPostSummary() {
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
                            return post.getScore() + "";
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
                            return post.getCreatedDate() + "";
                        }

                        @Override
                        public String getSubreddit() {
                            return post.getSubreddit();
                        }

                        @Override
                        public String getCommentCount() {
                            return post.getCommentCount() + "";
                        }

                        @Override
                        public String getExternalLink() {
                            return post.getExternalLink();
                        }
                    };
                }
            };
        }

    }

}
