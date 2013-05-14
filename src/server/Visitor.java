package server;

import protocol.Registration.*;
import protocol.GetListOfAvaiableRoomsRequest;
import protocol.RoomRequest.GetUsersInRoomRequest;
import protocol.RoomRequest.JoinOrCreateRoomRequest;
import protocol.RoomRequest.LeaveRoomRequest;
import protocol.SendMessageRequest;
import protocol.StopServer;

public interface Visitor<E> {
    public E visit(LogoutRequest request);
    public E visit(StopServer request);
    public E visit(JoinOrCreateRoomRequest request);
    public E visit(LeaveRoomRequest request);
    public E visit(GetUsersInRoomRequest request);
    public E visit(SendMessageRequest request);
    public E visit(GetListOfAvaiableRoomsRequest getListOfAvaiableRoomsRequest);
}