package com.ouchadam.loldr.search;

import android.content.Intent;
import android.os.Bundle;

import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.R;

public class SearchActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".SEARCH";

    public static Intent createIntent() {
        return new Intent(ACTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

}
