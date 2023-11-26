package server;

public class Message {

	private boolean isOrder;
	private String clientID;
	private String orderID;

	public Message(boolean isOrder, String clientID, String orderID) {
		this.isOrder = isOrder;
		this.clientID = clientID;
		this.orderID = orderID;
	}

	public boolean isOrder() {
		return isOrder;
	}

	public void setOrder(boolean order) {
		isOrder = order;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
}