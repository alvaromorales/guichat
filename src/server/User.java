package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import com.google.gson.*;

import protocol.InterfaceAdapter;
import protocol.LoginRequest;
import protocol.LoginRequest;
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
    
    /**
     * Creates a new user
     * @param socket the user's socket
     */
    public User(Socket socket, BufferedReader input, PrintWriter output, BlockingQueue<Request> requestQueue) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.requestQueue = requestQueue;
        this.requestGson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).create();
        this.responseGson = new GsonBuilder().registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).create();
    }
    
    @Override
    public void run() {
        //login();
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
                //currently just prints to the console
                //will be route to a request handler in the future
                //System.out.println(clientRequest);
                
                //Do stuff
                
                try {
                    LoginRequest loginRequest = requestGson.fromJson(clientRequest, LoginRequest.class);
                    // check if the username is available, return a loginsuccessful message
                } catch(Exception e) {
                    ServerError error = new ServerError("Unauthorized. Please log in.");
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
                //currently just prints to the console
                //will be route to a request handler in the future
                System.out.println(clientRequest);
                
//                Request r = requestGson.fromJson(clientRequest, Request.class);
//                try {
//                    requestQueue.put(r);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

}
