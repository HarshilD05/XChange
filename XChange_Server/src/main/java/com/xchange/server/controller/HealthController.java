package com.xchange.server.controller;

import com.xchange.server.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private static final HealthResponse XCHANGE_ACTIVE_RESPONSE = new HealthResponse(
            "XChange Main Server",
            "ACTIVE",
            "XChange main server is online",
            "1.0"
    );

    @GetMapping("/api/health")
    public HealthResponse apiHealth() {
        return XCHANGE_ACTIVE_RESPONSE;
    }

    @GetMapping("/health")
    public HealthResponse health() {
        return XCHANGE_ACTIVE_RESPONSE;
    }
}
