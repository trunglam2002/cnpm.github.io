package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientGatewayExec implements Runnable {

	// Lớp này sẽ đại diện cho luồng thực thi cục bộ của khách hàng.

	private Socket socket;

	public ClientGatewayExec(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// Logic của bạn để xử lý luồng thực thi sẽ được đặt ở đây
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				// Đọc các tin nhắn từ máy chủ và xử lý chúng
				String message = input.readLine();
				if (message == null) {
					break;  // Thoát khỏi vòng lặp nếu máy chủ ngắt kết nối
				}

				// Xử lý tin nhắn đã nhận
				processMessage(message);
			}
		} catch (IOException e) {
			// Xử lý IOException
			e.printStackTrace();
		} finally {
			// Đóng các tài nguyên nếu cần
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processMessage(String message) {
		try {
			// Phân tích và xử lý tin nhắn dựa trên nội dung
			String[] parts = message.split("\\|");

			if (parts.length >= 2) {
				String clientID = parts[0];
				String messageType = parts[1];

				// Xử lý tin nhắn dựa trên loại
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
			// Xử lý bất kỳ ngoại lệ nào xảy ra trong quá trình xử lý tin nhắn
			e.printStackTrace();
		}
	}

	private void handleNewOrder(String clientID, String[] messageParts) {
		// Xử lý lệnh mới ở đây
		// messageParts[2] là loại lệnh, messageParts[3] là số lượng, messageParts[4] là giá
		System.out.println("Received NewOrder from client " + clientID);
		// Thực hiện xử lý tùy thuộc vào yêu cầu của bạn
	}

	private void handleCancelOrder(String clientID, String[] messageParts) {
		// Xử lý lệnh hủy ở đây
		// messageParts[2] có thể là ID của lệnh cần hủy
		System.out.println("Received CancelOrder from client " + clientID);
		// Thực hiện xử lý tùy thuộc vào yêu cầu của bạn
	}

	private void handleMarketDataRequest(String clientID) {
		// Xử lý yêu cầu dữ liệu thị trường ở đây
		System.out.println("Received MarketData request from client " + clientID);
		// Thực hiện xử lý tùy thuộc vào yêu cầu của bạn
	}

	private void handleUnknownMessageType(String clientID, String messageType, String[] messageParts) {
		// Xử lý khi gặp một loại tin nhắn không xác định
		System.out.println("Unknown message type from client " + clientID + ": " + messageType);
		// Thực hiện xử lý tùy thuộc vào yêu cầu của bạn
	}

	private void handleInvalidMessage(String message) {
		// Xử lý khi nhận được một tin nhắn không hợp lệ
		System.out.println("Invalid message received: " + message);
		// Thực hiện xử lý tùy thuộc vào yêu cầu của bạn
	}
}