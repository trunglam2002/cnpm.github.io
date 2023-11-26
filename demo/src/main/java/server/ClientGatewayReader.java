package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientGatewayReader implements Runnable {

	private Socket socket;

	public ClientGatewayReader(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String message = input.readLine();
				if (message == null) {
					break;
				}

				System.out.println("Received message from gateway: " + message);
				// Add your logic to process the received message here
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
