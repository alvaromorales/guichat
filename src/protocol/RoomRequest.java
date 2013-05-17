package protocol;

import server.Visitor;

/**
 * Represents a RoomChange response
 */
public class RoomRequest {
    private String username;
    private String roomName;

    /**
     * Creates a new RoomChange object
     * @param username the username of the user of the request/response
     * @param roomName the name of the room
     */
    public RoomRequest(String username, String roomName) {
        this.username = username;
        this.roomName = roomName;
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
     * Gets the string representation of the RoomRequest object
     * @return the string representation of the RoomRequest object
     */
    @Override
    public String toString() {
            return "username=" + username + ", roomName=" + roomName;
    }

    /**
     * Represents a request to join or create a room
     */
    public static class JoinOrCreateRoomRequest extends RoomRequest implements Request {

        /**
         * Creates a new JoinOrCreateRoomRequest request object
         * @param username the username of the user of the request/response
         * @param roomName the name of the room
         */
        public JoinOrCreateRoomRequest(String username, String roomName) {
            super(username, roomName);
        }

        /**
         * Accepts a visitor
         */
        @Override
        public <E> E accept(Visitor<E> v) {
            return v.visit(this);
        }

        /**
         * Gets the String representation of the JoinOrCreateRoomRequest object
         * @return the String representation of the JoinOrCreateRoomRequest object
         */
        @Override
        public String toString() {
            return "JoinOrCreateRoomRequest [" + super.toString() + "]";
        }
        
        /**
         * Checks if a JoinOrCreateRoomRequest object is equal to another object
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
            JoinOrCreateRoomRequest other = (JoinOrCreateRoomRequest) obj;
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
            return true;
        }

    }
    
    /**
     * Represents a request to leave a room
     */
    public static class LeaveRoomRequest extends RoomRequest implements Request {

        /**
         * Creates a new LeaveRoomRequest request object
         * @param username the username of the user of the request/response
         * @param roomName the name of the room
         */
        public LeaveRoomRequest(String username, String roomName) {
            super(username, roomName);
        }

        /**
         * Accepts a visitor
         */
        @Override
        public <E> E accept(Visitor<E> v) {
            return v.visit(this);
        }

        /**
         * Gets the String representation of the LeaveRoomRequest object
         * @return the String representation of the LeaveRoomRequest object
         */
        @Override
        public String toString() {
            return "LeaveRoomRequest [" + super.toString() + "]";
        }
        
        /**
         * Checks if a LeaveRoomRequest object is equal to another object
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
            LeaveRoomRequest other = (LeaveRoomRequest) obj;
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
            return true;
        }
    }

}
