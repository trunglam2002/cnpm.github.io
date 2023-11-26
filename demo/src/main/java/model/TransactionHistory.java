package model;

public class TransactionHistory {
    private int id;
    private int userId;
    private int stockId;
    private int stockTransactionId;

    // Getters and setters...
    public TransactionHistory() {

    }

    public TransactionHistory(int id, int userId, int stockId, int stockTransactionId) {
        this.id = id;
        this.userId = userId;
        this.stockId = stockId;
        this.stockTransactionId = stockTransactionId;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getStockId() {
        return stockId;
    }

    public int getStockTransactionId() {
        return stockTransactionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public void setStockTransactionId(int stockTransactionId) {
        this.stockTransactionId = stockTransactionId;
    }

    @Override
    public String toString() {
        return "TransactionHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", stockId=" + stockId +
                ", stockTransactionId=" + stockTransactionId +
                '}';
    }
}
