package protocol;

import java.util.List;

import client.ClientVisitor;

/**
 * Represents a response detailing the usernames of the connected users in a room
 */
public class UsersInRoomResponse implements Response {
    private String roomName;
    private List<String> users;

    /**
     * Creates a new UsersInRoomResponse object
     * @param roomName the name of the room
     * @param users a list of the usernames of the users in the room
     */
    public UsersInRoomResponse(String roomName, List<String> users) {
        this.roomName = roomName;
        this.users = users;
    }

    /**
     * Gets the name of the room
     * @return the name of the room
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Gets the list of usernames of users connected to the room
     * @return the list of usernames of users connected to the room
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * Accepts a ClientVisitor
     */
    @Override
    public <E> E accept(ClientVisitor<E> v) {
        return v.visit(this);
    }
    
    
}
