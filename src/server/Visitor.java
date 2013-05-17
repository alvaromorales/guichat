package server;

import protocol.Registration.*;
import protocol.AvailableRoomsRequest;
import protocol.RoomRequest.JoinOrCreateRoomRequest;
import protocol.RoomRequest.LeaveRoomRequest;
import protocol.SendMessageRequest;
import protocol.StopServer;

/**
 * Represents a Visitor for server requests
 */
public interface Visitor<E> {
    public E visit(LogoutRequest request);
    public E visit(StopServer request);
    public E visit(JoinOrCreateRoomRequest request);
    public E visit(LeaveRoomRequest request);
    public E visit(SendMessageRequest request);
    public E visit(AvailableRoomsRequest getListOfAvaiableRoomsRequest);
}