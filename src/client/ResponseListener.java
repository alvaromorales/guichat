package client;


import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import protocol.InterfaceAdapter;
import protocol.Request;
import protocol.Response;

public class ResponseListener implements Runnable {

    BufferedReader input;
    Gson gson;

    public ResponseListener(BufferedReader input) {
        this.input = input;
        this.gson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).create();
    }

    @Override
    public void run() {
        listen();
    }

    /**
     * Listens for client requests
     */
    public void listen() {
        try {
            String serverResponse;
            while ((serverResponse = input.readLine()) != null) {
                Response r = gson.fromJson(serverResponse, Response.class);
                //TODO add response to blocking queue
            }            
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}