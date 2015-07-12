package com.ouchadam.loldr;

import android.content.Context;
import android.content.SharedPreferences;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.loldr.data.TokenProvider;

public class UserTokenProvider implements TokenProvider {

    private final TokenAcquirer tokenAcquirer;
    private final CurrentUserProvider currentUserProvider;

    public static UserTokenProvider newInstance(Context context) {
        return new UserTokenProvider(TokenAcquirer.newInstance(context), PreferenceUserProvider.newInstance(context));
    }

    public UserTokenProvider(TokenAcquirer tokenAcquirer, CurrentUserProvider currentUserProvider) {
        this.tokenAcquirer = tokenAcquirer;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public synchronized AccessToken provideAccessToken() {
        Token token = tokenAcquirer.acquireToken(currentUserProvider.provideCurrentUser()).toBlocking().first();
        return new TokenProvider.AccessToken(token.getAccessToken());
    }

    @Override
    public void invalidateToken() {
        tokenAcquirer.invalidateToken(currentUserProvider.provideCurrentUser());
    }

    interface CurrentUserProvider {
        String provideCurrentUser();
    }

    public static class PreferenceUserProvider implements CurrentUserProvider {

        private static final String USER = "user";
        private final SharedPreferences preferences;

        public static PreferenceUserProvider newInstance(Context context) {
            return new PreferenceUserProvider(context.getSharedPreferences("com.ouchadam.user", Context.MODE_PRIVATE));
        }

        private PreferenceUserProvider(SharedPreferences preferences) {
            this.preferences = preferences;
        }

        @Override
        public String provideCurrentUser() {
            return preferences.contains(USER) ? preferences.getString(USER, null) : null;
        }

        public void updateCurrentUser(String userName) {
            preferences.edit().putString(USER, userName).apply();
        }
    }

}
