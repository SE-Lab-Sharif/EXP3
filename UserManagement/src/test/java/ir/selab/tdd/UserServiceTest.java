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

    // Equals and HashCode Tests
    @Test
    public void testEquals_SameObject_ShouldReturnTrue() {
        User user = new User("testuser", "password", "test@example.com");
        assertTrue(user.equals(user));
    }

    @Test
    public void testEquals_NullObject_ShouldReturnFalse() {
        User user = new User("testuser", "password", "test@example.com");
        assertFalse(user.equals(null));
    }

    @Test
    public void testEquals_DifferentClass_ShouldReturnFalse() {
        User user = new User("testuser", "password", "test@example.com");
        assertFalse(user.equals(new Object()));
    }

    @Test
    public void testEquals_EqualObjects_ShouldReturnTrue() {
        User user1 = new User("testuser", "password", "test@example.com");
        User user2 = new User("testuser", "password", "test@example.com");
        assertTrue(user1.equals(user2));
    }

    @Test
    public void testEquals_DifferentUsername_ShouldReturnFalse() {
        User user1 = new User("user1", "password", "test@example.com");
        User user2 = new User("user2", "password", "test@example.com");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void testEquals_DifferentPassword_ShouldReturnFalse() {
        User user1 = new User("testuser", "password1", "test@example.com");
        User user2 = new User("testuser", "password2", "test@example.com");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void testEquals_DifferentEmail_ShouldReturnFalse() {
        User user1 = new User("testuser", "password", "email1@example.com");
        User user2 = new User("testuser", "password", "email2@example.com");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void testHashCode_EqualObjects_ShouldHaveSameHashCode() {
        User user1 = new User("testuser", "password", "test@example.com");
        User user2 = new User("testuser", "password", "test@example.com");
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_ShouldHaveDifferentHashCode() {
        User user1 = new User("user1", "password", "test@example.com");
        User user2 = new User("user2", "password", "test@example.com");
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void getAllUsers_ShouldReturnAllRegisteredUsers() {
        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("admin")));
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("ali")));
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("hasan")));
    }

    @Test
    public void changeUserEmail_UserExistsAndNewEmailIsValid_ShouldReturnTrue() {
        boolean result = userService.changeUserEmail("admin", "admin_new@example.com");

        assertTrue(result);
        User updatedUser = userService.getUserByEmail("admin_new@example.com");
        assertNotNull(updatedUser);
        assertEquals("admin", updatedUser.getUsername());
    }

    @Test
    public void changeUserEmail_UserDoesNotExist_ShouldReturnFalse() {
        boolean result = userService.changeUserEmail("nonexistent", "nonexistent@example.com");

        assertFalse(result);
    }

    @Test
    public void changeUserEmail_NewEmailAlreadyExists_ShouldReturnFalse() {
        userService.registerUser("user1", "password", "existing@example.com");

        boolean result = userService.changeUserEmail("admin", "existing@example.com");

        assertFalse(result);
    }

    @Test
    public void changeUserEmail_NewEmailIsSameAsCurrentEmail_ShouldReturnFalse() {
        boolean result = userService.changeUserEmail("hasan", "hasan@gmail.com");

        assertFalse(result);
    }

    @Test
    public void changeUserEmail_UserExistsAndEmailIsChanged_ShouldAllowLoginWithNewEmail() {
        boolean result = userService.changeUserEmail("hasan", "hasan_new@example.com");

        assertTrue(result);
        assertTrue(userService.loginWithEmail("hasan_new@example.com", "hasan123@"));
        assertFalse(userService.loginWithEmail("hasan@gmail.com", "hasan123@"));
    }

    @Test
    public void registerUser_WithInvalidInputs_ShouldReturnFalse() {
        assertFalse(userService.registerUser(null, "password", "email@example.com"));
        assertFalse(userService.registerUser("", "password", "email@example.com"));
        assertFalse(userService.registerUser("   ", "password", "email@example.com"));
        assertFalse(userService.registerUser("username", null, "email@example.com"));
        assertFalse(userService.registerUser("username", "password", null));

        assertFalse(userService.registerUser("username", "password", ""));
        assertFalse(userService.registerUser("username", "password", "   "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithDuplicateUsernames_ShouldThrowException() {
        List<User> users = List.of(
                new User("user1", "password1"),
                new User("user1", "password2") // Duplicate username
        );

        new UserRepository(users);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithDuplicateEmails_ShouldThrowException() {
        List<User> users = List.of(
                new User("user1", "password1", "email@example.com"),
                new User("user2", "password2", "email@example.com") // Duplicate email
        );

        new UserRepository(users);
    }
}
