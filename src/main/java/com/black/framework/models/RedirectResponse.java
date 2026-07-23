package com.black.framework.models;

public class RedirectResponse {
    private final String toPath;

    public RedirectResponse(String toPath) {
        this.toPath = toPath;
    }

    public String getToPath() {
        return toPath;
    }
}
