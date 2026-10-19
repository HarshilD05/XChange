package com.cosmic.XChange.user.dto;

public record UpdatePasswordRequest (
    String currentPassword,
    String newPassword
) {}