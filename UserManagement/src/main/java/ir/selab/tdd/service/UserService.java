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
        // TODO: implement login with email. return true if email and password are valid.
        User userByEmail = repository.getUserByEmail(email);
        return userByEmail != null && userByEmail.getPassword().equals(password);
    }

    public boolean registerUser(String username, String password) {
        User user = new User(username, password);
        return repository.addUser(user);
    }

    public boolean registerUser(String username, String password, String email) {
        User user = new User(username, password);
        user.setEmail(email);
        return repository.addUser(user);
    }

    public boolean removeUser(String username) {
        // TODO: implement: remove user from repository. Return true if successful, otherwise false.
        return repository.removeUser(username);
    }

    public List<User> getAllUsers() {
        // TODO: implement
        return repository.getAllUsers();
    }

    public boolean changeUserEmail(String username, String newEmail) {
        // TODO: implement (if user exists and user's email is valid, then change email)
        // TODO: after changing user's email, user must be able to login with new email.
        return false;
    }

    public int getUserCount() {
        // TODO: Should return this.repository.getUserCount();
        return repository.getUserCount();
    }

    public User getUserByUsername(String username) {
        // TODO: Should return this.repository.getUserByUsername(username);
        return repository.getUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        // TODO: Should return this.repository.getUserByEmail(email);
        return repository.getUserByEmail(email);
    }

}
