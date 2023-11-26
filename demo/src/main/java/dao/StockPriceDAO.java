package dao;

import model.StockPrice;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockPriceDAO {
    public List<StockPrice> getStockPricesByStockId(int stockId) {
        List<StockPrice> stockPrices = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_price WHERE stock_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.setId(resultSet.getInt("id"));
                        stockPrice.setStockId(resultSet.getInt("stock_id"));
                        stockPrice.setDate(resultSet.getDate("date"));
                        stockPrice.setOpen(BigDecimal.valueOf(resultSet.getDouble("open")));
                        stockPrice.setHigh(BigDecimal.valueOf(resultSet.getDouble("high")));
                        stockPrice.setLow(BigDecimal.valueOf(resultSet.getDouble("low")));
                        stockPrice.setClose(BigDecimal.valueOf(resultSet.getDouble("close")));
                        stockPrice.setAdjustedClose(BigDecimal.valueOf(resultSet.getDouble("adjusted_close")));
                        stockPrice.setVolume(resultSet.getInt("volume"));
                        stockPrices.add(stockPrice);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockPrices;
    }

    public void addStockPrice(StockPrice stockPrice) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO stock_price (stock_id, date, open, high, low, close, adjusted_close, volume) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockPrice.getStockId());
                preparedStatement.setDate(2, stockPrice.getDate());
                preparedStatement.setBigDecimal(3, stockPrice.getOpen());
                preparedStatement.setBigDecimal(4, stockPrice.getHigh());
                preparedStatement.setBigDecimal(5, stockPrice.getLow());
                preparedStatement.setBigDecimal(6, stockPrice.getClose());
                preparedStatement.setBigDecimal(7, stockPrice.getAdjustedClose());
                preparedStatement.setInt(8, stockPrice.getVolume());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStockPrice(StockPrice stockPrice) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE stock_price SET date = ?, open = ?, high = ?, low = ?, close = ?, " +
                    "adjusted_close = ?, volume = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, stockPrice.getDate());
                preparedStatement.setBigDecimal(2, stockPrice.getOpen());
                preparedStatement.setBigDecimal(3, stockPrice.getHigh());
                preparedStatement.setBigDecimal(4, stockPrice.getLow());
                preparedStatement.setBigDecimal(5, stockPrice.getClose());
                preparedStatement.setBigDecimal(6, stockPrice.getAdjustedClose());
                preparedStatement.setInt(7, stockPrice.getVolume());
                preparedStatement.setInt(8, stockPrice.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStockPrice(int stockPriceId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM stock_price WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockPriceId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
