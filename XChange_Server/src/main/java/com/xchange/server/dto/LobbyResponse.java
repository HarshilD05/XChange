package com.xchange.server.dto;

import com.xchange.server.entity.Lobby;
import com.xchange.server.entity.LobbyStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LobbyResponse(
        UUID id,
        String name,
        String lobbyCode,
        OffsetDateTime createdAt,
        OffsetDateTime endedAt,
        String logs,
        LobbyStatus status
) {
    public static LobbyResponse from(Lobby lobby) {
        return new LobbyResponse(
                lobby.getId(),
                lobby.getName(),
                lobby.getLobbyCode(),
                lobby.getCreatedAt(),
                lobby.getEndedAt(),
                lobby.getLogs(),
                lobby.getStatus()
        );
    }
}
