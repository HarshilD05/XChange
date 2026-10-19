package com.cosmic.XChange.user;

import org.springframework.stereotype.Service;

import com.cosmic.XChange.player.Player;
import com.cosmic.XChange.player.PlayerRepository;
import com.cosmic.XChange.player.PlayerStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service 
public class UserService {    
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    public UserService(UserRepository userRepository, PlayerRepository playerRepository) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findByID(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username)
                .filter(user -> password.equals(user.getPasswordHash()));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByStatus(AccStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> getUsersByType(UserType type) {
        return userRepository.findByType(type);
    }

    public User createUser(User user) {
        User createdUser = userRepository.insert(user);
        // Also Register in Player Table
        if (createdUser.getUserType() == UserType.PLAYER) {
            playerRepository.insert(
                new Player(createdUser.getId(), PlayerStatus.OFFLINE)
            );
        }
        return createdUser;
    }

    public void updateUserPassword(UUID id, String passwordHash) {
        userRepository.updatePasswordHash(id, passwordHash);
    }

    public void updateUserStatus(UUID id, AccStatus status) {
        userRepository.updateAccStatus(id, status);
    }

    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}
