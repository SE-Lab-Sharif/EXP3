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
    public void createNewValidUser__ShouldSuccess() {
        String username = "reza";
        String password = "123abc";
        boolean b = userService.registerUser(username, password);
        assertTrue(b);
    }

    @Test
    public void createNewDuplicateUser__ShouldFail() {
        String username = "ali";
        String password = "123abc";
        boolean b = userService.registerUser(username, password);
        assertFalse(b);
    }

    @Test
    public void loginWithValidUsernameAndPassword__ShouldSuccess() {
        boolean login = userService.loginWithUsername("admin", "1234");
        assertTrue(login);
    }

    @Test
    public void loginWithValidUsernameAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithUsername("admin", "abcd");
        assertFalse(login);
    }

    @Test
    public void loginWithInvalidUsernameAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithUsername("ahmad", "abcd");
        assertFalse(login);
    }

    @Test
    public void loginWithValidEmailAndPassword__ShouldSuccess() {
        boolean login = userService.loginWithEmail("hasan@gmail.com", "hasan123@");
        assertTrue(login);
    }

    @Test
    public void loginWithValidEmailAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithEmail("hasan@gmail.com", "abcd");
        assertFalse(login);
    }

    @Test
    public void loginWithInvalidEmailAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithEmail("admin@admin.edu", "abcd");
        assertFalse(login);
    }

    @Test
    public void removeUser__ShouldSuccess() {
        boolean b = userService.removeUser("ali");
        assertTrue(b);
    }

    @Test
    public void removeUserShouldDeleteUserFromRepository() {
        assertNull(userService.getUserByUsername("ali"));
    }

    @Test
    public void removeUserWithInvalidUsername__ShouldFail() {
        boolean b = userService.removeUser("ahmad");
        assertFalse(b);
    }

    @Test
    public void afterRemovingAUser__UserCountShouldDecrease() {
        assertEquals(3, userService.getUserCount());
    }

    @Test
    public void changeUserEmail__ShouldCreateNewEmailAddress__IfPrevEmailDoesNotExist() {
        boolean b = userService.changeUserEmail("admin", "admin@corp.co");
        assertTrue(b);
    }

    @Test
    public void changeUserEmail__CheckEmailIsCreated() {
        assertNotNull(userService.getUserByEmail("admin@corp.co"));
    }

    @Test
    public void changeUserEmail__ShouldChangeEmail__IfPrevEmailExists() {
        boolean b = userService.changeUserEmail("hasan", "hasan@yahoo.com");
        assertTrue(b);
    }

    @Test
    public void changeUserEmail__CheckEmailIsChanged() {
        assertNull(userService.getUserByEmail("hasan@gmail.com"));
        assertNotNull(userService.getUserByEmail("hasan@yahoo.com"));
    }

}
