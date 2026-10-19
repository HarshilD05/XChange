package com.cosmic.XChange.player;

import java.util.UUID;
import com.cosmic.XChange.user.AccStatus;

public class Player {

    private UUID id;
    private String username;
    private AccStatus accountStatus;
    private PlayerStatus playerStatus;

    /* Constructors */
    public Player(){
    }

    public Player(UUID id, String username, AccStatus accountStatus, PlayerStatus playerStatus) {
        this.id = id;
        this.username = username;
        this.accountStatus = accountStatus;
        this.playerStatus = playerStatus;
    }

    public Player(UUID id, PlayerStatus playerStatus) {
        this.id = id;
        this.playerStatus = playerStatus;
    }

    /* Getters and Setters */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
    
    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

}
