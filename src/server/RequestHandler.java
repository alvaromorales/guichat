package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import protocol.Registration.*;
import protocol.Request;
import protocol.RoomRequest.*;
import protocol.RoomResponse.*;
import protocol.AvailableChatRoomsResponse;
import protocol.GetListOfAvailableRoomsRequest;
import protocol.Response;
import protocol.SendMessageRequest;
import protocol.StopServer;
import protocol.UserJoinOrLeaveRoomResponse;
import protocol.UsersInRoomResponse;

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
            synchronized (chatRooms) {
                ChatRoom room = chatRooms.get(message.getRoomName());
                room.broadcastResponse(message);
                return null;
            }
        }

        /**
         * Gets all of the users in a room
         */
        @Override
        public synchronized Void visit(GetUsersInRoomRequest request) {
            User u = usersMap.get(request.getUsername());
            synchronized (chatRooms) {
                ChatRoom room = chatRooms.get(request.getRoomName());
                if (room == null) {
                    u.sendResponse(new UsersInRoomResponse(request.getRoomName(), new ArrayList<String>(0)));
                } else {
                    u.sendResponse(new UsersInRoomResponse(request.getRoomName(), room.getUsers()));
                }
                return null;
            }
        }

        /**
         * Removes a User from the server
         */
        @Override
        public synchronized Void visit(LogoutRequest request) {
            synchronized (usersMap) {
                usersMap.get(request.getUsername()).disconnect();
                usersMap.remove(request.getUsername());
            }
            
            synchronized (chatRooms) {
                for (ChatRoom room : chatRooms.values()) {
                    room.removeUser(request.getUsername());
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
                for (User u: usersMap.values()) {
                    u.disconnect();
                }
                return null;
            }
        }

        /**
         * Adds a user to a room. If the room doesn't exist, it creates it
         */
        @Override
        public synchronized Void visit(JoinOrCreateRoomRequest request) {
            synchronized (chatRooms) {
                if (chatRooms.get(request.getRoomName()) != null) {
                    // add user to room
                    chatRooms.get(request.getRoomName()).addUser(request.getUsername());
                } else {
                    // create a room and add user to it
                    chatRooms.put(request.getRoomName(), new ChatRoom(request.getRoomName(), usersMap));
                    chatRooms.get(request.getRoomName()).addUser(request.getUsername());
                    broadastResponse(new AvailableChatRoomsResponse(getAvailableChatRooms()));
                }

                // send confirmation
                usersMap.get(request.getUsername()).sendResponse(new JoinedRoomResponse(request.getRoomName(), chatRooms.get(request.getRoomName()).getUsers()));
                
                // notify users that the user joined
                chatRooms.get(request.getRoomName()).broadcastResponse(new UserJoinOrLeaveRoomResponse(request.getUsername(),request.getRoomName(),true), request.getUsername());
                return null;
            }
        }

        /**
         * Removes a user from a room. If the room is left empty, the room is deleted.
         */
        @Override
        public synchronized Void visit(LeaveRoomRequest request) {
            synchronized (chatRooms) {
                ChatRoom room = chatRooms.get(request.getRoomName());
                room.removeUser(request.getUsername());

                if (room.isEmpty()) {
                    chatRooms.remove(request.getRoomName());
                    broadastResponse(new AvailableChatRoomsResponse(getAvailableChatRooms()));
                } else {
                    room.broadcastResponse(new UserJoinOrLeaveRoomResponse(request.getUsername(), request.getRoomName(), false));
                }
            }

            return null;
        }

        /**
         * Sends a list of available rooms
         */
        @Override
        public Void visit(GetListOfAvailableRoomsRequest request) {
            usersMap.get(request.getUsername()).sendResponse(new AvailableChatRoomsResponse(getAvailableChatRooms()));
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
            for (ChatRoom room : chatRooms.values()) {
                rooms.add(room.getName());
            }
            
            return rooms;
        }
    }
    
    /**
     * Broadcasts a response to all connected users
     */
    public synchronized void broadastResponse(Response response) {
        synchronized (usersMap) {
            for (User u: usersMap.values()) {
                u.sendResponse(response);
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
                System.out.println("REQUEST: " + request);
                request.accept(this.visitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
