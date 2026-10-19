package com.cosmic.XChange.player;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
public class PlayerService {
    
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> getPlayerById(java.util.UUID id) {
        return playerRepository.findById(id);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public void createPlayer(Player player) {
        playerRepository.insert(player);
    }

    public void updatePlayerStatus(java.util.UUID id, PlayerStatus status) {
        playerRepository.updatePlayerStatus(id, status);
    }

    public void deletePlayer(java.util.UUID id) {
        playerRepository.deleteById(id);
    }
}
