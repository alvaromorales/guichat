package protocol;

import java.util.List;

import client.ClientVisitor;

/**
 * Represents a RoomResponse response
 */
public class RoomResponse {
    private String roomName;

    /**
     * Creates a new RoomResponse object
     * @param roomName the name of the room
     */
    public RoomResponse(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Gets the room name
     * @return the room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Gets the String representation of the RoomResponse
     */
    @Override
    public String toString() {
        return "roomName=" + roomName;
    }

    public static class JoinedRoomResponse extends RoomResponse implements Response {
        private List<String> usersInRoom;

        /**
         * Creates a new JoinedRoomResponse object
         * @param roomName the name of the room
         */
        public JoinedRoomResponse(String roomName, List<String> usersInRoom) {
            super(roomName);
            this.usersInRoom = usersInRoom;
        }

        /**
         * Checks that a JoinedRoomResponse is equal to another object
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
            JoinedRoomResponse other = (JoinedRoomResponse) obj;
            if (usersInRoom == null) {
                if (other.usersInRoom != null)
                    return false;
            } else if (!usersInRoom.equals(other.usersInRoom))
                return false;
            
            if (getRoomName() == null) {
                if (other.getRoomName() != null)
                    return false;
            } else if (!getRoomName().equals(other.getRoomName()))
                return false;
            
            return true;
        }

        /**
         * Gets the list of users in the room
         * @return the list of users in the room
         */
        public List<String> getUsersInRoom() {
            return usersInRoom;
        }

        /**
         * Gets the String representation of a JoinedRoomResponse object
         * @return the String representation of a JoinedRoomResponse object
         */
        @Override
        public String toString() {
            return "JoinedRoomResponse [" + super.toString() + ", users=" + usersInRoom.toString() + "]";
        }

        @Override
        public <E> E accept(ClientVisitor<E> v) {
            return v.visit(this);
        }

    }

    public static class LeftRoomResponse extends RoomResponse implements Response {

        /**
         * Creates a new LeftRoomResponse object
         * @param roomName the name of the room
         */
        public LeftRoomResponse(String roomName) {
            super(roomName);
        }

        /**
         * Checks if a LeftRoomResponse object is equal to another object
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
            LeftRoomResponse other = (LeftRoomResponse) obj;
            if (getRoomName() == null) {
                if (other.getRoomName() != null)
                    return false;
            } else if (!getRoomName().equals(other.getRoomName()))
                return false;
            return true;
        }

        /**
         * Gets the String representation of a LeftRoomResponse object
         * @return the String representation of a LeftRoomResponse object
         */
        @Override
        public String toString() {
            return "LeftRoomResponse [" + super.toString() + "]";
        }

        /**
         * Accepts a visitor
         */
        @Override
        public <E> E accept(ClientVisitor<E> v) {
            return v.visit(this);
        }
    }


}
