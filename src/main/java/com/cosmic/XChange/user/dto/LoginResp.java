package com.cosmic.XChange.user.dto;

public record LoginResp(
    boolean success,
    String message,
    UserResp user
) {
    public LoginResp(boolean success, String message) {
        this(success, message, null);
    }
}