package com.xchange.server.repository;

import com.xchange.server.entity.Lobby;
import com.xchange.server.entity.LobbyStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LobbyRepository {
    private final DataSource dataSource;

    public LobbyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Lobby> findAll() {
        String sql = "SELECT id, name, lobby_code, created_at, ended_at, logs, status FROM lobbies ORDER BY created_at DESC";
        List<Lobby> lobbies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) lobbies.add(mapLobby(result));
            return lobbies;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read lobbies", exception);
        }
    }

    public Optional<Lobby> findById(UUID id) {
        String sql = "SELECT id, name, lobby_code, created_at, ended_at, logs, status FROM lobbies WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(mapLobby(result)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read lobby", exception);
        }
    }

    public List<Lobby> findByLobbyCode(String lobbyCode) {
        String sql = "SELECT id, name, lobby_code, created_at, ended_at, logs, status "
                + "FROM lobbies WHERE lobby_code = ? ORDER BY created_at DESC";
        List<Lobby> lobbies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lobbyCode);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) lobbies.add(mapLobby(result));
            }
            return lobbies;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read lobbies", exception);
        }
    }

    public boolean activeLobbyCodeExists(String lobbyCode) {
        String sql = "SELECT 1 FROM lobbies "
                + "WHERE lobby_code = ? AND status IN ('OPEN', 'ONGOING')";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lobbyCode);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not check lobby code", exception);
        }
    }

    public Lobby save(Lobby lobby) {
        if (lobby.getId() == null) lobby.setId(UUID.randomUUID());
        if (lobby.getCreatedAt() == null) lobby.setCreatedAt(OffsetDateTime.now());
        String sql = "INSERT INTO lobbies (id, name, lobby_code, created_at, ended_at, logs, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, "
                + "lobby_code = EXCLUDED.lobby_code, ended_at = EXCLUDED.ended_at, "
                + "logs = EXCLUDED.logs, status = EXCLUDED.status";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, lobby.getId());
            statement.setString(2, lobby.getName());
            statement.setString(3, lobby.getLobbyCode());
            statement.setObject(4, lobby.getCreatedAt());
            statement.setObject(5, lobby.getEndedAt());
            statement.setString(6, lobby.getLogs());
            statement.setString(7, lobby.getStatus().name());
            statement.executeUpdate();
            return lobby;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save lobby", exception);
        }
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM lobbies WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete lobby", exception);
        }
    }

    private Lobby mapLobby(ResultSet result) throws SQLException {
        Lobby lobby = new Lobby(result.getString("name"), result.getString("lobby_code"));
        lobby.setId(result.getObject("id", UUID.class));
        lobby.setCreatedAt(result.getObject("created_at", OffsetDateTime.class));
        lobby.setEndedAt(result.getObject("ended_at", OffsetDateTime.class));
        lobby.setLogs(result.getString("logs"));
        lobby.setStatus(LobbyStatus.valueOf(result.getString("status")));
        return lobby;
    }
}
