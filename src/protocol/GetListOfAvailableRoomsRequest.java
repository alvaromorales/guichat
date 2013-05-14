package protocol;

import server.Visitor;

/**
 * Represents a GetListOfAvaibleRoomsRequest
 */
public class GetListOfAvailableRoomsRequest implements Request {
    private String username;

    /**
     * Creates a new GetListOfAvaibleRoomsRequest object
     * @param username the username of the user of the request/response
     * @param roomName the name of the room
     */
    public GetListOfAvailableRoomsRequest(String username) {
        this.username = username;
    }

    /**
     * Gets the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Gets the string representation of the GetListOfAvaibleRoomsRequest object
     * @return the string representation of the GetListOfAvaibleRoomsRequest object
     */
    @Override
    public String toString() {
            return "username=" + username;
    }

    /**
     * Accepts a visitor
     */
    @Override
    public <E> E accept(Visitor<E> v) {
        return v.visit(this);
    }

    /**
     * Checks if a GetListOfAvaibleRoomsRequest object is equal to another object
     * @param obj the object to compare to
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GetListOfAvailableRoomsRequest other = (GetListOfAvailableRoomsRequest) obj;
        if (getUsername() == null) {
            if (other.getUsername() != null)
                return false;
        } else if (!getUsername().equals(other.getUsername()))
            return false;
        return true;
    }
}
