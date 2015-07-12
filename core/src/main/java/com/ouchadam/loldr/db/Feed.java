package com.ouchadam.loldr.db;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;

public class Feed {

    private final DataSource<Ui.PostSummary> dataSource;
    private final String afterId;

    public Feed(DataSource<Ui.PostSummary> dataSource, String afterId) {
        this.dataSource = dataSource;
        this.afterId = afterId;
    }

    public DataSource<Ui.PostSummary> getDataSource() {
        return dataSource;
    }

    public String getAfterId() {
        return afterId;
    }
}
