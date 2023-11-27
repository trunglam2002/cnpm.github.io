package model;

public class TransactionHistory {
    private int id;
    private int userId;
    private int stockId;
    private int stockTransactionId;

    public TransactionHistory() {
    }

    // Encapsulation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getStockTransactionId() {
        return stockTransactionId;
    }

    public void setStockTransactionId(int stockTransactionId) {
        this.stockTransactionId = stockTransactionId;
    }

    // Other getters and setters...

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
