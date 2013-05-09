package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import protocol.Request;

/**
 * Represents a user connected to the Server
 */
public class User implements Runnable {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private BlockingQueue<Request> requestQueue;
    
    /**
     * Creates a new user
     * @param socket the user's socket
     */
    public User(Socket socket, BufferedReader input, PrintWriter output, BlockingQueue<Request> requestQueue) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.requestQueue = requestQueue;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        // authenticate()
        listen();
    }
    
    /**
     * Listens for client requests
     */
    public void listen() {
        try {
            String clientRequest;
            while ((clientRequest = input.readLine()) != null) {
                //currently just prints to the console
                //will be route to a request handler in the future
                System.out.println(clientRequest);
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

}
