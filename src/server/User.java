package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import com.google.gson.*;

import protocol.InterfaceAdapter;
import protocol.LoginRequest;
import protocol.LoginResponse;
import protocol.Request;
import protocol.Response;
import protocol.ServerError;

/**
 * Represents a user connected to the Server
 */
public class User implements Runnable {
    private final BufferedReader input;
    private final PrintWriter output;
    private BlockingQueue<Request> requestQueue;
    private Gson gson;
    private Set<String> usernames;
    
    /**
     * Creates a new user
     * @param socket the user's socket
     */
    public User(BufferedReader input, PrintWriter output, BlockingQueue<Request> requestQueue, Set<String> usernames) {
        this.input = input;
        this.output = output;
        this.requestQueue = requestQueue;
        this.gson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).create();
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
                    LoginRequest loginRequest = gson.fromJson(clientRequest, LoginRequest.class);
                    System.out.println(loginRequest);
                    synchronized (usernames) {
                        if (usernames.add(loginRequest.getUsername())) {
                            Response loginSuccessful = new LoginResponse(loginRequest.getUsername());
                            System.out.println("Sending: " + loginSuccessful);
                            sendResponse(loginSuccessful);
                            isLoggedIn = true;
                        } else {
                            Response loginError = new ServerError(ServerError.Type.LOGIN_TAKEN, "Username taken");
                            sendResponse(loginError);
                        }
                    }
                } catch(Exception e) {
                    ServerError error = new ServerError(ServerError.Type.UNAUTHORIZED,"Unauthorized. Please log in.");
                    sendResponse(error);
                }
                
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a response to the user
     * @param response the response to send
     */
    public void sendResponse(Response response) {
        output.println(gson.toJson(response));
        output.flush();
    }
    
    /**
     * Listens for client requests
     */
    public void listen() {
        try {
            String clientRequest;
            while ((clientRequest = input.readLine()) != null) {
                Request r = gson.fromJson(clientRequest, Request.class);
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
