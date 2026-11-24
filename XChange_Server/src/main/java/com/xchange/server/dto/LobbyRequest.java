package com.xchange.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LobbyRequest(
        @NotBlank @Size(max = 64) String name
) {
}
