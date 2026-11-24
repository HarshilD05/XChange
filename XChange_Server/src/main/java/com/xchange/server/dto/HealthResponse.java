package com.xchange.server.dto;

public record HealthResponse(
        String service,
        String status,
        String message,
        String version
) {
}
