package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.*;

import protocol.InterfaceAdapter;
import protocol.Registration;
import protocol.Registration.*;
import protocol.Request;
import protocol.Response;
import protocol.ServerErrorResponse;

/**
 * Represents a user connected to the Server
 */
public class User implements Runnable {
    private final BufferedReader input;
    private final PrintWriter output;
    private BlockingQueue<Request> requestQueue;
    private Gson gson;
    private Map<String,User> users;
    private String username;
    private Socket socket;
    private AtomicBoolean running;

    /**
     * Creates a new user
     * @param socket the user's socket
     */
    public User(Socket socket, BufferedReader input, PrintWriter output, BlockingQueue<Request> requestQueue, Map<String,User> users) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.requestQueue = requestQueue;
        this.gson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).create();
        this.users = users;
        this.running = new AtomicBoolean(true);
    }

    /**
     * Runs the server
     */
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
            while (running.get() && !isLoggedIn && ((clientRequest = input.readLine()) != null)) {
                try {
                    Registration.LoginRequest loginRequest = (Registration.LoginRequest)gson.fromJson(clientRequest, Request.class);
                    //if casting fails, the exception will be caught and an UNAUTHORIZED error will be reported
                    //safe to do because we expect the FIRST request to be a loginRequest
                    synchronized (users) {
                        if (users.get(loginRequest.getUsername()) == null) {
                            users.put(loginRequest.getUsername(), this);
                            Response loginSuccessful = new LoginResponse(loginRequest.getUsername());
                            this.username = loginRequest.getUsername();
                            sendResponse(loginSuccessful);
                            isLoggedIn = true;
                        } else {
                            Response loginError = new ServerErrorResponse(ServerErrorResponse.Type.LOGIN_TAKEN, "Username taken");
                            sendResponse(loginError);
                        }
                    }
                } catch(Exception e) {
                    ServerErrorResponse error = new ServerErrorResponse(ServerErrorResponse.Type.UNAUTHORIZED,"Unauthorized. Please log in.");
                    sendResponse(error);
                }

            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects a user from the server
     */
    public synchronized void disconnect() {       
        running.set(false);
        try {
            socket.close();
            output.close();
            input.close();
        } catch (IOException e) {
        }
    }

    /**
     * Sends a response to the user
     * @param response the response to send
     */
    public synchronized void sendResponse(Response response) {
        output.println(gson.toJson(response,Response.class));
        output.flush();
    }

    /**
     * Listens for client requests
     */
    public void listen() {
        try {
            String clientRequest;
            while (running.get() && (clientRequest = input.readLine()) != null) {
                Request r = gson.fromJson(clientRequest, Request.class);
                try {
                    requestQueue.put(r);
                } catch (InterruptedException e) {
                }
            }            
        } catch (IOException e) {
            System.out.println("User " + username + " disconnected");
        }    
    }

}
