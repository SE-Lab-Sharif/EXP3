package ir.selab.tdd;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private UserRepository repository;

    @Before
    public void setUp() {
        List<User> userList = Arrays.asList(
                new User("admin", "1234"),
                new User("ali", "qwert"),
                new User("hasan", "hasan123@", "hasan@gmail.com")
        );
        repository = new UserRepository(userList);
    }


    @Test
    public void getContainingUser__ShouldReturn() {
        User ali = repository.getUserByUsername("ali");
        assertNotNull(ali);
        assertEquals("ali", ali.getUsername());
        assertEquals("qwert", ali.getPassword());
    }

    @Test
    public void getNotContainingUser__ShouldReturnNull() {
        User user = repository.getUserByUsername("reza");
        assertNull(user);
    }

    @Test
    public void createRepositoryWithDuplicateUsers__ShouldThrowException() {
        User user1 = new User("ali", "1234");
        User user2 = new User("ali", "4567");
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRepository(List.of(user1, user2));
        });
    }

    @Test
    public void addNewUser__ShouldIncreaseUserCount() {
        int oldUserCount = repository.getUserCount();

        // Given
        String username = "reza";
        String password = "123abc";
        String email = "reza@sharif.edu";
        User newUser = new User(username, password);

        // When
        repository.addUser(newUser);

        // Then
        assertEquals(oldUserCount + 1, repository.getUserCount());
    }

    @Test
    public void getUserByEmail__ShouldSuccess() {
        repository.addUser(new User("reza", "123abc", "reza@sharif.edu"));
        assertNotNull(repository.getUserByEmail("reza@sharif.edu"));
    }

    @Test
    public void getUserByEmail__ShouldFail() {
        assertNull(repository.getUserByEmail("hasanGholi@sharif.edu"));
    }

    @Test
    public void addUserWithDuplicateEmail__ShouldFail() {
        repository.addUser(new User("reza", "123abcd", "reza@sharif.edu"));
        User user1 = new User("hasanGholi", "gholi1@2", "reza@sharif.edu");
        int prevUserCount = repository.getUserCount();
        boolean b = repository.addUser(user1);
        assertFalse(b);
        assertEquals(repository.getUserByEmail("reza@sharif.edu").getUsername(), "reza");
        assertEquals(repository.getUserCount(), prevUserCount);
    }

    @Test
    public void removeUser__ShouldSuccess() {
        int prevUserCount = repository.getUserCount();
        boolean b = repository.removeUser("ali");
        assertTrue(b);
        assertEquals(repository.getUserCount(), prevUserCount - 1);
        assertNull(repository.getUserByUsername("ali"));
    }

    @Test
    public void removeUser__ShouldFail() {
        int prevUserCount = repository.getUserCount();
        boolean b = repository.removeUser("hasanGholi");
        assertFalse(b);
        assertEquals(repository.getUserCount(), prevUserCount);
    }

    // Equals Tests
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

    // HashCode Tests
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

    @Test
    public void testGetUserByUsername_ShouldReturnUser() {
        assertNotNull(repository.getUserByUsername("admin"));
    }

    @Test
    public void testGetUserByUsername_ShouldReturnNullForInvalidUser() {
        assertNull(repository.getUserByUsername("nonexistent"));
    }

    @Test
    public void testGetUserByEmail_ShouldReturnUser() {
        User user = repository.getUserByEmail("hasan@gmail.com");
        assertNotNull(user);
        assertEquals("hasan", user.getUsername());
    }

    @Test
    public void testGetUserByEmail_ShouldReturnNullForInvalidEmail() {
        User user = repository.getUserByEmail("invalid@example.com");
        assertNull(user);
    }

    @Test
    public void getAllUsers_ShouldReturnAllRegisteredUsers() {
        List<User> users = repository.getAllUsers();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("admin")));
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("ali")));
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("hasan")));
    }

    @Test
    public void removeUser_ShouldSuccess() {
        assertTrue(repository.removeUser("ali"));
        assertNull(repository.getUserByUsername("ali"));
    }

    @Test
    public void removeUser_NonExistingUser_ShouldFail() {
        assertFalse(repository.removeUser("nonexistent"));
    }

    @Test
    public void afterRemovingAUser_UserCountShouldDecrease() {
        int initialCount = repository.getUserCount();
        repository.removeUser("ali");
        assertEquals(initialCount - 1, repository.getUserCount());
    }
}