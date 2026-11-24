package com.xchange.server.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Lobby {
    private UUID id;
    private String name;
    private String lobbyCode;
    private OffsetDateTime createdAt;
    private OffsetDateTime endedAt;
    private String logs;
    private LobbyStatus status;

    protected Lobby() {
    }

    public Lobby(String name, String lobbyCode) {
        this.name = name;
        this.lobbyCode = lobbyCode;
        this.status = LobbyStatus.OPEN;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(OffsetDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public LobbyStatus getStatus() { return status; }
    public void setStatus(LobbyStatus status) { this.status = status; }
}
