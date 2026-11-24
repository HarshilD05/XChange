package com.xchange.server.repository;

import com.xchange.server.entity.AccountStatus;
import com.xchange.server.entity.Player;
import com.xchange.server.entity.PlayerStatus;
import com.xchange.server.entity.User;
import com.xchange.server.entity.UserType;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerRepository {
    private final DataSource dataSource;

    public PlayerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Player> findAll() {
        String sql = "SELECT u.id, u.username, u.password_hash, u.user_type, u.account_status, p.status "
                + "FROM players p JOIN users u ON u.id = p.id ORDER BY u.username";
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) players.add(mapPlayer(result));
            return players;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read players", exception);
        }
    }

    public Optional<Player> findById(UUID id) {
        String sql = "SELECT u.id, u.username, u.password_hash, u.user_type, u.account_status, p.status "
                + "FROM players p JOIN users u ON u.id = p.id WHERE p.id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(mapPlayer(result)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read player", exception);
        }
    }

    public Optional<Player> findByUsername(String username) {
        String sql = "SELECT u.id, u.username, u.password_hash, u.user_type, u.account_status, p.status "
                + "FROM players p JOIN users u ON u.id = p.id WHERE u.username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(mapPlayer(result)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read player", exception);
        }
    }

    public Player save(Player player) {
        String sql = "INSERT INTO players (id, status) VALUES (?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET status = EXCLUDED.status";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, player.getId());
            statement.setString(2, player.getPlayerStatus().name());
            statement.executeUpdate();
            return player;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save player", exception);
        }
    }

    private Player mapPlayer(ResultSet result) throws SQLException {
        User user = new User(
                result.getString("username"),
                result.getString("password_hash"),
                UserType.valueOf(result.getString("user_type"))
        );
        user.setId(result.getObject("id", UUID.class));
        user.setAccountStatus(AccountStatus.valueOf(result.getString("account_status")));
        Player player = new Player(user);
        player.setPlayerStatus(PlayerStatus.valueOf(result.getString("status")));
        return player;
    }
}
