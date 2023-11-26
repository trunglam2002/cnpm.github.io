// Lớp Exchange
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
		System.out.println("Adding order to the order book...");
		double price = orderToAdd.getPrice();
		orderbook.putIfAbsent(price, new PriorityQueue<>());
		orderbook.get(price).add(orderToAdd);
		printOrderBook();
	}

	public void cancelOrder(String clientID, String orderID) {
		System.out.println("Cancelling order for client " + clientID + " with ID " + orderID);

		for (PriorityQueue<Order> orderQueue : orderbook.values()) {
			// Iterate through all order queues in the orderbook
			for (Order order : orderQueue) {
				if (order.getClientID().equals(clientID) && order.getOrderID().toString().equals(orderID)) {
					// Found the order to cancel
					orderQueue.remove(order); // Remove the order from the queue
					System.out.println("Order cancelled: " + order);
					printOrderBook();
					return;
				}
			}
		}

		// If it reaches here, it means the order to cancel was not found
		System.out.println("Order not found for cancellation: Client " + clientID + ", Order ID " + orderID);
	}

	public void sendMarketData(String clientID) {
		System.out.println("Sending Market Data...");
		StringBuilder book = new StringBuilder();

		for (ConcurrentMap.Entry<Double, PriorityQueue<Order>> priceLevel : orderbook.entrySet()) {
			for (Order order : priceLevel.getValue()) {
				book.append(order.toString()).append("|");
			}
		}

		clientFeeds.get(clientID).sendMessage(book.toString());
	}

	public void addClientFeed(String clientID, Connection connection) {
		System.out.println("Adding client feed for " + clientID);
		clientFeeds.put(clientID, connection);
	}

	public void stopServer() {
		System.out.println("Stopping the server...");
		this.isStopped = true;
		try {
			this.serverSock.close();
		} catch (IOException e) {
			System.out.println("Error closing server socket");
		}
	}

	public boolean isStopped() {
		return this.isStopped;
	}

	public void printOrderBook() {
		System.out.println("Order Book:");
		for (ConcurrentMap.Entry<Double, PriorityQueue<Order>> priceLevel : orderbook.entrySet()) {
			System.out.print(priceLevel.getKey() + ": ");
			for (Order order : priceLevel.getValue()) {
				System.out.print(order + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
