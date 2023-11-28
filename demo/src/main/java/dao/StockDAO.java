package dao;

import model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String query = "SELECT * FROM stock";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Stock stock = new Stock();
                stock.setId(resultSet.getInt("id"));
                stock.setSymbol(resultSet.getString("symbol"));
                stock.setCompany(resultSet.getString("company"));
                stocks.add(stock);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return stocks;
    }

    public Stock getStockById(int stockId) {
        String query = "SELECT * FROM stock WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Stock stock = new Stock();
                    stock.setId(resultSet.getInt("id"));
                    stock.setSymbol(resultSet.getString("symbol"));
                    stock.setCompany(resultSet.getString("company"));
                    return stock;
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return null;
    }

    public void addStock(Stock stock) {
        String query = "INSERT INTO stock (symbol, company) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, stock.getSymbol());
            preparedStatement.setString(2, stock.getCompany());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStock(Stock stock) {
        String query = "UPDATE stock SET symbol = ?, company = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, stock.getSymbol());
            preparedStatement.setString(2, stock.getCompany());
            preparedStatement.setInt(3, stock.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStock(int stockId) {
        String query = "DELETE FROM stock WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleSQLException(SQLException e) {
        // Xử lý ngoại lệ SQL, ví dụ: log và/hoặc ném lại ngoại lệ khác
        e.printStackTrace();
        throw new RuntimeException("Database error", e);
    }
}
