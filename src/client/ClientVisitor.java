package client;

import protocol.Registration.LoginResponse;
import protocol.RoomResponse.JoinedRoomResponse;
import protocol.RoomResponse.LeftRoomResponse;
import protocol.ServerErrorResponse;

public interface ClientVisitor<E> {
    public E visit(LoginResponse response);
    public E visit(JoinedRoomResponse response);
    public E visit(LeftRoomResponse response);
    public E visit(ServerErrorResponse response);
}