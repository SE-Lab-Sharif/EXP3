package ir.selab.tdd;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import ir.selab.tdd.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService;

    @Before
    public void setUp() {
        UserRepository userRepository = new UserRepository(List.of());
        userService = new UserService(userRepository);
        userService.registerUser("admin", "1234");
        userService.registerUser("ali", "qwert");
        userService.registerUser("hasan", "hasan123@", "hasan@gmail.com");
    }

    @Test
    public void createNewValidUser_ShouldSuccess() {
        boolean result = userService.registerUser("reza", "123abc");
        assertTrue(result);
    }

    @Test
    public void createNewDuplicateUser_ShouldFail() {
        boolean result = userService.registerUser("ali", "123abc");
        assertFalse(result);
    }

    @Test
    public void registerUser_WithNullUsername_ShouldReturnFalse() {
        assertFalse(userService.registerUser(null, "password"));
    }

    @Test
    public void registerUser_WithEmptyUsername_ShouldReturnFalse() {
        assertFalse(userService.registerUser("", "password"));
    }

    @Test
    public void registerUser_WithSpacesOnlyUsername_ShouldReturnFalse() {
        assertFalse(userService.registerUser("   ", "password"));
    }

    @Test
    public void registerUser_WithNullPassword_ShouldReturnFalse() {
        assertFalse(userService.registerUser("username", null));
    }

    @Test
    public void registerUser_WithValidUsernamePasswordAndEmail_ShouldReturnTrue() {
        assertTrue(userService.registerUser("newuser", "password", "newuser@example.com"));
    }

    @Test
    public void registerUser_WithNullEmail_ShouldReturnFalse() {
        assertFalse(userService.registerUser("newuser", "password", null));
    }

    @Test
    public void registerUser_WithEmptyEmail_ShouldReturnFalse() {
        assertFalse(userService.registerUser("newuser", "password", ""));
    }

    @Test
    public void registerUser_WithSpacesOnlyEmail_ShouldReturnFalse() {
        assertFalse(userService.registerUser("newuser", "password", "   "));
    }

    @Test
    public void loginWithValidUsernameAndPassword_ShouldSuccess() {
        assertTrue(userService.loginWithUsername("admin", "1234"));
    }

    @Test
    public void loginWithValidUsernameAndInvalidPassword_ShouldFail() {
        assertFalse(userService.loginWithUsername("admin", "abcd"));
    }

    @Test
    public void loginWithInvalidUsername_ShouldFail() {
        assertFalse(userService.loginWithUsername("ahmad", "abcd"));
    }

    @Test
    public void loginWithNullUsername_ShouldFail() {
        assertFalse(userService.loginWithUsername(null, "abcd"));
    }

    @Test
    public void loginWithValidEmailAndPassword_ShouldSuccess() {
        assertTrue(userService.loginWithEmail("hasan@gmail.com", "hasan123@"));
    }

    @Test
    public void loginWithInvalidEmail_ShouldFail() {
        assertFalse(userService.loginWithEmail("invalid@example.com", "password"));
    }

    @Test
    public void loginWithNullEmail_ShouldFail() {
        assertFalse(userService.loginWithEmail(null, "password"));
    }

    @Test
    public void removeUser_ShouldSuccess() {
        assertTrue(userService.removeUser("ali"));
    }

    @Test
    public void removeUser_NonExistingUser_ShouldFail() {
        assertFalse(userService.removeUser("nonexistent"));
    }

    @Test
    public void afterRemovingAUser_UserCountShouldDecrease() {
        int initialCount = userService.getUserCount();
        userService.removeUser("ali");
        assertEquals(initialCount - 1, userService.getUserCount());
    }

    @Test
    public void changeUserEmail_ShouldSucceedForNewEmail() {
        assertTrue(userService.changeUserEmail("admin", "admin@corp.co"));
        assertNotNull(userService.getUserByEmail("admin@corp.co"));
    }

    @Test
    public void changeUserEmail_ShouldFailForDuplicateEmail() {
        assertFalse(userService.changeUserEmail("admin", "hasan@gmail.com"));
    }

    @Test
    public void changeUserEmail_ShouldAllowLoginWithNewEmail() {
        userService.changeUserEmail("hasan", "hasan@yahoo.com");
        assertTrue(userService.loginWithEmail("hasan@yahoo.com", "hasan123@"));
    }

    @Test
    public void testGetUserCount() {
        assertEquals(3, userService.getUserCount());
    }

    @Test
    public void testGetUserByUsername() {
        assertNotNull(userService.getUserByUsername("admin"));
    }

    @Test
    public void testGetUserByEmail() {
        assertNotNull(userService.getUserByEmail("hasan@gmail.com"));
    }

    @Test
    public void testGetUserByInvalidEmail_ShouldReturnNull() {
        assertNull(userService.getUserByEmail("invalid@example.com"));
    }
}
