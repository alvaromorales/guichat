package protocol;

import client.ClientVisitor;

/**
 * Represents a UserJoinOrLeaveRoomResponse
 */
public class UserJoinOrLeaveRoomResponse implements Response {
    private String username;
    private String roomName;
    private boolean joining;

    /**
     * Creates a new UserJoinOrLeaveRoomResponse object
     * @param username the username of the user of the request/response
     * @param roomName the name of the room
     */
    public UserJoinOrLeaveRoomResponse(String username, String roomName, boolean joining) {
        this.username = username;
        this.roomName = roomName;
        this.joining = joining;
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
    public boolean isJoining() {
        return joining;
    }
    
    /**
     * Gets the string representation of the UserJoinOrLeaveRoomResponse object
     * @return the string representation of the UserJoinOrLeaveRoomResponse object
     */
    @Override
    public String toString() {
            return "username=" + username + 
                   ", roomName=" + roomName +
                   ", joining=" + joining;
    }

    /**
     * Accepts a visitor
     */
    @Override
    public <E> E accept(ClientVisitor<E> v) {
        return v.visit(this);
    }

    /**
     * Checks if a UserJoinOrLeaveRoomResponse object is equal to another object
     * @param obj the object to compare to
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UserJoinOrLeaveRoomResponse other = (UserJoinOrLeaveRoomResponse) obj;
        if (getRoomName() == null) {
            if (other.getRoomName() != null)
                return false;
        } else if (!getRoomName().equals(other.getRoomName()))
            return false;
        if (getUsername() == null) {
            if (other.getUsername() != null)
                return false;
        } else if (!getUsername().equals(other.getUsername()))
            return false;
        if (isJoining() != other.isJoining()) {
            return false;
        }
        return true;
    }
}
