package com.cosmic.XChange.lobby;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LobbyService {

    private final LobbyRepository lobbyRepository;

    public LobbyService(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public Lobby create(Lobby lobby) {
        return lobbyRepository.insert(lobby);
    }

    public Lobby createLobby(Lobby lobby) {
        return create(lobby);
    }

    public List<Lobby> getAll() {
        return lobbyRepository.findAll();
    }

    public List<Lobby> getAllLobbies() {
        return getAll();
    }

    public Optional<Lobby> getById(UUID id) {
        return lobbyRepository.findById(id);
    }

    public Optional<Lobby> getLobbyById(UUID id) {
        return getById(id);
    }

    public void updateStatus(UUID id, LobbyStatus status) {
        lobbyRepository.updateStatus(id, status);
    }

    public void updateLobbyStatus(UUID id, LobbyStatus status) {
        updateStatus(id, status);
    }

    public void delete(UUID id) {
        lobbyRepository.deleteById(id);
    }

    public void deleteLobby(UUID id) {
        delete(id);
    }

    public void deleteById(UUID id) {
        delete(id);
    }
}
