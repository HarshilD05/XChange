package com.cosmic.XChange.player;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import com.cosmic.XChange.user.AccStatus;

@Repository 
public class PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlayerRepository(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    private final RowMapper<Player> playerRowMapper = (rs, rowNum) -> new Player(
        rs.getObject("id", UUID.class),
        rs.getString("username"),
        AccStatus.valueOf(rs.getString("account_status")),
        PlayerStatus.valueOf(rs.getString("player_status"))
    );

    /* Find */
    public Optional<Player> findById(UUID id) {
        String query = """
            SELECT p.id, u.username, u.account_status, p.player_status
            FROM players p
            JOIN users u ON p.id = u.id
            WHERE p.id = ?;
        """;
        return jdbcTemplate.query(query, playerRowMapper, id).stream().findFirst();
    }

    public List<Player> findAll() {
        String query = """
            SELECT p.id, u.username, u.account_status, p.player_status
            FROM players p
            JOIN users u ON p.id = u.id;
        """;
        return jdbcTemplate.query(query, playerRowMapper);
    }

    /* Insert */
    public Player insert(Player player) {
        String query = "INSERT INTO players (id, player_status) VALUES (?, ?)";
        jdbcTemplate.update(query, player.getId(), player.getPlayerStatus().name());
        return player;
    }

    /* Update */
    public void updatePlayerStatus(UUID id, PlayerStatus status) {
        String query = "UPDATE players SET player_status = ? WHERE id = ?";
        jdbcTemplate.update(query, status.name(), id);
    }

    /* Delete */
    public void deleteById(UUID id) {
        String query = "DELETE FROM players WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}
