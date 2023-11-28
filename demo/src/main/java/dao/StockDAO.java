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
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Stock stock = new Stock();
                        stock.setId(resultSet.getInt("id"));
                        stock.setSymbol(resultSet.getString("symbol"));
                        stock.setCompany(resultSet.getString("company"));
                        stocks.add(stock);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public Stock getStockById(int stockId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addStock(Stock stock) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO stock (symbol, company) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, stock.getSymbol());
                preparedStatement.setString(2, stock.getCompany());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStock(Stock stock) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE stock SET symbol = ?, company = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, stock.getSymbol());
                preparedStatement.setString(2, stock.getCompany());
                preparedStatement.setInt(3, stock.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStock(int stockId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM stock WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
