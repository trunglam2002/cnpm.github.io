package com.example.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import controller.StockController;
import dao.StockDAO;
import dao.StockPriceDAO;
import dao.StockTransactionDAO;
import dao.UserDAO;
import model.Stock;
import model.StockPrice;
import model.User;

public class TestStockController {
    public static void main(String[] args) {
        // Create instances of DAOs and Controller
        StockDAO stockDAO = new StockDAO();
        StockPriceDAO stockPriceDAO = new StockPriceDAO();
        StockTransactionDAO stockTransactionDAO = new StockTransactionDAO();
        UserDAO userDAO = new UserDAO();
        StockController stockController = new StockController(stockDAO, stockTransactionDAO, userDAO);

        // Display stock list
        // stockController.viewAllStockDetails();
        // giao dich mua ban
        stockController.performTransaction(3, 2, 2,
                userDAO.getUserBalance(3), "BUY");

    }
}
