package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Represents a user connected to the Server
 */
public class User implements Runnable {
    private final Socket socket;
    
    
    /**
     * Creates a new user
     * @param socket the user's socket
     */
    public User(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        // authenticate()
        
        listen();
        // listen()
    }
    
    /**
     * Listens for client requests
     */
    public void listen() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientRequest;
            while ((clientRequest = in.readLine()) != null) {
                System.out.println(clientRequest);
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

}
