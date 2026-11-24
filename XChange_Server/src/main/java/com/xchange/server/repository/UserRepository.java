package com.xchange.server.repository;

import com.xchange.server.entity.AccountStatus;
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
public class UserRepository {
    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> findAll() {
        String sql = "SELECT id, username, password_hash, user_type, account_status FROM users ORDER BY username";
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) users.add(mapUser(result));
            return users;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read users", exception);
        }
    }

    public Optional<User> findById(UUID id) {
        String sql = "SELECT id, username, password_hash, user_type, account_status FROM users WHERE id = ?";
        return findOne(sql, id);
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, user_type, account_status FROM users WHERE username = ?";
        return findOne(sql, username);
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not check user", exception);
        }
    }

    public User save(User user) {
        String sql = "INSERT INTO users (id, username, password_hash, user_type, account_status) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET username = EXCLUDED.username, "
                + "password_hash = EXCLUDED.password_hash, user_type = EXCLUDED.user_type, "
                + "account_status = EXCLUDED.account_status";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUserType().name());
            statement.setString(5, user.getAccountStatus().name());
            statement.executeUpdate();
            return user;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save user", exception);
        }
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete user", exception);
        }
    }

    private Optional<User> findOne(String sql, Object parameter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, parameter);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(mapUser(result)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not read user", exception);
        }
    }

    private User mapUser(ResultSet result) throws SQLException {
        User user = new User(
                result.getString("username"),
                result.getString("password_hash"),
                UserType.valueOf(result.getString("user_type"))
        );
        user.setId(result.getObject("id", UUID.class));
        user.setAccountStatus(AccountStatus.valueOf(result.getString("account_status")));
        return user;
    }
}
