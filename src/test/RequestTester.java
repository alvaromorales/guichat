package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.DelayQueue;

import com.google.gson.Gson;
import protocol.Login;
import protocol.Request;
import protocol.Response;

/**
 * Class to help test requests and responses from the server
 * Simulates a connected client
 */
public class RequestTester implements Runnable {
    private final String SERVER_NAME = "localhost";
    private final int SERVER_PORT = 4444;
    private BufferedReader input;
    private PrintWriter output;    
    private Gson requestGson;
    private Gson responseGson;
    private List<Response> responseList;
    private Thread requestSender;
    private int expectedResponses;

    class RequestSender implements Runnable {
        DelayQueue<DelayedRequest> requestQueue;

        public RequestSender(DelayQueue<DelayedRequest> requestQueue) {
            this.requestQueue = requestQueue;
        }

        @Override
        public void run() {
            DelayedRequest request;
            while((request = requestQueue.poll()) != null) {
                sendRequest(request);
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
            //Attempt to connect to the chat server
            Socket socket = new Socket(SERVER_NAME, SERVER_PORT);

            input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            output = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));

            Request login = new Login(username);
            sendRequest(login);

            requestSender = new Thread(new RequestSender(requests));

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
        output.println(requestGson.toJson(r));
        output.flush();
    }

    /**
     * Listens for server responses
     */
    public void listen() {
        try {
            for (int i=0;i<expectedResponses;i++) {
                String serverResponse = input.readLine();
                if (serverResponse != null) {
                    Response r = responseGson.fromJson(serverResponse, Response.class);
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
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
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
