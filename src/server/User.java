package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import com.google.gson.*;

import protocol.InterfaceAdapter;
import protocol.Login;
import protocol.Login;
import protocol.Request;
import protocol.Response;
import protocol.ServerError;

/**
 * Represents a user connected to the Server
 */
public class User implements Runnable {
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private BlockingQueue<Request> requestQueue;
    private Gson requestGson;
    private Gson responseGson;
    private Set<String> usernames;
    
    /**
     * Creates a new user
     * @param socket the user's socket
     */
    public User(Socket socket, BufferedReader input, PrintWriter output, BlockingQueue<Request> requestQueue, Set<String> usernames) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.requestQueue = requestQueue;
        this.requestGson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).create();
        this.responseGson = new GsonBuilder().registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).create();
        this.usernames = usernames;
    }
    
    @Override
    public void run() {
        login();
        listen();
    }
    
    /**
     * Logs a user in
     */
    public void login() {
        try {
            boolean isLoggedIn = false;
            String clientRequest;
            while (((clientRequest = input.readLine()) != null) && !isLoggedIn) {
                try {
                    Login loginRequest = requestGson.fromJson(clientRequest, Login.class);
                    synchronized (usernames) {
                        if (usernames.add(loginRequest.getUsername())) {
                            Response loginSuccessful = new Login(loginRequest.getUsername());
                            output.println(responseGson.toJson(loginSuccessful));
                            output.flush();
                            isLoggedIn = true;
                        } else {
                            Response loginError = new ServerError(ServerError.Type.LOGIN_TAKEN, "Username taken");
                            output.println(responseGson.toJson(loginError));
                            output.flush();
                        }
                    }
                } catch(Exception e) {
                    ServerError error = new ServerError(ServerError.Type.UNAUTHORIZED,"Unauthorized. Please log in.");
                    output.write(responseGson.toJson(error) + "\n");
                }
                
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Listens for client requests
     */
    public void listen() {
        try {
            String clientRequest;
            while ((clientRequest = input.readLine()) != null) {
                Request r = requestGson.fromJson(clientRequest, Request.class);
                try {
                    requestQueue.put(r);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

}
