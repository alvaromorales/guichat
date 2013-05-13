package server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import protocol.Registration.*;
import protocol.Request;
import protocol.RoomRequest.*;
import protocol.RoomResponse.*;
import protocol.RoomResponse.LeftRoomResponse;
import protocol.StopServer;

/**
 * Represents a RequestHandler thread
 */
public class RequestHandler implements Runnable {
    private Map<String,ChatRoom> chatRooms;
    private Map<String,User> usersMap;
    private BlockingQueue<Request> requestQueue;
    private AtomicBoolean running;
    private RequestHandlerVisitor visitor;

    class RequestHandlerVisitor implements Visitor<Void> {
        /**
         * Creates a new RequestHandlerVisitor object
         */
        public RequestHandlerVisitor() {

        }

        /**
         * Removes a User from the server
         */
        @Override
        public synchronized Void visit(LogoutRequest request) {
            usersMap.get(request.getUsername()).disconnect();
            synchronized (usersMap) {
                usersMap.remove(request.getUsername());
            }
            return null;
        }

        /**
         * Stops the request handler thread
         * @param request the StopServer request issued by the server
         */
        @Override
        public synchronized Void visit(StopServer request) {
            running.set(false);
            for (User u: usersMap.values()) {
                u.disconnect();
            }
            return null;
        }

        /**
         * Adds a user to a room. If the room doesn't exist, it creates it
         */
        @Override
        public synchronized Void visit(JoinOrCreateRoomRequest request) {
            if (chatRooms.get(request.getRoomName()) != null) {
                // add user to room
                chatRooms.get(request.getRoomName()).addUser(request.getUsername());
            } else {
                // create a room and add user to it
                chatRooms.put(request.getRoomName(), new ChatRoom(request.getRoomName(), usersMap));
                chatRooms.get(request.getRoomName()).addUser(request.getUsername());
            }

            // send confirmation
            usersMap.get(request.getUsername()).sendResponse(new JoinedRoomResponse(request.getRoomName()));
            return null;
        }

        /**
         * Removes a user from a room. If the room is left empty, the room is deleted.
         */
        @Override
        public synchronized Void visit(LeaveRoomRequest request) {
            ChatRoom room = chatRooms.get(request.getRoomName());
            room.removeUser(request.getUsername());

            synchronized (chatRooms) {
                if (room.isEmpty()) {
                    chatRooms.remove(request.getRoomName());
                }
            }

            // send confirmation
            usersMap.get(request.getUsername()).sendResponse(new LeftRoomResponse(request.getRoomName()));
            return null;
        }
    }

    /**
     * Creates a new RequestHandler object
     * @param requestQueue the blocking queue of requests
     * @param users the map of users connected to the server
     */
    public RequestHandler(BlockingQueue<Request> requestQueue, Map<String,User> users) {
        this.chatRooms = Collections.synchronizedMap(new HashMap<String,ChatRoom>());
        this.requestQueue = requestQueue;
        this.usersMap = users;
        this.running = new AtomicBoolean(true);
        this.visitor = new RequestHandlerVisitor();
    }

    /**
     * Stops the request handler
     */
    public void stop() {
        requestQueue.offer(new StopServer());
    }

    /**
     * Runs the request handler
     */
    @Override
    public void run() {
        try {
            while (running.get()) {
                Request request = requestQueue.take();
                request.accept(this.visitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
