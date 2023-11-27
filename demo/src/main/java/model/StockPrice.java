package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockPrice {
    private int id;
    private int stockId;
    private LocalDate date;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal adjustedClose;
    private int volume;

    public StockPrice() {
    }

    public StockPrice(int id, int stockId, LocalDate date, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal adjustedClose, int volume) {
        this.id = id;
        this.stockId = stockId;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
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

    // Use LocalDate instead of Date
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Validation in setter
    public void setVolume(int volume) {
        if (volume >= 0) {
            this.volume = volume;
        } else {
            throw new IllegalArgumentException("Volume must be non-negative");
        }
    }

    // Other getters and setters...

    @Override
    public String toString() {
        return "StockPrice{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", adjustedClose=" + adjustedClose +
                ", volume=" + volume +
                '}';
    }
}
