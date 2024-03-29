package client;

import protocol.Registration.LoginResponse;
import protocol.AvailableRoomsResponse;
import protocol.Response;
import protocol.RoomResponse.JoinedRoomResponse;
import protocol.SendMessageRequest;
import protocol.ServerErrorResponse.Type;
import protocol.ServerErrorResponse;
import protocol.UserJoinOrLeaveRoomResponse;

/**
 * Represents a ResponseHandler thread
 */
public class ResponseHandler implements Runnable {
    
    private ChatSession session;
    private ResponseHandlerVisitor visitor;

    /**
     * Represents a ClientVisitor
     */
    class ResponseHandlerVisitor implements ClientVisitor<Void> {
        /**
         * Creates a new ResponseHandlerVisitor object
         */
        public ResponseHandlerVisitor() {
        }

        /**
         * Alert user of successful login
         */
        @Override
        public synchronized Void visit(LoginResponse response) {
            //given that we have received a loginresponse
            //we know that the client was successfully logged in
            session.gui.setIsConnected(true);
            session.gui.clearWindow();
            session.gui.writeToWindow("System Message: You have been successfully " +
                                      "logged in with the username " + response.getUsername() + ".\n");
            session.setUsername(response.getUsername());
            session.sendRequestForAvaibleRooms();
            return null;
        }

        /**
         * Alert user that they have successfully joined a room
         */
        @Override
        public Void visit(JoinedRoomResponse response) {
            //create chat window
            ChatWindow c = new ChatWindow(response.getRoomName());
            c.setUsers(response.getUsersInRoom());
            //add window to session
            session.addChatWindow(c);
            return null;
        }

        /**
         * Server error response.
         * Can result from unsuccessful login
         * Or unauthorized access attempt
         */
        @Override
        public Void visit(ServerErrorResponse response) {
            if (response.getType().equals(Type.LOGIN_TAKEN)) {
                session.gui.writeToWindow("System Message: Login failed. " +
                                          "The username you requested is taken.\n");
            } else if (response.getType().equals(Type.UNAUTHORIZED)) {
                session.gui.writeToWindow("System Message: " + response.getError());
            }
            return null;
        }

        /**
         * Alert user of new message
         */
        @Override
        public Void visit(SendMessageRequest response) {
            if (!response.getUsername().equals(session.getUsername())) { //ensure the message wasn't sent by you
                //add the message and render it in the gui if the 
                //chat window is the current window
                //otherwise adjust the table on the right of the gui
                session.getActiveChatWindows().get(response.getRoomName()).addMessage(response.getMessage(),session.gui);
            }
            return null;
        }

        /**
         * Alert user that another user has joined or left a room.
         */
        @Override
        public Void visit(UserJoinOrLeaveRoomResponse response) {
            ChatWindow c = session.getActiveChatWindows().get(response.getRoomName());
            String username = response.getUsername();
            if (response.isJoining()) { //joining room
                c.addUser(username, session.gui);
            } else { //exiting room
                c.removeUser(username, session.gui);
            }
            return null;
        }

        @Override
        public Void visit(AvailableRoomsResponse response) {
            session.setAvailableChatRooms(response.getRooms());
            return null;
        }
    }
    
    /**
     * Creates a new ResponseHandler object
     * @param session the user is part of
     */
    public ResponseHandler(ChatSession session) {
        this.session = session;
        this.visitor = new ResponseHandlerVisitor();
    }
    
    /**
     * Processes one Response at a time from the blocking queue
     */
    @Override
    public void run() {
        try {
            while (true) {
                Response response = session.responseQueue.take();
                response.accept(this.visitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
