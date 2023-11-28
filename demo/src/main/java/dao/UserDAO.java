package dao;

import model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {

    public void addUser(User user) {
        String query = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateUser(User user) {
        String query = "UPDATE user SET username = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteUser(int userId) {
        String query = "DELETE FROM user WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public boolean isUserExists(String username) {
        String query = "SELECT * FROM user WHERE username = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return false;
    }

    public Optional<User> loginUser(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createUserFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return Optional.empty();
    }

    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createUserFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return Optional.empty();
    }

    public void updateUserBalance(int userId, BigDecimal newBalance) {
        String query = "UPDATE user SET balance = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public BigDecimal getUserBalance(int userId) {
        String query = "SELECT balance FROM user WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("balance");
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return BigDecimal.ZERO; // Trả về giá trị mặc định nếu không tìm thấy số dư
    }

    public Optional<User> getUserById(int userId) {
        String query = "SELECT * FROM user WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createUserFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return Optional.empty();
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));

        // Kiểm tra xem cột "password" có tồn tại hay không
        if (resultSet.getString("password") != null) {
            // Mật khẩu được lấy từ cơ sở dữ liệu và được mã hóa trước khi gán
            String hashedPassword = resultSet.getString("password");
            user.setPassword(hashedPassword);
        } else {
            // Xử lý nếu cột "password" không tồn tại
            // Có thể thông báo hoặc xử lý theo cách khác tùy vào yêu cầu
            System.out.println("Column 'password' does not exist for user " + user.getUsername());
            return null;
        }

        return user;
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Database error", e);
    }
}
