package model;

import java.math.BigDecimal;
import java.sql.Date;

public class StockTransaction {
    private int id;
    private int stockId;
    private int userId;
    private String transactionType;
    private Date transactionDate;
    private int quantity;
    private BigDecimal price;

    // Constructors, getters, setters
    public StockTransaction() {

    }

    public StockTransaction(int id, int stockId, int userId, String transactionType, Date transactionDate, int quantity, BigDecimal price) {
        this.id = id;
        this.stockId = stockId;
        this.userId = userId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getStockId() {
        return stockId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", userId=" + userId +
                ", transactionType='" + transactionType + '\'' +
                ", transactionDate=" + transactionDate +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
