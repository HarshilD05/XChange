package com.xchange.server.dto;

import com.xchange.server.entity.User;
import com.xchange.server.entity.UserType;
import com.xchange.server.entity.AccountStatus;

import java.util.UUID;

public record UserResponse(UUID id, String username, UserType userType, AccountStatus accountStatus) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getUserType(),
                user.getAccountStatus()
        );
    }
}
