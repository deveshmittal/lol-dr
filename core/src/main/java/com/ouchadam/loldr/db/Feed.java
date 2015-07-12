package com.ouchadam.loldr.db;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;

public class Feed {

    private final DataSource<Ui.PostSummary> dataSource;
    private final AfterId afterId;

    public Feed(DataSource<Ui.PostSummary> dataSource, AfterId afterId) {
        this.dataSource = dataSource;
        this.afterId = afterId;
    }

    public DataSource<Ui.PostSummary> getDataSource() {
        return dataSource;
    }

    public AfterId getAfterId() {
        return afterId;
    }
}
