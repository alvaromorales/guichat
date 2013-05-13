package client;

import protocol.Response;

/**
 * Represents a RequestHandler thread
 */
public class ResponseHandler implements Runnable {
    
    private ChatSession session;
    private ResponseHandlerVisitor visitor;

    class ResponseHandlerVisitor implements ClientVisitor<Void> {
        /**
         * Creates a new ResponseHandlerVisitor object
         */
        public ResponseHandlerVisitor() {
            
        }

        //TODO implement the various visit methods

    }
    
    /**
     * Creates a new RequestHandler object
     * @param requestQueue the blocking queue of requests
     * @param users the map of users connected to the server
     */
    public ResponseHandler(ChatSession session) {
        this.session = session;
        this.visitor = new ResponseHandlerVisitor();
    }
    
    /**
     * Stops the response handler
     */
    public void stop() {
        //TODO
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Response response = session.responseQueue.take();
                //TODO use vistor to process response
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
