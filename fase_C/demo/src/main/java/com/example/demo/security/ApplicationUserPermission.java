package com.example.demo.security;

public enum ApplicationUserPermission {
    SAMPLE_READ("sample:read"),
    SAMPLE_WRITE("sample:write");

    private final String userPermission;

    ApplicationUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getUserPermission() {
        return userPermission;
    }
}
