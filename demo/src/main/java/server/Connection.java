// Lớp Connection
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PriorityQueue;

public class Connection implements Runnable {

	private Socket conn;
	private String clientID;
	private ClientConnectionType connType;

	private PrintWriter out;
	private BufferedReader in;
	private Exchange exchange;

	public Connection(Socket clientSocket, Exchange excObj) {
		this.conn = clientSocket;
		this.exchange = excObj;
	}

	public void run() {
		try {
			out = new PrintWriter(conn.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String bufferString = in.readLine();
			String[] clientInfo = bufferString.split("\\|");

			this.clientID = clientInfo[0];
			this.connType = ClientConnectionType.valueOf(clientInfo[1].trim());

			System.out.println("Starting connection for client " + clientID + " on connection type " + connType.toString());

			switch (connType) {
				case EXEC:
					runExec();
					break;

				case FEED:
					runFeed();
					break;

				default:
					System.out.println("Error starting client connection, improper connection type specified from client");
					conn.close();
					break;
			}
		} catch (IOException e) {
			System.out.println("Error getting streams from client");
		}
	}

	public void runExec() {
		System.out.println("Starting exec connection now...");
		while (!exchange.isStopped()) {
			try {
				String input;
				while ((input = in.readLine()) != null) {
					String[] messageArray = input.split("\\|", 5);
					System.out.println("Message received from the client: " + input);

					String messageType = messageArray[1];

					switch (messageType) {
						case "NewOrder":
							System.out.println("New Order received!");
							Order incomingOrder = new Order(messageArray[0], OrderType.valueOf(messageArray[2]),
									Integer.parseInt(messageArray[3]), Double.parseDouble(messageArray[4]), true);
							exchange.addOrder(incomingOrder);
							break;

						case "CancelOrder":
							System.out.println("Cancellation request received");
							exchange.cancelOrder(messageArray[0], messageArray[2]);
							break;

						case "MarketData":
							System.out.println("Sending Request for market data, client ID: " + messageArray[0]);
							exchange.sendMarketData(messageArray[0]);
							break;

						default:
							break;
					}
				}
			} catch (IOException e) {
				System.out.println("Exception thrown while reading from exec feed");
			}
		}
	}

	public void runFeed() {
		try {
			System.out.println("Registering client with the exchange..." + this.toString());
			exchange.addClientFeed(clientID, this);

			while (!exchange.isStopped()) {
				String input = in.readLine();

				if (input != null) {
					System.out.println("Received message from the server: " + input);

					// Process feed message here
					processFeedMessage(input);
				}
			}
		} catch (IOException e) {
			System.out.println("Exception occurred while reading from feed");
		}
	}

	private void processFeedMessage(String feedMessage) {
		// Implement logic to process feed messages here
		// Example: You can add the feed message to a queue for further processing
		System.out.println("Processing feed message: " + feedMessage);
	}

	public void sendMessage(String message) {
		out.println(message);
	}
}
