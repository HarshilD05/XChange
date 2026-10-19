package com.cosmic.XChange.user.dto;

import java.util.UUID;
import com.cosmic.XChange.user.AccStatus;
import com.cosmic.XChange.user.User;
import com.cosmic.XChange.user.UserType;

public record UserResp(
    UUID id,
    String username,
    AccStatus status,
    UserType type
) {
    public static UserResp fromEntity(User user) {
        if (user == null) return null;
        return new UserResp(user.getId(), user.getUsername(), user.getAccStatus(), user.getUserType());
    }
}
