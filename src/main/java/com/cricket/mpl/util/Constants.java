package com.cricket.mpl.util;

public enum Constants {
    USER_ROLE_ADMIN("ADMIN"),
    USER_ROLE_PLAYER("PLAYER"),
    USER_ROLE_CAPTION("CAPTION");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
