package dao;

import model.StockTransaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockTransactionDAO {

    public void addStockTransaction(StockTransaction stockTransaction) {
        String query = "INSERT INTO stock_transaction (stock_id, user_id, transaction_type, transaction_date, quantity, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setStockTransactionParameters(preparedStatement, stockTransaction);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public List<StockTransaction> getAllStockTransactions() {
        List<StockTransaction> stockTransactions = new ArrayList<>();
        String query = "SELECT * FROM stock_transaction";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                StockTransaction stockTransaction = createStockTransactionFromResultSet(resultSet);
                stockTransactions.add(stockTransaction);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return stockTransactions;
    }

    public void updateStockTransaction(StockTransaction stockTransaction) {
        String query = "UPDATE stock_transaction SET stock_id = ?, user_id = ?, transaction_type = ?, " +
                "transaction_date = ?, quantity = ?, price = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setStockTransactionParameters(preparedStatement, stockTransaction);
            preparedStatement.setInt(7, stockTransaction.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public List<StockTransaction> getStockTransactionsByUser(int userId) {
        List<StockTransaction> stockTransactions = new ArrayList<>();
        String query = "SELECT * FROM stock_transaction WHERE user_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    StockTransaction stockTransaction = createStockTransactionFromResultSet(resultSet);
                    stockTransactions.add(stockTransaction);
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return stockTransactions;
    }

    public List<StockTransaction> getStockTransactionsByStock(int stockId) {
        List<StockTransaction> stockTransactions = new ArrayList<>();
        String query = "SELECT * FROM stock_transaction WHERE stock_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    StockTransaction stockTransaction = createStockTransactionFromResultSet(resultSet);
                    stockTransactions.add(stockTransaction);
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return stockTransactions;
    }

    public void deleteStockTransaction(int stockTransactionId) {
        String query = "DELETE FROM stock_transaction WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockTransactionId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private StockTransaction createStockTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setId(resultSet.getInt("id"));
        stockTransaction.setStockId(resultSet.getInt("stock_id"));
        stockTransaction.setUserId(resultSet.getInt("user_id"));
        stockTransaction.setTransactionType(StockTransaction.TransactionType.valueOf(resultSet.getString("transaction_type")));
        stockTransaction.setTransactionDate(resultSet.getDate("transaction_date").toLocalDate());
        stockTransaction.setQuantity(resultSet.getInt("quantity"));
        stockTransaction.setPrice(BigDecimal.valueOf(resultSet.getDouble("price")));
        return stockTransaction;
    }

    private void setStockTransactionParameters(PreparedStatement preparedStatement, StockTransaction stockTransaction) throws SQLException {
        preparedStatement.setInt(1, stockTransaction.getStockId());
        preparedStatement.setInt(2, stockTransaction.getUserId());
        preparedStatement.setString(3, String.valueOf(stockTransaction.getTransactionType()));
        preparedStatement.setDate(4, Date.valueOf(stockTransaction.getTransactionDate()));
        preparedStatement.setInt(5, stockTransaction.getQuantity());
        preparedStatement.setBigDecimal(6, stockTransaction.getPrice());
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Database error", e);
    }
}
