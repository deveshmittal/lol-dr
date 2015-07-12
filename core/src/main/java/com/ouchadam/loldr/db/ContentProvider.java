package com.ouchadam.loldr.db;

import android.net.Uri;

import com.ouchadam.loldr.sql.DB;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class ContentProvider extends SQLiteContentProviderImpl {

    private static final String AUTHORITY = "content://com.ouchadam/";

    public static final Uri POSTS = buildUri(DB.Tables.PostSummary);

    private static Uri buildUri(String tableOrView) {
        return Uri.parse(AUTHORITY).buildUpon().appendPath(tableOrView).build();
    }

}