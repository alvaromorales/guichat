package client;

import protocol.AvailableRoomsResponse;
import protocol.Registration.LoginResponse;
import protocol.RoomResponse.JoinedRoomResponse;
import protocol.SendMessageRequest;
import protocol.ServerErrorResponse;
import protocol.UserJoinOrLeaveRoomResponse;

/**
 * Represents a ClientVisitor interface
 */
public interface ClientVisitor<E> {
    public E visit(LoginResponse response);
    public E visit(JoinedRoomResponse response);
    public E visit(ServerErrorResponse response);
    public E visit(SendMessageRequest response);
    public E visit(UserJoinOrLeaveRoomResponse userJoinOrLeaveRoomResponse);
    public E visit(AvailableRoomsResponse availableChatRoomsResponse);
}