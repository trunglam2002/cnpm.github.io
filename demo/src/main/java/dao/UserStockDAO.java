package dao;

import model.UserStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserStockDAO {

    public void addUserStock(UserStock userStock) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO user_stock (user_id, symbol, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userStock.getUserId());
                preparedStatement.setString(2, userStock.getSymbol());
                preparedStatement.setInt(3, userStock.getQuantity());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserStock> getUserStocksByUser(int userId) {
        List<UserStock> userStocks = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user_stock WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        UserStock userStock = new UserStock();
                        userStock.setId(resultSet.getInt("id"));
                        userStock.setUserId(resultSet.getInt("user_id"));
                        userStock.setSymbol(resultSet.getString("symbol"));
                        userStock.setQuantity(resultSet.getInt("quantity"));
                        userStocks.add(userStock);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userStocks;
    }

    public void updateUserStock(UserStock userStock) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE user_stock SET quantity = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userStock.getQuantity());
                preparedStatement.setInt(2, userStock.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserStock(int userStockId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM user_stock WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userStockId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
