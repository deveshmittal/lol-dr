package com.ouchadam.loldr.data;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

class AuthInteceptor implements Interceptor {

    private static final int MAX_RETRIES=3;

    private final TokenProvider tokenProvider;

    private int retries = 0;

    AuthInteceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        TokenProvider.AccessToken accessToken = tokenProvider.provideAccessToken();

        Response response = chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization", "bearer " + accessToken.get())
                        .build()
        );

        if (response.code() == 401 && allowedToRetry()) {
            tokenProvider.invalidateToken();
            retries++;
            return intercept(chain);
        }
        retries = 0;

        return response;
    }

    private boolean allowedToRetry() {
        return retries < MAX_RETRIES;
    }
}
