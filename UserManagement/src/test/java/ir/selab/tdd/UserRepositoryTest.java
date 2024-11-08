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
                new User("mohammad", "123asd", "moh@gmail.com"));
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
}
