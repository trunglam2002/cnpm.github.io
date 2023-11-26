package server;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String args[]) throws IOException {
        String clientID = args[0];
        String connectionType = args[1];
        int port = Integer.parseInt(args[2]);

        switch (connectionType) {
            case "FEED":
                System.out.println("Starting feed method call...");
                startFeed(clientID, port);
                break;
            case "EXEC":
                startExec(clientID, port);
                break;
            default:
                System.out.println("Could not read connection type. Exiting...");
        }
    }

    public static void startExec(String clientID, int port) throws IOException {
        Socket connExchange = new Socket("localhost", port);
        BufferedReader input = new BufferedReader(new InputStreamReader(connExchange.getInputStream()));
        PrintWriter output = new PrintWriter(connExchange.getOutputStream(), true);

        String initExec = clientID + "|" + "EXEC";
        System.out.println("Sending client Id and exec feed to exchange...");
        output.println(initExec);

        boolean isRunning = true;
        Console c = System.console();

        while (isRunning) {
            printMenu();

            String command = c.readLine();
            int action = Integer.parseInt(command);

            switch (action) {
                case 1:
                    // Create order
                    System.out.println("Order Entry:  [BUY/SELL] | [quantity] | [price]");
                    String order = c.readLine();
                    String[] orderArray = order.split("\\|");
                    System.out.println("Making Order, field1: " + orderArray[0]);
                    System.out.println("Making Order, field2: " + orderArray[1]);
                    System.out.println("Making Order, field3: " + orderArray[2]);

                    System.out.println("Sending order...");
                    output.println(clientID + "|" + "NewOrder" + "|" + orderArray[0] + "|" + orderArray[1] + "|" + orderArray[2]);
                    break;

                case 2:
                    // Cancel order
                    String cancelID = c.readLine("Enter Order ID to Cancel");
                    System.out.println("Sending Cancellation to exchange...");
                    output.println(clientID + "|" + "CancelOrder" + "|" + cancelID);
                    break;

                case 3:
                    // Request market data
                    System.out.println("Requesting market data...");
                    output.println(clientID + "|" + "MarketData");
                    break;

                case 4:
                    // Exit
                    isRunning = false;
                    break;

                default:
                    System.out.println("Not a valid option");
                    break;
            }
        }

        System.out.println("Client exiting...");
    }

    public static void printMenu() {
        System.out.println("You are connected to the exchange as an EXEC Feed:");
        System.out.println("1 : Create Order");
        System.out.println("2 : Cancel Order");
        System.out.println("3 : Request Market Data");
        System.out.println("4 : Quit");
    }

    public static void startFeed(String clientID, int port) throws IOException {
        Socket connFeedExchange = new Socket("localhost", port);
        BufferedReader input = new BufferedReader(new InputStreamReader(connFeedExchange.getInputStream()));
        PrintWriter output = new PrintWriter(connFeedExchange.getOutputStream(), true);

        String initExec = clientID + "|" + "FEED";
        System.out.println("Sending ID and feed request to server " + clientID);
        output.println(initExec);

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Feed is ready! Waiting for lines from exchange....");
            String feed = input.readLine();
            System.out.println("Message from gateway received!");
            System.out.println(feed);
        }
    }
}
