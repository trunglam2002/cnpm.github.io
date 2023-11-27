package controller;

import dao.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public boolean registerUser(String username, String password) {
        // Kiểm tra xem người dùng đã tồn tại chưa
        if (userDAO.isUserExists(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return false;
        }

        // Nếu chưa tồn tại, thêm người dùng mới vào cơ sở dữ liệu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        userDAO.addUser(newUser);
        System.out.println("Registration successful. You can now log in.");
        return true;
    }

    public boolean loginUser(String username, String password) {
        // Lấy thông tin người dùng từ cơ sở dữ liệu
        User user = userDAO.getUserByUsername(username);

        // Kiểm tra xem người dùng có tồn tại và mật khẩu đúng không
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            System.out.println("Login successful. Welcome, " + username + "!");
            return true;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }
    }
}