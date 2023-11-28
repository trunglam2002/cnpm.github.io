package dao;

import model.StockPrice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockPriceDAO {

    public List<StockPrice> getStockPricesByStockId(int stockId) {
        List<StockPrice> stockPrices = new ArrayList<>();
        String query = "SELECT * FROM stock_price WHERE stock_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    StockPrice stockPrice = createStockPriceFromResultSet(resultSet);
                    stockPrices.add(stockPrice);
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return stockPrices;
    }

    public void addStockPrice(StockPrice stockPrice) {
        String query = "INSERT INTO stock_price (stock_id, date, open, high, low, close, adjusted_close, volume) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setStockPriceParameters(preparedStatement, stockPrice);
            preparedStatement.setDate(2, Date.valueOf(stockPrice.getDate()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateStockPrice(StockPrice stockPrice) {
        String query = "UPDATE stock_price SET date = ?, open = ?, high = ?, low = ?, close = ?, " +
                "adjusted_close = ?, volume = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setStockPriceParameters(preparedStatement, stockPrice);
            preparedStatement.setDate(1, Date.valueOf(stockPrice.getDate()));
            preparedStatement.setInt(8, stockPrice.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteStockPrice(int stockPriceId) {
        String query = "DELETE FROM stock_price WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, stockPriceId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private StockPrice createStockPriceFromResultSet(ResultSet resultSet) throws SQLException {
        StockPrice stockPrice = new StockPrice();
        stockPrice.setId(resultSet.getInt("id"));
        stockPrice.setStockId(resultSet.getInt("stock_id"));
        stockPrice.setDate(resultSet.getDate("date").toLocalDate());
        stockPrice.setOpen(resultSet.getBigDecimal("open"));
        stockPrice.setHigh(resultSet.getBigDecimal("high"));
        stockPrice.setLow(resultSet.getBigDecimal("low"));
        stockPrice.setClose(resultSet.getBigDecimal("close"));
        stockPrice.setAdjustedClose(resultSet.getBigDecimal("adjusted_close"));
        stockPrice.setVolume(resultSet.getInt("volume"));
        return stockPrice;
    }

    private void setStockPriceParameters(PreparedStatement preparedStatement, StockPrice stockPrice) throws SQLException {
        preparedStatement.setInt(1, stockPrice.getStockId());
        preparedStatement.setDate(2, Date.valueOf(stockPrice.getDate()));
        preparedStatement.setBigDecimal(3, stockPrice.getOpen());
        preparedStatement.setBigDecimal(4, stockPrice.getHigh());
        preparedStatement.setBigDecimal(5, stockPrice.getLow());
        preparedStatement.setBigDecimal(6, stockPrice.getClose());
        preparedStatement.setBigDecimal(7, stockPrice.getAdjustedClose());
        preparedStatement.setInt(8, stockPrice.getVolume());
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Database error", e);
    }
}
