package model;

import java.math.BigDecimal;

public class Stock {
    private int id;
    private String symbol;
    private String company;

    private BigDecimal currentPrice; // Thêm thông tin về giá hiện tại của cổ phiếu

    private int quantity;  // Trường mới để đại diện cho số lượng cổ phiếu có sẵn

    // Constructors, getters, setters
    public Stock() {

    }
    public Stock(int id, String symbol, String company) {
        this.id = id;
        this.symbol = symbol;
        this.company = company;
    }

    public void updateQuantity(int quantityChange) {
        this.quantity += quantityChange;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}