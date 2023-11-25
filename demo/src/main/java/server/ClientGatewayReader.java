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
			// Logic để xử lý luồng đọc của khách hàng sẽ được đặt ở đây
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				// Đọc tin nhắn từ máy chủ và in chúng ra
				String message = input.readLine();
				if (message == null) {
					break;  // Thoát khỏi vòng lặp nếu máy chủ ngắt kết nối
				}

				// In tin nhắn ra màn hình
				System.out.println("Nhận được tin nhắn từ gateway: " + message);
			}
		} catch (IOException e) {
			// Xử lý IOException
			e.printStackTrace();
		} finally {
			// Đóng tài nguyên nếu cần
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
