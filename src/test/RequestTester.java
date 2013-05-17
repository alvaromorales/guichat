package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import protocol.InterfaceAdapter;
import protocol.Message;
import protocol.Registration;
import protocol.Request;
import protocol.Response;

/**
 * Class to help test requests and responses from the server
 * Simulates a connected client
 */
public class RequestTester implements Runnable {
    private final String SERVER_NAME = "localhost";
    private final long MAX_WAIT = 10000;
    private final int SERVER_PORT = 4444;
    private BufferedReader input;
    private PrintWriter output;    
    private Socket socket;
    private Gson gson;
    private List<Response> responseList;
    private Thread requestSender;
    private int expectedResponses;
    private String username;

    /**
     * Class to send requests
     */
    class RequestSender implements Runnable {
        DelayQueue<DelayedRequest> requestQueue;

        public RequestSender(DelayQueue<DelayedRequest> requestQueue) {
            this.requestQueue = requestQueue;
        }

        @Override
        public void run() {
            DelayedRequest request;
            try {
                while(!requestQueue.isEmpty() && (request = requestQueue.poll(MAX_WAIT,TimeUnit.MILLISECONDS)) != null) {
                    sendRequest(request.getRequest());
                }
            } catch (InterruptedException e) {
                System.out.println("Request sender TIMED OUT");
            }
        }

    }

    /**
     * Creates a new RequestTester object
     * @param username the username to log in to the server
     * @param requests the requests to send
     * @param expectedResponses the number of expected responses from the server
     */
    public RequestTester(String username, DelayQueue<DelayedRequest> requests, int expectedResponses) {
        try {
            this.expectedResponses = expectedResponses;
            this.gson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).registerTypeAdapter(Message.class, new InterfaceAdapter<Message>()).create();
            this.responseList = new ArrayList<Response>();
            this.username = username;

            //Attempt to connect to the chat server
            this.socket = new Socket(SERVER_NAME, SERVER_PORT);

            input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            output = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));

            Request login = new Registration.LoginRequest(username);

            DelayQueue<DelayedRequest> allRequests = new DelayQueue<DelayedRequest>();
            allRequests.add(new DelayedRequest(login, 0));
            allRequests.addAll(requests);

            requestSender = new Thread(new RequestSender(allRequests));
        } catch (IOException e) {
            //Failure to connect
            e.printStackTrace();
            System.out.println("Failed to connect to chat server at " + SERVER_NAME + ":" + SERVER_PORT + " or broken socket.");
            System.exit(-1);
        }
    }

    /**
     * Sends a request to the server
     * @param r the request to send
     */
    public void sendRequest(Request r) {
        output.println(gson.toJson(r,Request.class));
        output.flush();
    }

    /**
     * Listens for server responses
     */
    public synchronized void listen() {
        try {
            for (int i=0;i<expectedResponses;i++) {
                String serverResponse = input.readLine();
                if (serverResponse != null) {
                    Response r = gson.fromJson(serverResponse, Response.class);
                    responseList.add(r);
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        requestSender.start();
        listen();
        try {
            requestSender.join();
            sendRequest(new Registration.LogoutRequest(username));
        } catch (InterruptedException e) {
            System.out.println("Socket closed");
            e.printStackTrace();
        }
    }

    /**
     * Gets the list of server responses
     * @param responseList the list of server responses
     */
    public List<Response> getResponseList() {
        return responseList;
    }

}
