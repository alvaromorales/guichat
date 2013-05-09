package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Chat server runner.
 */
public class Server {

    private final ServerSocket serverSocket;
    private static final int SERVER_PORT = 4444;
    private final String serverErrorMessage = "Unable to connect. Try again and consider" + 
                                              "using a different port number";
    private final List usernames = Collections.synchronizedList(new ArrayList<String>());
    
    /**
     * Creates a new server instance on the specified port
     * @param port the port where the server runs
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(serverErrorMessage);
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
            BufferedReader input = new BufferedReader(
                                       new InputStreamReader(
                                           socket.getInputStream()));
            PrintWriter output = new PrintWriter(
                                     new OutputStreamWriter(
                                         socket.getOutputStream()));
            Thread t = new Thread(new User(socket, input, output));
            t.run();
        }
    }
    
    /**
     * Start a chat server.
     */
    public static void main(String[] args) {
        Server server = new Server(SERVER_PORT);
        try {
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
