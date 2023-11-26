package dao;

import model.TransactionHistory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public List<TransactionHistory> getTransactionHistoryByUserId(int userId) {
        List<TransactionHistory> transactionHistoryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM transaction_history WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        TransactionHistory transactionHistory = new TransactionHistory();
                        transactionHistory.setId(resultSet.getInt("id"));
                        transactionHistory.setUserId(resultSet.getInt("user_id"));
                        transactionHistory.setStockId(resultSet.getInt("stock_id"));
                        transactionHistory.setStockTransactionId(resultSet.getInt("stock_transaction_id"));
                        transactionHistoryList.add(transactionHistory);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistoryList;
    }

    public TransactionHistory getTransactionHistoryById(int transactionHistoryId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM transaction_history WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, transactionHistoryId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        TransactionHistory transactionHistory = new TransactionHistory();
                        transactionHistory.setId(resultSet.getInt("id"));
                        transactionHistory.setUserId(resultSet.getInt("user_id"));
                        transactionHistory.setStockId(resultSet.getInt("stock_id"));
                        transactionHistory.setStockTransactionId(resultSet.getInt("stock_transaction_id"));
                        return transactionHistory;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TransactionHistory> getAllTransactionHistory() {
        List<TransactionHistory> transactionHistoryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM transaction_history";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        TransactionHistory transactionHistory = new TransactionHistory();
                        transactionHistory.setId(resultSet.getInt("id"));
                        transactionHistory.setUserId(resultSet.getInt("user_id"));
                        transactionHistory.setStockId(resultSet.getInt("stock_id"));
                        transactionHistory.setStockTransactionId(resultSet.getInt("stock_transaction_id"));
                        transactionHistoryList.add(transactionHistory);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistoryList;
    }

    public List<TransactionHistory> getTransactionHistoryByUser(int userId) {
        List<TransactionHistory> transactionHistories = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM transaction_history WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        TransactionHistory transactionHistory = new TransactionHistory();
                        transactionHistory.setId(resultSet.getInt("id"));
                        transactionHistory.setUserId(resultSet.getInt("user_id"));
                        transactionHistory.setStockId(resultSet.getInt("stock_id"));
                        transactionHistory.setStockTransactionId(resultSet.getInt("stock_transaction_id"));
                        transactionHistories.add(transactionHistory);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistories;
    }

    public List<TransactionHistory> getTransactionHistoryByStock(int stockId) {
        List<TransactionHistory> transactionHistories = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM transaction_history WHERE stock_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        TransactionHistory transactionHistory = new TransactionHistory();
                        transactionHistory.setId(resultSet.getInt("id"));
                        transactionHistory.setUserId(resultSet.getInt("user_id"));
                        transactionHistory.setStockId(resultSet.getInt("stock_id"));
                        transactionHistory.setStockTransactionId(resultSet.getInt("stock_transaction_id"));
                        transactionHistories.add(transactionHistory);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistories;
    }

    public void deleteTransactionHistory(int transactionHistoryId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "DELETE FROM transaction_history WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, transactionHistoryId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add other methods for CRUD operations

    public void insertTransactionHistory(TransactionHistory transactionHistory) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO transaction_history (user_id, stock_id, stock_transaction_id) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, transactionHistory.getUserId());
                preparedStatement.setInt(2, transactionHistory.getStockId());
                preparedStatement.setInt(3, transactionHistory.getStockTransactionId());
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transactionHistory.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
