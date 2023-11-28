package dao;

import model.TransactionHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryDAO {

    public List<TransactionHistory> getAllTransactionHistory() {
        List<TransactionHistory> transactionHistoryList = new ArrayList<>();
        String query = "SELECT * FROM transaction_history";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                TransactionHistory transactionHistory = createTransactionHistoryFromResultSet(resultSet);
                transactionHistoryList.add(transactionHistory);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return transactionHistoryList;
    }

    public List<TransactionHistory> getTransactionHistoryByStock(int stockId) {
        List<TransactionHistory> transactionHistories = new ArrayList<>();
        String query = "SELECT * FROM transaction_history WHERE stock_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TransactionHistory transactionHistory = createTransactionHistoryFromResultSet(resultSet);
                    transactionHistories.add(transactionHistory);
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return transactionHistories;
    }

    public void deleteTransactionHistory(int transactionHistoryId) {
        String query = "DELETE FROM transaction_history WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, transactionHistoryId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void insertTransactionHistory(TransactionHistory transactionHistory) {
        String query = "INSERT INTO transaction_history (user_id, stock_id, stock_transaction_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            setTransactionHistoryParameters(preparedStatement, transactionHistory);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transactionHistory.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private TransactionHistory createTransactionHistoryFromResultSet(ResultSet resultSet) throws SQLException {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setId(resultSet.getInt("id"));
        transactionHistory.setUserId(resultSet.getInt("user_id"));
        transactionHistory.setStockId(resultSet.getInt("stock_id"));
        transactionHistory.setStockTransactionId(resultSet.getInt("stock_transaction_id"));
        return transactionHistory;
    }

    private void setTransactionHistoryParameters(PreparedStatement preparedStatement, TransactionHistory transactionHistory) throws SQLException {
        preparedStatement.setInt(1, transactionHistory.getUserId());
        preparedStatement.setInt(2, transactionHistory.getStockId());
        preparedStatement.setInt(3, transactionHistory.getStockTransactionId());
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Database error", e);
    }
}
