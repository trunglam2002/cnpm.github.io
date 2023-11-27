package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User getUserById(int userId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setUsername(resultSet.getString("username"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(User user) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO user (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateUser(User user) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE user SET username = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setInt(2, user.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExists(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password); // Giả sử bạn đã mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setUsername(resultSet.getString("username"));

                        // Mật khẩu được lấy từ cơ sở dữ liệu và được mã hóa trước khi gán
                        String hashedPassword = resultSet.getString("password");
                        user.setPassword(hashedPassword);

                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            // Xử lý lỗi SQL, có thể thông báo hoặc ghi log
            e.printStackTrace();
        }
        return null;
    }

}
