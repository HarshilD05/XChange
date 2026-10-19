package com.cosmic.XChange.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cosmic.XChange.user.dto.LoginReq;
import com.cosmic.XChange.user.dto.LoginResp;
import com.cosmic.XChange.user.dto.UserResp;
import com.cosmic.XChange.user.dto.UpdatePasswordRequest;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResp> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        UserResp userResp = UserResp.fromEntity(createdUser);
        return new ResponseEntity<>(userResp, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody LoginReq request) {
        if (request == null || request.username() == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResp(false, "Invalid username or password"));
        }

        Optional<User> userOpt = userService.login(request.username(), request.password());
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(new LoginResp(true, "Login successful", UserResp.fromEntity(userOpt.get())));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResp(false, "Invalid username or password"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable AccStatus status) {
        return ResponseEntity.ok(userService.getUsersByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<User>> getUsersByType(@PathVariable UserType type) {
        return ResponseEntity.ok(userService.getUsersByType(type));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable UUID id, @RequestBody UpdatePasswordRequest request) {
        if (request == null) return ResponseEntity.badRequest().build();
        String newPassword = request.newPassword();
        if (newPassword == null || newPassword.isBlank()) return ResponseEntity.badRequest().build();

        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        String currentPassword = request.currentPassword();
        if (currentPassword != null && !currentPassword.isBlank()) {
            if (!currentPassword.equals(userOpt.get().getPasswordHash())) {
                return ResponseEntity.badRequest().build();
            }
        }

        userService.updateUserPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        String statusStr = request.get("status");
        if (statusStr == null) return ResponseEntity.badRequest().build();
        try {
            AccStatus status = AccStatus.valueOf(statusStr.toUpperCase());
            userService.updateUserStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
