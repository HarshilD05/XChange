package com.xchange.server.entity;

public class User {
    private java.util.UUID id;
    private String username;
    private String password;
    private UserType userType;
    private AccountStatus accountStatus;

    protected User() {
    }

    public User(String username, String password, UserType userType) {
        this.id = java.util.UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public java.util.UUID getId() { return id; }
    public void setId(java.util.UUID id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; }
}
