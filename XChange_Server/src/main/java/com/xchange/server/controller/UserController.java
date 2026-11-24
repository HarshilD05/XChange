package com.xchange.server.controller;

import com.xchange.server.dto.CreateUserRequest;
import com.xchange.server.dto.ChangePasswordRequest;
import com.xchange.server.dto.AccountStatusRequest;
import com.xchange.server.dto.PlayerResponse;
import com.xchange.server.dto.PlayerStatusRequest;
import com.xchange.server.dto.UserResponse;
import com.xchange.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/users/{username}")
    public UserResponse getUser(@PathVariable String username) {
        return userService.findUser(username);
    }

    @PostMapping("/users/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createAdmin(@Valid @RequestBody CreateUserRequest request) {
        return userService.createAdmin(request);
    }

    @PostMapping("/users/player")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(@Valid @RequestBody CreateUserRequest request) {
        return userService.createPlayer(request);
    }

    @PatchMapping("/users/{username}/password")
    public UserResponse changePassword(
            @PathVariable String username,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        return userService.changePassword(username, request);
    }

    @DeleteMapping("/users/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    @GetMapping("/players")
    public List<PlayerResponse> getPlayers() {
        return userService.findAllPlayers();
    }

    @GetMapping("/players/{username}")
    public PlayerResponse getPlayer(@PathVariable String username) {
        return userService.findPlayer(username);
    }

    @PatchMapping("/players/{username}/status")
    public PlayerResponse updatePlayerStatus(
            @PathVariable String username,
            @Valid @RequestBody PlayerStatusRequest request
    ) {
        return userService.updatePlayerStatus(username, request.playerStatus());
    }

    @PatchMapping("/players/{username}/account-status")
    public PlayerResponse updatePlayerAccountStatus(
            @PathVariable String username,
            @Valid @RequestBody AccountStatusRequest request
    ) {
        return userService.setPlayerAccountStatus(username, request.accountStatus());
    }
}
