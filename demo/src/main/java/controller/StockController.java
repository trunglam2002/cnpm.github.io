package controller;

import dao.StockDAO;
import dao.StockTransactionDAO;
import dao.UserDAO;
import model.Stock;
import model.StockTransaction;
import model.User;

import java.math.BigDecimal;
import java.util.List;

public class StockController {
    private final StockDAO stockDAO;
    private final StockTransactionDAO stockTransactionDAO;
    private final UserDAO userDAO;

    public StockController(StockDAO stockDAO, StockTransactionDAO stockTransactionDAO, UserDAO userDAO) {
        this.stockDAO = stockDAO;
        this.stockTransactionDAO = stockTransactionDAO;
        this.userDAO = userDAO;
    }

    public List<Stock> getAllStocks() {
        return stockDAO.getAllStocks();
    }

    public void viewStockList(boolean showDetails, int stockIdToShowDetails) {
        List<Stock> stocks = stockDAO.getAllStocks();

        if (stocks.isEmpty()) {
            System.out.println("Không có cổ phiếu khả dụng.");
        } else {
            System.out.println("Danh sách cổ phiếu:");
            for (Stock stock : stocks) {
                System.out.println("ID Cổ phiếu: " + stock.getId());
                System.out.println("Ký hiệu: " + stock.getSymbol());
                System.out.println("Công ty: " + stock.getCompany());
                if (showDetails && stock.getId() == stockIdToShowDetails) {
                    System.out.println("Giá hiện tại: " + stock.getCurrentPrice());
                    // Hiển thị các thông tin khác nếu cần
                }
                System.out.println("----------------------------------");
            }
        }
    }

    public void viewStockDetails(int stockId) {
        Stock stock = stockDAO.getStockById(stockId);
        if (stock != null) {
            System.out.println("Thông tin cổ phiếu:");
            System.out.println("ID Cổ phiếu: " + stock.getId());
            System.out.println("Ký hiệu: " + stock.getSymbol());
            System.out.println("Công ty: " + stock.getCompany());
            System.out.println("Giá hiện tại: " + stock.getCurrentPrice());
            // Hiển thị các thông tin khác nếu cần
            System.out.println("----------------------------------");
        } else {
            System.out.println("Không tìm thấy cổ phiếu.");
        }
    }

    public void buyStock(int userId, int stockId, int quantity, BigDecimal limitPrice) {
        performTransaction(userId, stockId, quantity, limitPrice, "BUY");
    }

    public void sellStock(int userId, int stockId, int quantity, BigDecimal limitPrice) {
        performTransaction(userId, stockId, quantity, limitPrice, "SELL");
    }

    public void placeLimitOrder(int userId, int stockId, int quantity, BigDecimal limitPrice, String orderType) {
        Stock stock = stockDAO.getStockById(stockId);
        if (stock == null) {
            System.out.println("Không tìm thấy cổ phiếu.");
            return;
        }

        // Kiểm tra giá cổ phiếu và thực hiện giao dịch nếu giá đạt đến mức giá đã đặt
        if ((orderType.equalsIgnoreCase("BUY") && stock.getCurrentPrice().compareTo(limitPrice) <= 0)
                || (orderType.equalsIgnoreCase("SELL") && stock.getCurrentPrice().compareTo(limitPrice) >= 0)) {
            // Thực hiện giao dịch
            StockTransaction limitOrder = createTransaction(userId, stockId, quantity, limitPrice, orderType);
            stockTransactionDAO.addStockTransaction(limitOrder);
            updateBalance(userId, limitOrder);
            System.out.println("Đặt lệnh giới hạn thành công. ID giao dịch: " + limitOrder.getId());
        } else {
            System.out.println("Lệnh giới hạn không được thực hiện. Giá cổ phiếu hiện tại không đạt đến mức giá đã đặt.");
        }
    }

    private void performTransaction(int userId, int stockId, int quantity, BigDecimal limitPrice, String transactionType) {
        Stock stock = stockDAO.getStockById(stockId);
        if (stock == null) {
            System.out.println("Không tìm thấy cổ phiếu.");
            return;
        }

        BigDecimal totalPrice = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));

        // Kiểm tra xem người dùng có đủ tiền hoặc cổ phiếu để thực hiện giao dịch không
        if ((transactionType.equals("BUY") && limitPrice != null && limitPrice.compareTo(stock.getCurrentPrice()) < 0)
                || (transactionType.equals("SELL") && quantity > getUserStockQuantity(userId, stockId))) {
            System.out.println("Giao dịch không thể hoàn thành. Kiểm tra số dư hoặc số lượng cổ phiếu.");
            return;
        }

        // Thực hiện giao dịch
        StockTransaction transaction = createTransaction(userId, stockId, quantity, limitPrice, transactionType);
        stockTransactionDAO.addStockTransaction(transaction);
        updateBalance(userId, transaction);
        System.out.println("Giao dịch thành công. ID giao dịch: " + transaction.getId());
    }

    private int getUserStockQuantity(int userId, int stockId) {
        List<StockTransaction> userTransactions = stockTransactionDAO.getStockTransactionsByUser(userId);
        return userTransactions.stream()
                .filter(t -> t.getStockId() == stockId)
                .mapToInt(StockTransaction::getQuantity)
                .sum();
    }

    private StockTransaction createTransaction(int userId, int stockId, int quantity, BigDecimal limitPrice, String transactionType) {
        Stock stock = stockDAO.getStockById(stockId);
        StockTransaction transaction = new StockTransaction();
        transaction.setStockId(stockId);
        transaction.setUserId(userId);
        transaction.setTransactionType(StockTransaction.TransactionType.valueOf(transactionType.toUpperCase()));
        transaction.setTransactionDate(DateUtils.getCurrentDate().toLocalDate());
        transaction.setQuantity(quantity);
        transaction.setPrice(limitPrice != null ? limitPrice : stock.getCurrentPrice());
        return transaction;
    }

    private void updateBalance(int userId, StockTransaction transaction) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            BigDecimal transactionAmount = transaction.getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity()));
            if (transaction.getTransactionType() == StockTransaction.TransactionType.BUY) {
                user.setBalance(user.getBalance().subtract(transactionAmount));
            } else if (transaction.getTransactionType() == StockTransaction.TransactionType.SELL) {
                user.setBalance(user.getBalance().add(transactionAmount));
            }
            userDAO.updateUser(user);
        }
    }
}
