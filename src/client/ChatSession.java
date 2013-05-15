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
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import protocol.AvailableRoomsRequest;
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
    List<String> availableChatRooms;
    BlockingQueue<Response> responseQueue;
    Thread responseListener;
    Thread responseHandler;
    Gson requestGson;
    String username = "";
    ChatGUI gui;

    /**
     * Constructs a ChatSession
     * Spawns two threads: one for listening
     * for responses and one for handling responses
     *
     * @param socket
     * @param gui
     */
    public ChatSession(Socket socket, ChatGUI gui) {
        try {
            this.responseQueue = new LinkedBlockingQueue<Response>();
            this.gui = gui;
            this.requestGson = new GsonBuilder().registerTypeAdapter(Request.class, new InterfaceAdapter<Request>()).registerTypeAdapter(Response.class, new InterfaceAdapter<Response>()).registerTypeAdapter(Message.class, new InterfaceAdapter<Message>()).create();
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

    /**
     * Setter for username
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Adds a ChatWindow to the ChatSession.
     * Checks if the window was previously active
     * and if it was carries the history over.
     *
     * GUI is updated to reflect the newly joined room.
     *
     * @param name: name of chat window
     */
    public synchronized void addChatWindow(ChatWindow c) {

        if (prevChatWindows.containsKey(c.getName())) {
            ChatWindow oldC = prevChatWindows.remove(c.getName());
            oldC.setUsers(c.getUsers());
            activeChatWindows.put(c.getName(), oldC);
            //adjust the table to reflect the new ChatWindow
            synchronized(gui.getTableModelLock()) {
                //locate the correct row
                for (int row = 0; row < gui.chatWindowsTableModel.getRowCount(); row++) {
                    if (((String) gui.chatWindowsTableModel.getValueAt(row, 1)).equals(oldC.getName())) {
                        gui.chatWindowsTableModel.setValueAt(oldC.getUsers().size(),row,2);
                        gui.chatWindowsTableModel.setValueAt("Yes",row,0);
                        break;
                    }
                }
            }
        } else {
            activeChatWindows.put(c.getName(), c);
            //adjust the table to reflect the new ChatWindow
            synchronized(gui.getTableModelLock()) {
                gui.chatWindowsTableModel.addRow(new String[] {"Yes",
                                                               c.getName(),
                                                               c.getUsers().size() + "",
                                                               c.getUnreadCount() + ""});
            }
        }
        gui.reload(c.getName());
    }

    /**
     * Removes the ChatWindow from the ChatSession.
     * Adds it to prevChatWindows.
     *
     * GUI is updated to reflect the newly removed room.
     *
     * @param name: name of chat window
     */
    public synchronized void removeChatWindow(ChatWindow c) {
        c.removeUser(username, this.gui);
        prevChatWindows.put(c.getName(), activeChatWindows.remove(c.getName()));
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

    /**
     * Getter for activeChatWindows
     *
     * @return activeChatWindows
     */
    public Map<String, ChatWindow> getActiveChatWindows() {
        return activeChatWindows;
    }
    
    /**
     * Getter for prevChatWindows
     *
     * @return prevChatWindows
     */
    public Map<String, ChatWindow> getPrevChatWindows() {
        return prevChatWindows;
    }

    /**
     * Getter for a specific activeChatWindow
     *
     * @param name of room to get
     * @return specific chatwindow
     */
    public ChatWindow getActiveChatWindow(String name) {
        return activeChatWindows.get(name);
    } 
    
    /**
     * Getter for a specific prevChatWindow
     *
     * @param name of room to get
     * @return specific chatwindow
     */
    public ChatWindow getPrevChatWindow(String name) {
        return prevChatWindows.get(name);
    }

    /**
     * Getter for a username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for avaibleChatRooms
     *
     * @param list of avaibleChatRooms
     */
    public void setAvailableChatRooms(List<String> avaibleChatRooms) {
        this.availableChatRooms = avaibleChatRooms;
    }

    /**
     * Sends a request to the user
     * @param request the request to send
     */
    private void sendRequest(Request request) {
        output.println(requestGson.toJson(request,Request.class));
        output.flush();
    }

    /**
     * Adds a message to a specific ChatWindow and
     * sends a request to the server to distribute
     * the message to the other users in the room.
     *
     * @param message
     * @param chatwindow to add message to
     */
    public void sendMessage(ChatWindow c, Message m) {
        //add message to the chatwindow and gui window
        c.addMessage(m, gui);
        Request sendMessageRequest = new SendMessageRequest(gui.username, c.getName(), m);
        sendRequest(sendMessageRequest);
    }

    /**
     * Sends a request to the server to see
     * if the user can create the given chatwindow.
     *
     * @param nameOfRoom to add
     */
    public  void createChatWindow(String nameOfRoom) {
        Request createRoomRequest = new RoomRequest.JoinOrCreateRoomRequest(gui.username, nameOfRoom);
        sendRequest(createRoomRequest);
    }

    /**
     * Sends a request to the server to see
     * if the user can join the given chatwindow.
     *
     * @param nameOfRoom to join
     */
    public  void joinChatWindow(String nameOfRoom) {
        Request createRoomRequest = new RoomRequest.JoinOrCreateRoomRequest(gui.username, nameOfRoom);
        sendRequest(createRoomRequest);
    }

    /**
     * Writes the names of all of users in the
     * given room to chatTextArea
     *
     * @param current ChatWindow
     */
    public  void getUsersInChatWindow(ChatWindow cur) {
        //possible concurrency issues here
        //change when you get a chance
        List<String> users = cur.getUsers();
        gui.writeToWindow("There are " + users.size() + " users in the room.\n");
        for(String user: users) {
            gui.writeToWindow(user+"\n");
        }
    }
    
    /**
     * Retrieves the history of a given room
     *
     * @param nameOfRoom to get history of
     */
    public  void viewHistory(String nameOfRoom) {
        if (activeChatWindows.containsKey(nameOfRoom)){
            ChatWindow c = activeChatWindows.get(nameOfRoom);
            gui.writeToHistoryWindow(c.getMessages());
        }
        else if (prevChatWindows.containsKey(nameOfRoom)){
            ChatWindow c = prevChatWindows.get(nameOfRoom);
            gui.writeToHistoryWindow(c.getMessages());
        }
        else{
            //gui.writeToHistoryWindow(null);
        }
    }

    /**
     * Sends a request to the server to alert
     * it that user is leave room. Then removes the 
     * window from the active windows.
     *
     * @param ChatWindow to close
     */
    public void closeChatWindow(ChatWindow c) {
        Request leaveRoomRequest = new RoomRequest.LeaveRoomRequest(gui.username, c.getName());
        sendRequest(leaveRoomRequest);
        removeChatWindow(c);
    }

    /**
     * Saves the conversation of the specified ChatWindow
     * to a file.
     *
     * @param ChatWindow to save history for
     */
    public void saveConversation(ChatWindow cur) {
        cur.saveConversation();
        gui.writeToWindow("System Message: Save message to file system.");
    }

    /**
     * Gets the list of rooms user can join.
     *
     * @return array of rooms to join
     */
    public String[] getAvailableChatRooms() {
        return availableChatRooms.toArray(new String[availableChatRooms.size()]);
    }

    /**
     * Sends a login request to the server
     */
    public void sendLoginRequest() {
        //create/send login request
        Request loginRequest = new Registration.LoginRequest(gui.username);
        sendRequest(loginRequest);
    } 

    /**
     * Sends a request to the server to get
     * the available rooms
     */
    public void sendRequestForAvaibleRooms() {
        Request request = new AvailableRoomsRequest(username);
        sendRequest(request);
    }

    /**
     * Sends a request to the server notifying
     * it that the user is logging out
     */
    public void logout() {
        //create/send logout request
        Request logoutRequest = new Registration.LogoutRequest(gui.username);
        sendRequest(logoutRequest);
    }
}