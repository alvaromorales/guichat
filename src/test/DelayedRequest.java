package test;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import protocol.Request;

public class DelayedRequest implements Request, Delayed {
    private Request request;
    private long delay;
    
    /**
     * Creates a new DelayedRequest object
     * @param request the request
     * @param delay, the delay in milliseconds
     */
    public DelayedRequest(Request request, long delay) {
        this.request = request;
        this.delay = delay;
    }
    
    /**
     * Compares a DelayedRequest object with another DelayedRequest object
     * TODO
     */
    @Override
    public int compareTo(Delayed arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Gets the request's delay
     * @return the request's delay
     */
    @Override
    public long getDelay(TimeUnit arg0) {
        return delay;
    }
    
    /**
     * Gets the request
     * @return the request
     */
    public Request getRequest() {
        return request;
    }

}
