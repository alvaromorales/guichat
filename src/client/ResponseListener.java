package client;


import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import protocol.InterfaceAdapter;
import protocol.Message;
import protocol.Request;
import protocol.Response;

/**
 * Represents a ResponseListenerREsRessadfdfs thread
 */
public class ResponseListener implements Runnable {

    ChatSession session;
    Gson gson;

    /**
     * Creates a new ResponseListener object
     * @param session the user is part of
     */
    public ResponseListener(ChatSession session) {
        this.session = session;
        this.gson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).registerTypeAdapter(Message.class, new InterfaceAdapter<Message>()).create();
    }

    /**
     * Listens to the socket
     */
    @Override
    public void run() {
        listen();
    }

    /**
     * Listens for client responses
     * Deserealizes responses from json 
     * and adds them to the chat session 
     * blocking queue.
     */
    public void listen() {
        try {
            String serverResponse;
            while ((serverResponse = session.input.readLine()) != null) {
                Response r = gson.fromJson(serverResponse, Response.class);
                session.responseQueue.put(r);
            }            
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }
}