package com.xchange.server.dto;

import com.xchange.server.entity.Player;
import com.xchange.server.entity.PlayerStatus;
import com.xchange.server.entity.AccountStatus;

import java.util.UUID;

public record PlayerResponse(UUID id, String username, PlayerStatus playerStatus, AccountStatus accountStatus) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getUsername(),
                player.getPlayerStatus(),
                player.getUser().getAccountStatus()
        );
    }
}
