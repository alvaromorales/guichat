package server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.Registration.*;
import protocol.Request;
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
            return null;
        }

        /**
         * Stops the request handler thread
         * @param request the StopServer request issues by the server
         */
        @Override
        public synchronized Void visit(StopServer request) {
            running.set(false);
            synchronized (usersMap) {
                for (User u: usersMap.values()) {
                    u.disconnect();
                }
            }
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
    
    @Override
    public void run() {
        try {
            while (running.get()) {
                Request request = requestQueue.take();
                request.accept(this.visitor);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
