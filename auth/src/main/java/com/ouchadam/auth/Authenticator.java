package com.ouchadam.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.functions.Func1;

public class Authenticator extends AbstractAccountAuthenticator {

    public static final String KEY_TOKEN_EXPIRY = "KEY_TOKEN_EXPIRY";

    private final Context context;
    private final AccountManager accountManager;

    public Authenticator(Context context) {
        super(context);
        this.context = context;
        this.accountManager = AccountManager.get(context);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
                             String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        return addAccount(response);
    }

    private Bundle addAccount(AccountAuthenticatorResponse response) {
        Bundle result = new Bundle();
        Intent intent = OAuthSignInActivity.create(context, response);
        result.putParcelable(AccountManager.KEY_INTENT, intent);
        return result;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        String refreshToken = accountManager.getPassword(account);
        String token = accountManager.peekAuthToken(account, authTokenType);

        if (refreshTokenIsInvalid(refreshToken)) {

            return promptToLogin(response, options);
        }

        if (token == null || tokenHasExpired(account)) {
            try {
                return refreshAccount(account, refreshToken);
            } catch (Exception e) {
                throw new RuntimeException("we failed to refresh our token");
            }
        }

        return reuseExistingToken(account, token);
    }

    private boolean refreshTokenIsInvalid(String token) {
        return TextUtils.isEmpty(token);
    }

    private boolean tokenHasExpired(Account account) {
        long tokenExpiry = Long.parseLong(accountManager.getUserData(account, KEY_TOKEN_EXPIRY));
        return System.currentTimeMillis() >= tokenExpiry;
    }

    private Bundle refreshAccount(Account account, String refreshToken) throws Exception {
        RefreshTokenResponse refreshedToken = refreshAccount(refreshToken).toBlocking().first();
        accountManager.setAuthToken(account, account.type, refreshedToken.getRawToken());

        return createFromAccount(account, refreshedToken);
    }

    public Observable<RefreshTokenResponse> refreshAccount(String refreshToken) {
        return Observable.just(refreshToken).map(new Func1<String, RefreshTokenResponse>() {
            @Override
            public RefreshTokenResponse call(String refreshToken) {
                try {
                    MediaType textMediaType = MediaType.parse("application/x-www-form-urlencoded");
                    Request request = new Request.Builder()
                            .url("https://www.reddit.com/api/v1/access_token")
                            .post(RequestBody.create(textMediaType, "grant_type=refresh_token&refresh_token=" + refreshToken))
                            .addHeader("Authorization", Credentials.basic(OAuthSignInActivity.CLIENT_ID, ""))
                            .build();

                    Response response = new OkHttpClient().newCall(request).execute();

                    String result = response.body().string();

                    Log.e("!!!", result);

                    return parseRefreshToken(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private RefreshTokenResponse parseRefreshToken(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String rawToken = jsonObject.getString("access_token");
            int expiryInSeconds = jsonObject.getInt("expires_in");
            return new RefreshTokenResponse(rawToken, expirySecondsToTimeStamp(expiryInSeconds));
        } catch (JSONException e) {
            throw new RuntimeException("failed to get token", e);
        }
    }

    private long expirySecondsToTimeStamp(int expiryInSeconds) {
        return TimeUnit.SECONDS.toMillis(expiryInSeconds) + System.currentTimeMillis();
    }

    private Bundle reuseExistingToken(Account account, String token) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, token);
        return bundle;
    }

    private Bundle createFromAccount(Account account, RefreshTokenResponse token) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, token.getRawToken());

        Bundle extra = createUserFromToken(token);
        bundle.putBundle(AccountManager.KEY_USERDATA, extra);
        return bundle;
    }

    private Bundle createUserFromToken(RefreshTokenResponse token) {
        Bundle userData = new Bundle();
        userData.putLong(KEY_TOKEN_EXPIRY, token.getExpiryTime());
        return userData;
    }

    private Bundle promptToLogin(AccountAuthenticatorResponse response, Bundle bundle) {
        Bundle activityOptions = new Bundle();
        activityOptions.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Intent accountActivity = OAuthSignInActivity.create(context, response);
        accountActivity.putExtras(activityOptions);

        bundle.putParcelable(AccountManager.KEY_INTENT, accountActivity);
        bundle.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }
}
