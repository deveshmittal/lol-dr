package com.ouchadam.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import rx.Observable;
import rx.functions.Func1;

public class TokenAcquirer {

    private final Foo foo;
    private final AnonTokenStorage anonTokenStorage;
    private final String accountType;

    private final AccountManager accountManager;

    public static TokenAcquirer newInstance(Context context) {
        UUID deviceId = UUID.randomUUID();
        return new TokenAcquirer(new Foo(deviceId), AnonTokenStorage.from(context), context.getString(R.string.account_type), AccountManager.get(context));
    }

    public TokenAcquirer(Foo foo, AnonTokenStorage anonTokenStorage, String accountType, AccountManager accountManager) {
        this.foo = foo;
        this.anonTokenStorage = anonTokenStorage;
        this.accountType = accountType;
        this.accountManager = accountManager;
    }

    public Observable<Token> acquireToken(String accountName) {
        if (accountName == null) {
            return getAnonToken();
        } else {
            return getUserToken(accountName);
        }
    }

    private Observable<Token> getAnonToken() {
        AnonToken storedToken = anonTokenStorage.getToken();

        if (storedToken == null || storedToken.hasExpired()) {
            return foo.requestAnonymousAccessToken().map(saveAnonToken());
        }

        return Observable.<Token>just(storedToken);
    }

    private Observable<Token> getUserToken(String accountName) {
        return Observable.just(accountName)
                .map(accountForName())
                .map(tokenFromAccount());

    }

    private Func1<Account, Token> tokenFromAccount() {
        return new Func1<Account, Token>() {
            @Override
            public Token call(Account account) {
                try {
                    final String accessToken = accountManager.blockingGetAuthToken(account, "", true);
                    return new Token() {
                        @Override
                        public String getAccessToken() {
                            return accessToken;
                        }
                    };
                } catch (OperationCanceledException | AuthenticatorException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private Func1<String, Account> accountForName() {
        return new Func1<String, Account>() {
            @Override
            public Account call(String accountName) {
                Account[] accountsByType = accountManager.getAccountsByType(accountType);
                for (Account account : accountsByType) {
                    if (account.name.equals(accountName)) {
                        return account;
                    }
                }
                throw new RuntimeException("account : " + accountName + " does not exist");
            }
        };
    }

    public Observable<UserToken> requestNewToken(String oauthRedirect) {
        return foo.requestUserToken(oauthRedirect).map(fetchUserName());
    }

    private Func1<TokenResponse, UserToken> fetchUserName() {
        return new Func1<TokenResponse, UserToken>() {
            @Override
            public UserToken call(TokenResponse tokenResponse) {
                String accountName = new UserFetcher().fetchUserName(tokenResponse);
                return new UserToken(accountName, tokenResponse.getRawToken(), tokenResponse.getRefreshToken(), tokenResponse.getExpiryTime());
            }
        };
    }

    private Func1<AnonToken, Token> saveAnonToken() {
        return new Func1<AnonToken, Token>() {
            @Override
            public AnonToken call(AnonToken input) {
                Log.e("!!!", " saving token");
                anonTokenStorage.storeToken(input);
                return input;
            }
        };
    }

    public void invalidateToken(String accountName) {
        accountManager.invalidateAuthToken(accountType, accountManager.peekAuthToken(accountForName().call(accountName), ""));
    }
}
