package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import protocol.InterfaceAdapter;
import protocol.Message;
import protocol.Registration;
import protocol.Request;
import protocol.Response;

/**
 * Creates a chat session with the server
 */
public class ChatSession {
    PrintWriter output;
    BufferedReader input;
    List<ChatWindow> chatWindows;
    List<String> avaibleChatRooms;
    BlockingQueue<Response> responseQueue;
    Thread responseListener;
    Thread responseHandler;
    Gson requestGson;
    String username;
    ChatGUI gui;

    public ChatSession(Socket socket, ChatGUI gui) {
        try {
            this.gui = gui;
            this.requestGson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).create();
            this.output = new PrintWriter(
                              new OutputStreamWriter(
                                socket.getOutputStream()));
            this.input = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()));
            this.chatWindows = Collections.synchronizedList(new ArrayList<ChatWindow>());
            //create and run the responseHandler thread
            this.responseHandler = new Thread(new ResponseHandler(this));
            this.responseHandler.start();
            //create and run the responseListener thread
            this.responseListener = new Thread(new ResponseListener(this));
            this.responseListener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }

    /**
     * Sends a request to the user
     * @param request the request to send
     */
    private void sendRequest(Request request) {
        output.println(requestGson.toJson(request,Request.class));
        output.flush();
    }

    public void sendMessage(ChatWindow c, Message m) {
        //TODO
    }

    public  void createChatWindow(String nameOfRoom) {
        //TODO
    }

    public  void joinChatWindow(String nameOfString) {
        //TODO
    }

    public  void getUsersInChatWindow(ChatWindow cur) {
        //TODO
    }

    public void closeChatWindow(ChatWindow c) {
        //TODO
    }

    public void saveConversation(ChatWindow cur) {
        //TODO
    }

    public String[] getAvailableChatRooms() {
        return avaibleChatRooms.toArray(new String[avaibleChatRooms.size()]);
    }

    public void sendLoginRequest() {
        //create/send login request
        Request loginRequest = new Registration.LoginRequest(gui.username);
        sendRequest(loginRequest);
    }   

    public void logout() {
        //TODO
    }
}