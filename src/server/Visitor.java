package server;

import protocol.Registration.*;
import protocol.RoomRequest.JoinOrCreateRoomRequest;
import protocol.RoomRequest.LeaveRoomRequest;
import protocol.StopServer;

public interface Visitor<E> {
    public E visit(LogoutRequest request);
    public E visit(StopServer request);
    public E visit(JoinOrCreateRoomRequest request);
    public E visit(LeaveRoomRequest request);
}