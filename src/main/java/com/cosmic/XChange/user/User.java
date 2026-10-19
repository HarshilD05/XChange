package com.cosmic.XChange.user;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String passwordHash;
    private UserType userType;
    private AccStatus accStatus;
    
    /* Constructors */
    public User() {
        
    }

    public User(UUID id, String username, String passwordHash, UserType userType, AccStatus accStatus) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.accStatus = accStatus;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public AccStatus getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(AccStatus accStatus) {
        this.accStatus = accStatus;
    }
}
