package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Chat server runner.
 */
public class Server {
    private final ServerSocket serverSocket;
    
    /**
     * Creates a new server instance on the specified port
     * @param port the port where the server runs
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect. Try again and consider using a different port number");
        }
    }
    
    /**
     * Runs the server.
     * Listens to incoming connections and creates threads for every user connected
     * @throws IOException if the server socket is broken.
     */
    public void serve() throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();
            Thread t = new Thread(new User(socket));
            t.run();
        }
    }
    
    /**
     * Start a chat server.
     */
    public static void main(String[] args) {
        Server server = new Server(4444);
        try {
            server.serve();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
