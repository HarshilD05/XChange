CREATE TABLE Users (
    id UUID PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type VARCHAR(8) NOT NULL CHECK (user_type IN ('ADMIN', 'PLAYER')),
    account_status VARCHAR(10) NOT NULL CHECK (account_status IN ('ACTIVE', 'SUSPENDED'))
);

CREATE TABLE Players (
    id UUID PRIMARY KEY REFERENCES Users(id) ON DELETE CASCADE,
    status VARCHAR(10) NOT NULL CHECK (status IN ('OFFLINE', 'ONLINE', 'IN_LOBBY'))
);

CREATE TABLE Lobby (
    id UUID PRIMARY KEY,
    status VARCHAR(8) NOT NULL CHECK (status IN ('OPEN', 'CLOSED') )
);
