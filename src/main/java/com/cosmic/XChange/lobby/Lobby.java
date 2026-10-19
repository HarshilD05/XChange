package com.cosmic.XChange.lobby;

import java.util.UUID;

public class Lobby {

    private UUID id;
    private LobbyStatus status;

    /* Constructors */
    public Lobby() {
    }

    public Lobby(UUID id, LobbyStatus status) {
        this.id = id;
        this.status = status;
    }

    public Lobby(LobbyStatus status) {
        this.status = status;
    }

    /* Getters and Setters */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }
}
