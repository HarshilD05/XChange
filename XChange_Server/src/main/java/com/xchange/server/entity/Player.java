package com.xchange.server.entity;

public class Player {
    private java.util.UUID id;
    private User user;
    private PlayerStatus playerStatus = PlayerStatus.OFFLINE;

    protected Player() {
    }

    public Player(User user) {
        this.id = user.getId();
        this.user = user;
        this.playerStatus = PlayerStatus.OFFLINE;
    }
    
    public java.util.UUID getId() { return id; }
    public String getUsername() { return user == null ? null : user.getUsername(); }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public User getUser() {
        return user;
    }
}
