package com.ouchadam.loldr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.novoda.easycustomtabs.EasyCustomTabs;
import com.novoda.easycustomtabs.navigation.EasyCustomTabsIntentBuilder;
import com.novoda.easycustomtabs.navigation.IntentCustomizer;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyCustomTabs.initialize(this);
        EasyCustomTabs.getInstance().withIntentCustomizer(new IntentCustomizer() {
            @Override
            public EasyCustomTabsIntentBuilder onCustomiseIntent(EasyCustomTabsIntentBuilder easyCustomTabsIntentBuilder) {
                easyCustomTabsIntentBuilder.withToolbarColor(getResources().getColor(R.color.primary));
                return easyCustomTabsIntentBuilder;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EasyCustomTabs.getInstance().connectTo(this);
    }

    @Override
    protected void onPause() {
        EasyCustomTabs.getInstance().disconnectFrom(this);
        super.onPause();
    }

}
