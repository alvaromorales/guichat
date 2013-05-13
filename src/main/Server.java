package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import protocol.Request;
import server.RequestHandler;
import server.User;

/**
 * Chat server runner.
 */
public class Server {

    private final ServerSocket serverSocket;
    private static final int SERVER_PORT = 4444;
    private final String serverErrorMessage = "Unable to connect to port " + SERVER_PORT + ". " +
                                              "Try again and consider using a different port number";
    private BlockingQueue<Request> requestQueue;
    private Map<String,User> users;
    private RequestHandler requestHandler;
    private AtomicBoolean running;
    
    /**
     * Creates a new server instance on the specified port
     * @param port the port where the server runs
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            requestQueue = new LinkedBlockingQueue<Request>();
            users = Collections.synchronizedMap(new HashMap<String,User>());
            requestHandler = new RequestHandler(requestQueue, users);
            running = new AtomicBoolean(true);
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
        while(running.get()) {
            Socket socket = serverSocket.accept();
            BufferedReader input = new BufferedReader(
                                       new InputStreamReader(
                                           socket.getInputStream()));
            PrintWriter output = new PrintWriter(
                                     new OutputStreamWriter(
                                         socket.getOutputStream()));
            Thread t = new Thread(new User(socket, input, output, requestQueue, users));
            t.start();
        }
    }
    
    /**
     * Stops the server
     */
    public void stop() {
        running.set(false);
        requestHandler.stop();
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
