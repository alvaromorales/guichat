package protocol;

import java.util.ArrayList;
import java.util.List;

import client.ClientVisitor;

/**
 * Represents a request or response with the list of available chat rooms
 */
public class AvailableRoomsResponse implements Response {
    private List<String> rooms;
    
    /**
     * Creates a new AvailableChatRoomsResponse response object
     * @param rooms a list of the names of available chat rooms
     */
    public AvailableRoomsResponse(List<String> rooms) {
        this.rooms = rooms;
    }
    
    
    /**
     * Gets a list of the names of available chat rooms
     * @return a list of the names of available chat rooms
     */
    public List<String> getRooms() {
        return new ArrayList<String>(rooms);
    }

    /**
     * Accepts a visitor
     */
    @Override
    public <E> E accept(ClientVisitor<E> v) {
        return v.visit(this);
    }

    /**
     * Gets the String representation of an AvailableRoomsResponse object
     */
    @Override
    public String toString() {
        return "AvailableRoomsResponse [rooms=" + rooms + "]";
    }


    /**
     * Checks if an AvailableRoomsResponse is equal to another object
     * @param obj the object to compare to
     * @return true if equals, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AvailableRoomsResponse other = (AvailableRoomsResponse) obj;
        if (rooms == null) {
            if (other.rooms != null)
                return false;
        } else if (!rooms.equals(other.rooms))
            return false;
        return true;
    }
        
}
