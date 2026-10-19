package com.cosmic.XChange.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getObject("id", UUID.class),
            rs.getString("username"),
            rs.getString("password_hash"),
            UserType.valueOf(rs.getString("user_type")),
            AccStatus.valueOf(rs.getString("account_status"))
    );

    /* Find */
    public Optional<User> findByID(UUID id) {
        String query = """
            SELECT id, username, password_hash, user_type, account_status
            FROM users
            WHERE id = ?;
        """;
        return jdbcTemplate.query(query, userRowMapper, id).stream().findFirst();
    }

    public Optional<User> findByUsername(String username) {
        String query = """
            SELECT id, username, password_hash, user_type, account_status
            FROM users
            WHERE username = ?;
        """;
        return jdbcTemplate.query(query, userRowMapper, username).stream().findFirst();
    }

    public List<User> findAll() {
        String query = """
            SELECT id, username, password_hash, user_type, account_status
            FROM users;
        """;
        return jdbcTemplate.query(query, userRowMapper);
    }

    public List<User> findByStatus(AccStatus status) {
        String query = """
            SELECT id, username, password_hash, user_type, account_status
            FROM users
            WHERE account_status = ?;
        """;
        return jdbcTemplate.query(query, userRowMapper, status.name());
    }

    public List<User> findByType(UserType type) {
        String query = """
            SELECT id, username, password_hash, user_type, account_status
            FROM users
            WHERE user_type = ?;
        """;
        return jdbcTemplate.query(query, userRowMapper, type.name());
    }


    /* Insert */
    public User insert(User u) {
        String query = """
            INSERT INTO users (id, username, password_hash, user_type, account_status)
            VALUES (gen_random_uuid(), ?, ?, ?, ?)
            RETURNING id; 
        """;

        UUID generatedId = jdbcTemplate.queryForObject(
            query,
            UUID.class,
            u.getUsername(),
            u.getPasswordHash(),
            u.getUserType().name(),
            u.getAccStatus().name()
        );
        u.setId(generatedId);
        return u;
    }

    /* Update */
    public void updatePasswordHash(UUID id, String passwordHash) {
        String query = """
            UPDATE users
            SET password_hash = ?
            WHERE id = ?;
        """;
        jdbcTemplate.update(query, passwordHash, id);
    }

    public void updateAccStatus(UUID id, AccStatus status) {
        String query = """
            UPDATE users
            SET account_status = ?
            WHERE id = ?;
        """;
        jdbcTemplate.update(query, status.name(), id);
    }

    
    /* Delete */
    public void deleteById(UUID id) {
        String query = """
            DELETE FROM users
            WHERE id = ?;
        """;
        jdbcTemplate.update(query, id);
    }

    public void deleteByUsername(String username) {
        String query = """
            DELETE FROM users
            WHERE username = ?;
        """;
        jdbcTemplate.update(query, username);
    }
}
