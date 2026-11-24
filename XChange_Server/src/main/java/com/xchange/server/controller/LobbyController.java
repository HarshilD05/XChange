package com.xchange.server.controller;

import com.xchange.server.dto.LobbyRequest;
import com.xchange.server.dto.LobbyResponse;
import com.xchange.server.dto.LobbyStatusRequest;
import com.xchange.server.service.LobbyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lobbies")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping
    public List<LobbyResponse> getLobbies() {
        return lobbyService.findAll();
    }

    @GetMapping("/{id}")
    public LobbyResponse getLobby(@PathVariable UUID id) {
        return lobbyService.findById(id);
    }

    @GetMapping("/code/{lobbyCode}")
    public List<LobbyResponse> getLobbiesByCode(@PathVariable String lobbyCode) {
        return lobbyService.findByCode(lobbyCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LobbyResponse createLobby(@Valid @RequestBody LobbyRequest request) {
        return lobbyService.create(request);
    }

    @PatchMapping("/{id}/status")
    public LobbyResponse updateLobbyStatus(
            @PathVariable UUID id,
            @Valid @RequestBody LobbyStatusRequest request
    ) {
        return lobbyService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable UUID id) {
        lobbyService.delete(id);
    }
}
