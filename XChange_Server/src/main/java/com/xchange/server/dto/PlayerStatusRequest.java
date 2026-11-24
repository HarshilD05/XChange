package com.xchange.server.dto;

import com.xchange.server.entity.PlayerStatus;
import jakarta.validation.constraints.NotNull;

public record PlayerStatusRequest(@NotNull PlayerStatus playerStatus) {
}
