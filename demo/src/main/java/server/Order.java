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
		this.price = price;
		this.quantity = quantity;
		this.clientID = clientID;
		this.type = type;
		this.orderID = UUID.randomUUID();
		this.isValid = true;
		this.isRealOrder = isRealOrder;
	}

	// Constructor for an invalid order
	public Order() {
		this.isValid = false;
	}

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
}