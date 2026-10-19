package com.cosmic.XChange.lobby;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/lobbies")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping
    public ResponseEntity<Lobby> create(@RequestBody(required = false) Lobby lobby) {
        if (lobby == null) {
            lobby = new Lobby(LobbyStatus.OPEN);
        }
        Lobby created = lobbyService.create(lobby);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Lobby>> getAll() {
        return ResponseEntity.ok(lobbyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lobby> getById(@PathVariable UUID id) {
        return lobbyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        if (request == null || !request.containsKey("status")) {
            return ResponseEntity.badRequest().build();
        }
        String statusStr = request.get("status");
        try {
            LobbyStatus status = LobbyStatus.valueOf(statusStr.toUpperCase());
            lobbyService.updateStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        lobbyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
