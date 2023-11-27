package controller;

import controller.UserController;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    // Mock UserDAO for testing
    private static class MockUserDAO extends UserDAO {
        private User storedUser;

        @Override
        public User getUserById(int userId) {
            return null;
        }

        @Override
        public void addUser(User user) {
            storedUser = user;
        }

        @Override
        public void updateUser(User user) {
            // Not implemented for this test
        }

        @Override
        public void deleteUser(int userId) {
            // Not implemented for this test
        }

        @Override
        public boolean isUserExists(String username) {
            return storedUser != null && storedUser.getUsername().equals(username);
        }

        @Override
        public boolean loginUser(String username, String password) {
            return storedUser != null && storedUser.getUsername().equals(username) && storedUser.getPassword().equals(password);
        }

        @Override
        public User getUserByUsername(String username) {
            return storedUser;
        }
    }

    @Test
    public void testRegisterUser() {
        UserController userController = new UserController(new MockUserDAO());

        // Test registration with a new username
        assertTrue(userController.registerUser("Client1", "1234"));

        // Test registration with an existing username
        assertFalse(userController.registerUser("Client1", "1234"));

        // Ensure the password is hashed and stored correctly
        User storedUser = ((MockUserDAO) userController.getUserDAO()).storedUser;
        assertNotNull(storedUser);
        assertNotEquals("1234", storedUser.getPassword());
    }

    @Test
    public void testLoginUser() {
        UserController userController = new UserController(new MockUserDAO());

        // Register a user
        assertTrue(userController.registerUser("Client1", "1234"));

        // Test login with correct credentials
        assertTrue(userController.loginUser("CLient1", "1234"));

        // Test login with incorrect password
        assertFalse(userController.loginUser("CLient1", "123"));

        // Test login with incorrect username
        assertFalse(userController.loginUser("sb", "1234"));
    }
}
