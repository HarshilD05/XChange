package com.xchange.server.dto;

import com.xchange.server.entity.LobbyStatus;
import jakarta.validation.constraints.NotNull;

public record LobbyStatusRequest(@NotNull LobbyStatus status) {
}
