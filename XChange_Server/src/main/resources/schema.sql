-- Run this file as xchange_admin, not as xchange_user.
-- xchange_user should receive only CRUD privileges on these tables.

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type VARCHAR(8) NOT NULL,
    account_status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT users_user_type_check
        CHECK (user_type IN ('PLAYER', 'ADMIN')),
    CONSTRAINT users_account_status_check
        CHECK (account_status IN ('ACTIVE', 'SUSPENDED'))
);
COMMENT ON TABLE users IS 'XChange accounts. Passwords are stored only as BCrypt hashes.';
COMMENT ON COLUMN users.id IS 'Unique account identifier.';
COMMENT ON COLUMN users.username IS 'Unique login name for the account.';
COMMENT ON COLUMN users.password_hash IS 'BCrypt password hash; never store or return the plaintext password.';
COMMENT ON COLUMN users.user_type IS 'Account role: ADMIN or PLAYER.';
COMMENT ON COLUMN users.account_status IS 'Account state: ACTIVE or SUSPENDED.';


CREATE TABLE IF NOT EXISTS players (
    id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(10) NOT NULL DEFAULT 'OFFLINE',
    CONSTRAINT players_status_check
        CHECK (status IN ('OFFLINE', 'ONLINE', 'IN_LOBBY'))
);
COMMENT ON TABLE players IS 'Player-specific state for accounts whose user_type is PLAYER.';
COMMENT ON COLUMN players.id IS 'References users.id and identifies the player.';
COMMENT ON COLUMN players.status IS 'Current player state: OFFLINE, ONLINE, or IN_LOBBY.';


CREATE TABLE IF NOT EXISTS lobbies (
    id UUID PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    lobby_code VARCHAR(6) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ended_at TIMESTAMPTZ,
    logs TEXT,
    status VARCHAR(8) NOT NULL DEFAULT 'OPEN',
    CONSTRAINT lobbies_lobby_code_check
        CHECK (lobby_code ~ '^[A-Z0-9]{6}$'),
    CONSTRAINT lobbies_status_check
        CHECK (status IN ('OPEN', 'ONGOING', 'CLOSED'))
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_active_lobby_code
    ON lobbies (lobby_code)
    WHERE status IN ('OPEN', 'ONGOING');

COMMENT ON TABLE lobbies IS 'Game lobby lifecycle and post-game records.';
COMMENT ON COLUMN lobbies.id IS 'Unique lobby identifier.';
COMMENT ON COLUMN lobbies.name IS 'Display name of the lobby.';
COMMENT ON COLUMN lobbies.lobby_code IS 'Six-character uppercase alphanumeric code used by clients to find lobbies.';
COMMENT ON COLUMN lobbies.created_at IS 'Timestamp when the lobby was created.';
COMMENT ON COLUMN lobbies.ended_at IS 'Timestamp when the lobby ended, if applicable.';
COMMENT ON COLUMN lobbies.logs IS 'Post-game or lobby event log text.';
COMMENT ON COLUMN lobbies.status IS 'Lobby lifecycle state: OPEN, ONGOING, or CLOSED.';
