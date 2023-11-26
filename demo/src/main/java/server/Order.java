package server;

import java.util.UUID;

public class Order {

	private double price;
	private int quantity;
	private String clientID;
	private UUID orderID;
	private OrderType type;
	private boolean isValid;
	private boolean isRealOrder;

	public Order(String clientID, OrderType type, int quantity, double price, boolean isRealOrder) {
		if (price < 0 || quantity < 0) {
			this.isValid = false;
		} else {
			this.price = price;
			this.quantity = quantity;
			this.clientID = clientID;
			this.type = type;
			this.orderID = UUID.randomUUID();
			this.isValid = true;
			this.isRealOrder = isRealOrder;
		}
	}

	// Constructor for an invalid order
	public Order() {
		this.isValid = false;
	}

	// Getters and setters

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getClientID() {
		return clientID;
	}

	public UUID getOrderID() {
		return orderID;
	}

	public OrderType getType() {
		return type;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isRealOrder() {
		return isRealOrder;
	}

	@Override
	public String toString() {
		return "Order{" +
				"price=" + price +
				", quantity=" + quantity +
				", clientID='" + clientID + '\'' +
				", orderID=" + orderID +
				", type=" + type +
				", isValid=" + isValid +
				", isRealOrder=" + isRealOrder +
				'}';
	}
}