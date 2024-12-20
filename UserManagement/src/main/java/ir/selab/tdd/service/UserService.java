package ir.selab.tdd.service;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public boolean loginWithUsername(String username, String password) {
        User userByUsername = repository.getUserByUsername(username);
        if (userByUsername == null) {
            return false;
        }
        return userByUsername.getPassword().equals(password);
    }

    public boolean loginWithEmail(String email, String password) {
        User userByEmail = repository.getUserByEmail(email);
        return userByEmail != null && userByEmail.getPassword().equals(password);
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) {
            return false;
        }
        User user = new User(username, password);
        return repository.addUser(user);
    }

    public boolean registerUser(String username, String password, String email) {
        if (username == null || username.trim().isEmpty() || password == null || email == null || email.trim().isEmpty()) {
            return false;
        }
        User user = new User(username, password);
        user.setEmail(email);
        return repository.addUser(user);
    }

    public boolean removeUser(String username) {
        return repository.removeUser(username);
    }

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public boolean changeUserEmail(String username, String newEmail) {
        // TODO: implement (if user exists and user's email is valid, then change email)
        // TODO: after changing user's email, user must be able to login with new email.
        User user = repository.getUserByUsername(username);
        if (user == null || repository.getUserByEmail(newEmail) != null) {
            return false;
        }
        repository.removeUser(username);
        user.setEmail(newEmail);
        repository.addUser(user);
        return true;
    }

    public int getUserCount() {
        return repository.getUserCount();
    }

    public User getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

}
