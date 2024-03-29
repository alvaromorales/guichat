package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import protocol.Registration.*;
import protocol.Request;
import protocol.RoomRequest.*;
import protocol.RoomResponse.*;
import protocol.AvailableRoomsResponse;
import protocol.AvailableRoomsRequest;
import protocol.Response;
import protocol.SendMessageRequest;
import protocol.StopServer;
import protocol.UserJoinOrLeaveRoomResponse;

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
         * Sends Message to other users in room.
         */
        @Override
        public synchronized Void visit(SendMessageRequest message) {
            ChatRoom room = chatRooms.get(message.getRoomName());
            room.broadcastResponse(message);
            return null;
        }

        /**
         * Removes a User from the server
         */
        @Override
        public synchronized Void visit(LogoutRequest request) {
            usersMap.get(request.getUsername()).disconnect();
            usersMap.remove(request.getUsername());

            synchronized (chatRooms) {
                Iterator<ChatRoom> iterator = chatRooms.values().iterator();
                
                while(iterator.hasNext()) {
                    iterator.next().removeUser(request.getUsername());
                }

            }

            return null;
        }

        /**
         * Stops the request handler thread
         * @param request the StopServer request issued by the server
         */
        @Override
        public synchronized Void visit(StopServer request) {
            synchronized (usersMap) {
                running.set(false);
                Iterator<User> iterator = usersMap.values().iterator();
                
                while(iterator.hasNext()) {
                    iterator.next().disconnect();
                }

                return null;
            }
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
                broadastResponse(new AvailableRoomsResponse(getAvailableChatRooms()));
            }

            // send confirmation
            usersMap.get(request.getUsername()).sendResponse(new JoinedRoomResponse(request.getRoomName(), chatRooms.get(request.getRoomName()).getUsers()));

            // notify users that the user joined
            chatRooms.get(request.getRoomName()).broadcastResponse(new UserJoinOrLeaveRoomResponse(request.getUsername(),request.getRoomName(),true), request.getUsername());
            return null;
        }

        /**
         * Removes a user from a room. If the room is left empty, the room is deleted.
         */
        @Override
        public synchronized Void visit(LeaveRoomRequest request) {
            ChatRoom room = chatRooms.get(request.getRoomName());
            room.removeUser(request.getUsername());

            if (room.isEmpty()) {
                chatRooms.remove(request.getRoomName());
                broadastResponse(new AvailableRoomsResponse(getAvailableChatRooms()));
            } else {
                room.broadcastResponse(new UserJoinOrLeaveRoomResponse(request.getUsername(), request.getRoomName(), false));
            }

            return null;
        }

        /**
         * Sends a list of available rooms
         */
        @Override
        public Void visit(AvailableRoomsRequest request) {
            usersMap.get(request.getUsername()).sendResponse(new AvailableRoomsResponse(getAvailableChatRooms()));
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
     * Inserts a 'poison pill' into the request queue
     * Only ever called by the server
     */
    public void stop() {
        requestQueue.offer(new StopServer());
    }

    /**
     * Gets a list of names of available chat rooms
     * @return a list of names of available chat rooms
     */
    public synchronized List<String> getAvailableChatRooms() {
        synchronized (chatRooms) {
            List<String> rooms = new ArrayList<String>();
            Iterator<ChatRoom> iterator = chatRooms.values().iterator();
            
            while(iterator.hasNext()) {
                rooms.add(iterator.next().getName());
            }
            
            return rooms;
        }
    }

    /**
     * Broadcasts a response to all connected users
     */
    public synchronized void broadastResponse(Response response) {
        synchronized (usersMap) {
            Iterator<User> iterator = usersMap.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().sendResponse(response);
            }
        }
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
