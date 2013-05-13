package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import protocol.Response;

public class ChatRoom {
    private Map<String,User> usersMap;
    private List<String> usersInRoom;
    private String name;
 
    /**
     * Creates a new ChatRoom object
     * @param name the name of the chat room
     */
    public ChatRoom(String name, Map<String,User> usersMap) {
        this.name = name;
        this.usersMap = usersMap;
        this.usersInRoom = Collections.synchronizedList(new ArrayList<String>(0)); 
    }

    /**
     * Gets the name of the chat room
     * @return the name of the chat room
     */
    public String getName() {
        return name;
    }
    
    /**
     * Broadcasts a response to the users in the chat room
     * @param response the response to broadcast
     */
    public synchronized void broadcastResponse(Response response) {
        synchronized(usersInRoom) {
            for (String username: usersInRoom) {
                usersMap.get(username).sendResponse(response);
            }
        }
    }
    
    /**
     * Adds a user to the room
     * @param username the user to add to the room
     */
    public synchronized void addUser(String username) {
        synchronized (usersInRoom) {
            usersInRoom.add(username);
        }
    }
    
    /**
     * Removes a user from the room
     * @param username the username to remove
     */
    public synchronized void removeUser(String username) {
        synchronized (usersInRoom) {
            usersInRoom.remove(username);
        }
    }
    
    /**
     * Checks if a room is empty
     * @return true if empty, else false
     */
    public synchronized boolean isEmpty() {
        synchronized (usersInRoom) {
            return usersInRoom.size() == 0;
        }
    }
}
