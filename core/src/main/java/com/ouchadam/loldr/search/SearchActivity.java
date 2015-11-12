package com.ouchadam.loldr.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.R;
import com.ouchadam.loldr.feed.FeedActivity;

public class SearchActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".SEARCH";
    private EditText searchQuery;

    public static Intent createIntent() {
        return new Intent(ACTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQuery = (EditText) findViewById(R.id.search_query);
        findViewById(R.id.search_go).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO do an actual search with results
                        startActivity(FeedActivity.create(searchQuery.getText().toString()));
                    }
                }
        );
    }

}
