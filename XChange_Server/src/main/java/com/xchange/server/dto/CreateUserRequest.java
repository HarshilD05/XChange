package com.xchange.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max = 32) String username,
        @NotBlank @Size(min = 8, max = 64) String password
) {
}
