package model;

import java.math.BigDecimal;

public class Stock {
    private int id;
    private String symbol;
    private String company;
    private BigDecimal currentPrice;
    private int quantity;

    public Stock() {
    }

    public Stock(int id, String symbol, String company) {
        this.id = id;
        this.symbol = symbol;
        this.company = company;
    }

    public void updateQuantity(int quantityChange) {
        setQuantity(getQuantity() + quantityChange);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", company='" + company + '\'' +
                ", quantity=" + quantity +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
