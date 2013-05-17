package protocol;

import client.ClientVisitor;
import server.Visitor;

/**
 * Represents a SendMessageRequest
 */
public class SendMessageRequest implements Request, Response {
    private String username;
    private String roomName;
    private Message message;

    /**
     * Creates a new SendMessageRequest object
     * @param username the username of the user of the request/response
     * @param roomName the name of the room
     */
    public SendMessageRequest(String username, String roomName, Message message) {
        this.username = username;
        this.roomName = roomName;
        this.message = message;
    }

    /**
     * Gets the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the room name
     * @return the room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Gets the message
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Gets the string representation of the SendMessageRequest object
     * @return the string representation of the SendMessageRequest object
     */
    @Override
    public String toString() {
        return "username=" + username + 
                ", roomName=" + roomName +
                ", message=" + message;
    }

    /**
     * Accepts a visitor
     */
    @Override
    public <E> E accept(Visitor<E> v) {
        return v.visit(this);
    }

    /**
     * Checks if a SendMessageRequest object is equal to another object
     * @param obj the object to compare to
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SendMessageRequest other = (SendMessageRequest) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (roomName == null) {
            if (other.roomName != null)
                return false;
        } else if (!roomName.equals(other.roomName))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    /**
     * Accepts a visitor
     */
    @Override
    public <E> E accept(ClientVisitor<E> v) {
        return v.visit(this);
    }

}
