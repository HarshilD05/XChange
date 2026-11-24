package com.xchange.server.service;

import com.xchange.server.dto.ChangePasswordRequest;
import com.xchange.server.dto.CreateUserRequest;
import com.xchange.server.dto.PlayerResponse;
import com.xchange.server.dto.UserResponse;
import com.xchange.server.entity.AccountStatus;
import com.xchange.server.entity.Player;
import com.xchange.server.entity.PlayerStatus;
import com.xchange.server.entity.User;
import com.xchange.server.entity.UserType;
import com.xchange.server.repository.PlayerRepository;
import com.xchange.server.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PlayerRepository playerRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public UserResponse findUser(String username) {
        return UserResponse.from(findUserEntity(username));
    }

    public List<PlayerResponse> findAllPlayers() {
        return playerRepository.findAll().stream().map(PlayerResponse::from).toList();
    }

    public PlayerResponse findPlayer(String username) {
        Player player = findPlayerEntity(username);
        return PlayerResponse.from(player);
    }

    @Transactional
    public UserResponse createAdmin(CreateUserRequest request) {
        return createUser(request, UserType.ADMIN);
    }

    @Transactional
    public PlayerResponse createPlayer(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                UserType.PLAYER
        );
        userRepository.save(user);
        return PlayerResponse.from(playerRepository.save(new Player(user)));
    }

    @Transactional
    public UserResponse changePassword(String username, ChangePasswordRequest request) {
        User user = findUserEntity(username);
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public PlayerResponse setPlayerAccountStatus(String username, AccountStatus accountStatus) {
        Player player = findPlayerEntity(username);
        player.getUser().setAccountStatus(accountStatus);
        userRepository.save(player.getUser());
        return PlayerResponse.from(findPlayerEntity(username));
    }

    @Transactional
    public PlayerResponse updatePlayerStatus(String username, PlayerStatus status) {
        Player player = findPlayerEntity(username);
        player.setPlayerStatus(status);
        return PlayerResponse.from(playerRepository.save(player));
    }

    @Transactional
    public void deleteUser(String username) {
        User user = findUserEntity(username);
        userRepository.deleteById(user.getId());
    }

    private UserResponse createUser(CreateUserRequest request, UserType userType) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                userType
        );
        return UserResponse.from(userRepository.save(user));
    }

    private User findUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Player findPlayerEntity(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }
}
