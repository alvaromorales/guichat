package main;

import java.io.*;
import java.net.Socket;

import javax.swing.SwingUtilities;

import client.ChatGUI;
import client.ChatSession;

/**
 * GUI chat client runner.
 */
public class Client {

    private final String SERVER_NAME = "localhost"; //placeholder for now
    private final int SERVER_PORT = 4444;
    public BufferedReader input;
    public PrintWriter output;
    private ChatSession chatSession;

    /**
     * Start a GUI chat client.
     */
    public Client() {
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
        //chatSession = new ChatSession(output);
    }

    /**
     * Listen for responses from the server.
     * @throws IOException if the server socket is broken
     */
    private void listen() throws IOException {
        //Types of responses (both success and failure):
        // - login 
        // - joining a chatroom
        // - leaving a chatroom
        // - send a message
        // - other user joining a chatroom
        // - other user leaving a chatroom
        String serverResponse;
        while ((serverResponse = input.readLine()) != null) {
            //currently just prints to the console
            //will be route to a response handler in the future
            System.out.println(serverResponse);
        }
    }
    
    /**
     * Runs the client
     */
    public static void main(String[] args) {
        final Client client = new Client();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatGUI main = new ChatGUI(client.input,client.output);
                main.setVisible(true);
            }
        });
        
        try {
            //Read in responses from the server.
            client.listen();
        } catch (IOException e) {
            System.out.println("Socket broken.");
            e.printStackTrace();
        }
        
    }
}
