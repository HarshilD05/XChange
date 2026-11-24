package com.xchange.server.dto;

import com.xchange.server.entity.AccountStatus;
import jakarta.validation.constraints.NotNull;

public record AccountStatusRequest(@NotNull AccountStatus accountStatus) {
}
