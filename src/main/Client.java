package main;

import java.io.*;
import java.net.Socket;

/**
 * GUI chat client runner.
 */
public class Client {

    private static final String SERVER_NAME = "localhost"; //placeholder for now
    private static final int SERVER_PORT = 9999; //placeholder for now
    private static BufferedReader input;
    private static PrintWriter output;
    private static ChatSession chatSession;

    /**
     * Start a GUI chat client.
     */
    public static void main(String[] args) {
        try {
            //Attempt to connect to the chat server
            Socket socket = new Socket(SERVER_NAME, SERVER_PORT);
            input = new BufferedReader(
                        new InputStreamReader(
                            socket.getInputStream()));
            output = new PrintWriter(
                         new OutputStreamWriter(
                             socket.getOutputStream()));
            //Alert of success for testing purposes
            System.out.println("Connected to chat server at " + SERVER_NAME + ":" + SERVER_PORT + ".");
        } catch (IOException e) {
            //Failure to connect
            e.printStackTrace();
            System.out.println("Failed to connect to chat server at " + SERVER_NAME + ":" + SERVER_PORT + " or broken socket.");
            System.exit(-1);
        }

        //Create chat session instance
        //perhaps check to see if username is available here
        chatSession = new ChatSession(output);

        try {
            //Read in responses from the server.
            listen();
        } catch (IOException e) {
            System.out.println("Socket broken.");
            e.printStackTrace();
        }
    }

    /**
     * Listen for responses from the server.
     * @throws IOException if the server socket is broken
     */
    private static void listen() throws IOException {
        String serverResponse;
        while ((serverResponse = input.readLine()) != null) {
            //currently just prints to the console
            //will be route to a response handler in the future
            System.out.println(serverResponse);
        }
    }
}
