package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import protocol.InterfaceAdapter;
import protocol.Message;
import protocol.Registration;
import protocol.Request;
import protocol.Response;
import protocol.RoomRequest;
import protocol.SendMessageRequest;

/**
 * Creates a chat session with the server
 */
public class ChatSession {
    PrintWriter output;
    BufferedReader input;
    Map<String, ChatWindow> activeChatWindows;
    Map<String, ChatWindow> prevChatWindows;
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
            this.activeChatWindows = Collections.synchronizedMap(new HashMap<String, ChatWindow>());
            this.prevChatWindows = Collections.synchronizedMap(new HashMap<String, ChatWindow>());
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

    public synchronized void addChatWindow(ChatWindow c) {
        activeChatWindows.put(c.getName(),c);
        if (prevChatWindows.containsKey(c.getName())){
            prevChatWindows.remove(c.getName());
        }
        //adjust the table to reflect the new ChatWindow
        synchronized(gui.getTableModelLock()) {
            gui.chatWindowsTableModel.addRow(new String[] {"Yes",
                                                           c.getName(),
                                                           c.getUsers().size(),
                                                           c.getUnreadCount()});
        }
    }

    public synchronized void removeChatWindow(ChatWindow c) {
        activeChatWindows.remove(c.getName());
        prevChatWindows.put(c.getName(), c);
        //adjust active indictor
        synchronized(gui.getTableModelLock()) {
            //locate the correct row
            for (int row = 0; row < gui.chatWindowsTableModel.getRowCount(); row++) {
                if (((String) gui.chatWindowsTableModel.getValueAt(row, 1)).equals(c.getName())) {
                    gui.chatWindowsTableModel.setValueAt("No",row,0);
                    break;
                }
            }
        }
    }

    public Map<String, ChatWindow> getActiveChatWindows() {
        return activeChatWindows;
    }
    
    public Map<String, ChatWindow> getPrevChatWindows() {
        return prevChatWindows;
    }

    public ChatWindow getActiveChatWindow(String name) {
        return activeChatWindows.get(name);
    } 
    
    public ChatWindow getPrevChatWindow(String name) {
        return prevChatWindows.get(name);
    }

    public String getUsername() {
        return username;
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
        //add message to the chatwindow and gui window
        c.addMessage(m, gui);
        Request createRoomRequest = new SendMessageRequest(gui.username, c.getName(), m);
        sendRequest(createRoomRequest);
    }

    public  void createChatWindow(String nameOfRoom) {
        Request createRoomRequest = new RoomRequest.JoinOrCreateRoomRequest(gui.username, nameOfRoom);
        sendRequest(createRoomRequest);
    }

    public  void joinChatWindow(String nameOfRoom) {
        Request createRoomRequest = new RoomRequest.JoinOrCreateRoomRequest(gui.username, nameOfRoom);
        sendRequest(createRoomRequest);
    }

    public  void getUsersInChatWindow(ChatWindow cur) {
        //possible concurrency issues here
        //change when you get a chance
        List<String> users = cur.getUsers();
        gui.writeToWindow("There are " + users.size() + "users in the room.");
        for(String user: users) {
            gui.writeToWindow(user);
        }
    }

    public void closeChatWindow(ChatWindow c) {
        //TODO modify the gui
        Request leaveRoomRequest = new RoomRequest.LeaveRoomRequest(gui.username, c.getName());
        sendRequest(leaveRoomRequest);
    }

    public void saveConversation(ChatWindow cur) {
        cur.saveConversation();
        gui.writeToWindow("System Message: Save message to file system.");
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
        //create/send logout request
        Request logoutRequest = new Registration.LogoutRequest(gui.username);
        sendRequest(logoutRequest);
    }
}