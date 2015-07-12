package com.ouchadam.loldr.db;

public class AfterId {

    public static final AfterId MISSING = new AfterId("-1");

    private final String value;

    public AfterId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
