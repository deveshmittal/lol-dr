package com.ouchadam.auth;

class RefreshTokenResponse {

    private final String rawToken;
    private final long expiryTime;

    public RefreshTokenResponse(String rawToken, long expiryTime) {
        this.rawToken = rawToken;
        this.expiryTime = expiryTime;
    }

    public String getRawToken() {
        return rawToken;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

}
