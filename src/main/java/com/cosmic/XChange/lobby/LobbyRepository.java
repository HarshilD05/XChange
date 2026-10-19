package com.cosmic.XChange.lobby;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LobbyRepository {
    private final JdbcTemplate jdbcTemplate;

    public LobbyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Lobby> lobbyRowMapper = (rs, rowNum) -> new Lobby(
            rs.getObject("id", UUID.class),
            LobbyStatus.valueOf(rs.getString("status"))
    );

    /* Find */
    public Optional<Lobby> findById(UUID id) {
        String query = """
            SELECT id, status
            FROM lobby
            WHERE id = ?;
        """;
        return jdbcTemplate.query(query, lobbyRowMapper, id).stream().findFirst();
    }

    public List<Lobby> findAll() {
        String query = """
            SELECT id, status
            FROM lobby;
        """;
        return jdbcTemplate.query(query, lobbyRowMapper);
    }

    public List<Lobby> findByStatus(LobbyStatus status) {
        String query = """
            SELECT id, status
            FROM lobby
            WHERE status = ?;
        """;
        return jdbcTemplate.query(query, lobbyRowMapper, status.name());
    }

    /* Insert */
    public Lobby insert(Lobby lobby) {
        if (lobby.getStatus() == null) {
            lobby.setStatus(LobbyStatus.OPEN);
        }
        
        String query = """
            INSERT INTO lobby (id, status)
            VALUES (gen_random_uuid(), ?)
            RETURNING id;
        """;
        UUID generatedId = jdbcTemplate.queryForObject(query, UUID.class, lobby.getStatus().name());
        lobby.setId(generatedId);
        return lobby;        
    }

    /* Update */
    public void updateStatus(UUID id, LobbyStatus status) {
        String query = """
            UPDATE lobby
            SET status = ?
            WHERE id = ?;
        """;
        jdbcTemplate.update(query, status.name(), id);
    }

    /* Delete */
    public void deleteById(UUID id) {
        String query = """
            DELETE FROM lobby
            WHERE id = ?;
        """;
        jdbcTemplate.update(query, id);
    }
}
