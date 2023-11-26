package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientGatewayExec implements Runnable {

	private Socket socket;

	public ClientGatewayExec(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			while (true) {
				String message = input.readLine();
				if (message == null) {
					break;
				}
				processMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
	}

	private void processMessage(String message) {
		try {
			String[] parts = message.split("\\|");

			if (parts.length >= 2) {
				String clientID = parts[0];
				String messageType = parts[1];

				switch (messageType) {
					case "NewOrder":
						handleNewOrder(clientID, parts);
						break;
					case "CancelOrder":
						handleCancelOrder(clientID, parts);
						break;
					case "MarketData":
						handleMarketDataRequest(clientID);
						break;
					default:
						handleUnknownMessageType(clientID, messageType, parts);
				}
			} else {
				handleInvalidMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleNewOrder(String clientID, String[] messageParts) {
		try {
			if (messageParts.length >= 5) {
				String orderType = messageParts[2];
				int quantity = Integer.parseInt(messageParts[3]);
				double price = Double.parseDouble(messageParts[4]);

				System.out.println("Received NewOrder from client " + clientID);
				// Add your logic to handle a new order
			} else {
				System.out.println("Invalid NewOrder message format: " + String.join("|", messageParts));
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid number format in NewOrder message: " + e.getMessage());
		}
	}

	private void handleCancelOrder(String clientID, String[] messageParts) {
		try {
			if (messageParts.length >= 3) {
				String orderID = messageParts[2];
				System.out.println("Received CancelOrder from client " + clientID);
				// Add your logic to handle a cancel order
			} else {
				System.out.println("Invalid CancelOrder message format: " + String.join("|", messageParts));
			}
		} catch (Exception e) {
			System.out.println("Error handling CancelOrder message: " + e.getMessage());
		}
	}

	private void handleMarketDataRequest(String clientID) {
		System.out.println("Received MarketData request from client " + clientID);
		// Add your logic to handle a market data request
	}

	private void handleUnknownMessageType(String clientID, String messageType, String[] messageParts) {
		System.out.println("Unknown message type from client " + clientID + ": " + messageType);
		// Add your logic to handle an unknown message type
	}

	private void handleInvalidMessage(String message) {
		System.out.println("Invalid message received: " + message);
		// Add your logic to handle an invalid message
	}

	private void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}