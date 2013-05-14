package protocol;

import java.util.ArrayList;
import java.util.List;

import client.ClientVisitor;

/**
 * Represents a request or response with the list of available chat rooms
 */
public class AvailableChatRoomsResponse implements Response {
    private List<String> rooms;
    
    /**
     * Creates a new AvailableChatRoomsResponse response object
     * @param rooms a list of the names of available chat rooms
     */
    public AvailableChatRoomsResponse(List<String> rooms) {
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

}
