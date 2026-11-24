package com.xchange.server.service;

import com.xchange.server.dto.LobbyRequest;
import com.xchange.server.dto.LobbyResponse;
import com.xchange.server.dto.LobbyStatusRequest;
import com.xchange.server.entity.Lobby;
import com.xchange.server.entity.LobbyStatus;
import com.xchange.server.repository.LobbyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
public class LobbyService {

    private static final char[] CODE_CHARACTERS =
            "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789".toCharArray();
    private final SecureRandom random = new SecureRandom();
    private final LobbyRepository lobbyRepository;

    public LobbyService(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public List<LobbyResponse> findAll() {
        return lobbyRepository.findAll().stream().map(LobbyResponse::from).toList();
    }

    public LobbyResponse findById(UUID id) {
        return LobbyResponse.from(findLobby(id));
    }

    public List<LobbyResponse> findByCode(String lobbyCode) {
        if (!lobbyCode.matches("[A-Za-z0-9]{6}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Lobby code must contain exactly 6 alphanumeric characters");
        }
        return lobbyRepository.findByLobbyCode(lobbyCode.toUpperCase()).stream()
                .map(LobbyResponse::from)
                .toList();
    }

    @Transactional
    public LobbyResponse create(LobbyRequest request) {
        String lobbyCode = generateUniqueActiveLobbyCode();
        Lobby lobby = new Lobby(request.name().trim(), lobbyCode);
        lobby.setStatus(LobbyStatus.OPEN);
        return LobbyResponse.from(lobbyRepository.save(lobby));
    }

    @Transactional
    public LobbyResponse updateStatus(UUID id, LobbyStatusRequest request) {
        Lobby lobby = findLobby(id);
        lobby.setStatus(request.status());
        if (request.status() == LobbyStatus.CLOSED && lobby.getEndedAt() == null) {
            lobby.setEndedAt(java.time.OffsetDateTime.now());
        }
        return LobbyResponse.from(lobbyRepository.save(lobby));
    }

    @Transactional
    public void delete(UUID id) {
        findLobby(id);
        lobbyRepository.deleteById(id);
    }

    private Lobby findLobby(UUID id) {
        return lobbyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found"));
    }

    private String generateUniqueActiveLobbyCode() {
        for (int attempt = 0; attempt < 100; attempt++) {
            StringBuilder code = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                code.append(CODE_CHARACTERS[random.nextInt(CODE_CHARACTERS.length)]);
            }
            String lobbyCode = code.toString();
            if (!lobbyRepository.activeLobbyCodeExists(lobbyCode)) {
                return lobbyCode;
            }
        }
        throw new IllegalStateException("Could not generate a unique active lobby code");
    }
}
