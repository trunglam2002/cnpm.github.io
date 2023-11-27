package model;

import java.math.BigDecimal;

public class User {
    private int id;
    private String username;
    private String password;
    private BigDecimal balance;

    public User() {
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Encapsulation
    public BigDecimal getBalance() {
        return balance;
    }

    // Validation in setter
    public void setBalance(BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) >= 0) {
            this.balance = balance;
        } else {
            throw new IllegalArgumentException("Balance must be non-negative or null");
        }
    }

    // Other getters and setters...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}