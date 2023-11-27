package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockTransaction {
    private int id;
    private int stockId;
    private int userId;
    private TransactionType transactionType;
    private LocalDate transactionDate;
    private int quantity;
    private BigDecimal price;

    public enum TransactionType {
        BUY,
        SELL,
        LIMIT_BUY,
        LIMIT_SELL
    }

    public StockTransaction() {
    }

    // Encapsulation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    // Use LocalDate instead of Date
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Encapsulation
    public int getQuantity() {
        return quantity;
    }

    // Validation in setter
    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
    }

    // Encapsulation
    public BigDecimal getPrice() {
        return price;
    }

    // Validation in setter
    public void setPrice(BigDecimal price) {
        if (price != null && price.compareTo(BigDecimal.ZERO) >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price must be non-negative");
        }
    }

    // Other getters and setters...

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", userId=" + userId +
                ", transactionType=" + transactionType +
                ", transactionDate=" + transactionDate +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
