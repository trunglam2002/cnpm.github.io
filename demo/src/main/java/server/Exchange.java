package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.PriorityQueue;

public class Exchange {

	private boolean isStopped;
	private ServerSocket serverSock;
	private ConcurrentMap<String, Connection> clientFeeds;
	private ConcurrentMap<Double, PriorityQueue<Order>> orderbook;

	public static void main(String args[]) throws IOException {
		int port = Integer.parseInt(args[0]);
		Exchange OurExchange = new Exchange();
		OurExchange.runServer(port);
	}

	public Exchange() {
		orderbook = new ConcurrentHashMap<>();
		clientFeeds = new ConcurrentHashMap<>();
	}

	public void runServer(int port) throws IOException {
		Socket clientSock = null;
		this.serverSock = new ServerSocket(port);

		while (!isStopped) {
			try {
				clientSock = this.serverSock.accept();
			} catch (IOException e) {
				System.out.println("Error connecting to client");
			}

			new Thread(new Connection(clientSock, this)).start();
		}
	}

	public void addOrder(Order orderToAdd) {
		System.out.println("main.java.server.Exchange addOrder has been called");

		if (!instantFill(orderToAdd)) {
			System.out.println("Adding order to book now...");

			if (orderbook.containsKey(orderToAdd.getPrice())) {
				System.out.println("main.java.server.Order book contains orders at that level, adding our new order..");
				orderbook.get(orderToAdd.getPrice()).add(orderToAdd);

				clientFeeds.get(orderToAdd.getClientID()).feedMessageQueue.add("main.java.server.Order added with ID: " + orderToAdd.getOrderID().toString());

			} else {
				System.out.println("main.java.server.Order book has no orders at that price level, creating price level now...");
				PriorityQueue<Order> newprice = new PriorityQueue<>();
				newprice.add(orderToAdd);
				orderbook.putIfAbsent(orderToAdd.getPrice(), newprice);
				System.out.println("main.java.server.Order has been added!");
				clientFeeds.get(orderToAdd.getClientID()).addMessage("main.java.server.Order has been added to the book, ID: " + orderToAdd.getOrderID().toString());
			}
		}
	}

	public boolean instantFill(Order orderToFill) {
		if (orderToFill.getType() == OrderType.BUY) {
			try {
				for (ConcurrentMap.Entry<Double, PriorityQueue<Order>> priceLevel : orderbook.entrySet()) {
					for (Order individualOrder : priceLevel.getValue()) {
						if (orderToFill.getPrice() <= individualOrder.getPrice() && individualOrder.getType() == OrderType.SELL) {
							priceLevel.getValue().remove(individualOrder);
							match(orderToFill, individualOrder);
							return true;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Exception in instant fill " + e.toString());
			}
		} else if (orderToFill.getType() == OrderType.SELL) {
			Order currentBestFill = new Order();
			for (ConcurrentMap.Entry<Double, PriorityQueue<Order>> priceLevel : orderbook.entrySet()) {
				Order individualOrder = priceLevel.getValue().peek();

				if (individualOrder == null)
					return false;
				if (orderToFill.getPrice() <= individualOrder.getPrice() && individualOrder.getType() == OrderType.BUY) {
					currentBestFill = individualOrder;
					break;
				}
			}

			if (currentBestFill.isRealOrder()) {
				System.out.println("Current Best Fill found in instant fill...");
				orderbook.get(currentBestFill.getPrice()).remove(currentBestFill);
				match(orderToFill, currentBestFill);
				return true;
			}
		}
		System.out.println("No Instant Fill Made, returning false...");
		return false;
	}

	public void match(Order orderOne, Order orderTwo) {
		System.out.println("Match made!");
		String fill = "Fill Notification!  Buy Side: " + orderOne.getClientID() + " Sell Side: " + orderTwo.getClientID() +
				" Price: " + String.valueOf(orderTwo.getPrice()) + " Quantity: " + String.valueOf(orderTwo.getQuantity());
		clientFeeds.get(orderOne.getClientID()).addMessage(fill);
		clientFeeds.get(orderTwo.getClientID()).addMessage(fill);
	}

	public void cancelOrder(String clientID, String orderID) {
		for (ConcurrentMap.Entry<Double, PriorityQueue<Order>> priceLevel : orderbook.entrySet()) {
			for (Order individualOrder : priceLevel.getValue()) {
				System.out.println("Comparing order id " + individualOrder.getOrderID().toString() + " to order id " + orderID);
				if (individualOrder.getOrderID().toString().equals(orderID)) {
					System.out.println("Removing order # " + orderID);
					priceLevel.getValue().remove(individualOrder);
				}
			}
		}
	}

	public void sendMarketData(String clientID) {
		System.out.println("Sending Market Data...");
		StringBuilder book = new StringBuilder();

		for (ConcurrentMap.Entry<Double, PriorityQueue<Order>> priceLevel : orderbook.entrySet()) {
			Order order = priceLevel.getValue().peek();
			if (order != null) {
				book.append("Price: " + String.valueOf(order.getPrice()) + "    Quantity: " + String.valueOf(order.getQuantity()) +
						" 	Type: " + order.getType().toString());
				book.append(System.getProperty("line.separator"));
			}
		}
		System.out.println("Market Data assembled! " + book.toString());

		if (this.clientFeeds.containsKey(clientID)) {
			System.out.println("main.java.server.Client Feeds contains the key!");
			Connection ethan = this.clientFeeds.get(clientID);
			ethan.addMessage(book.toString());
		} else {
			System.out.println("Could not find a FEEd connection to send market data to...");
		}
	}

	public boolean registerClientFeed(String clientID, Connection connObject) {
		System.out.println("Putting clientID " + clientID + " Into client feeds object");
		this.clientFeeds.put(clientID, connObject);
		return false;
	}

	public boolean removeClientFeed(String clientID) {
		this.clientFeeds.remove(clientID);
		return true;
	}
}