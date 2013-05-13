package client;

import protocol.Registration.LoginResponse;
import protocol.RoomResponse.JoinedRoomResponse;
import protocol.RoomResponse.LeftRoomResponse;
import protocol.SendMessageRequest;
import protocol.ServerErrorResponse;
import protocol.UserJoinOrLeaveRoomResponse;
import protocol.UsersInRoomResponse;

public interface ClientVisitor<E> {
    public E visit(LoginResponse response);
    public E visit(JoinedRoomResponse response);
    public E visit(LeftRoomResponse response);
    public E visit(ServerErrorResponse response);
    public E visit(SendMessageRequest response);
    public E visit(UsersInRoomResponse usersInRoomResponse);
    public E visit(UserJoinOrLeaveRoomResponse userJoinOrLeaveRoomResponse);
}