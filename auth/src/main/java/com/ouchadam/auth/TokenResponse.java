package com.ouchadam.auth;

class TokenResponse {

    private final String rawToken;
    private final String refreshToken;
    private final long expiryTime;

    public TokenResponse(String rawToken, String refreshToken, long expiryTime) {
        this.rawToken = rawToken;
        this.refreshToken = refreshToken;
        this.expiryTime = expiryTime;
    }

    public String getRawToken() {
        return rawToken;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
