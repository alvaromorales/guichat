package test;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import protocol.Request;

/**
 * Represents a DelayedRequest to the server
 * Some code referenced from http://aredko.blogspot.com/2012/04/using-delayed-queues-in-practice.html
 */
public class DelayedRequest implements Delayed {
    private Request request;
    private final long delay;
    private final long origin;

    /**
     * Creates a new DelayedRequest object
     * @param request the request
     * @param delay, the delay in milliseconds
     */
    public DelayedRequest(Request request, long delay) {
        this.request = request;
        this.delay = delay;
        this.origin = System.currentTimeMillis();
    }

    /**
     * Compares a DelayedRequest object with another DelayedRequest object
     */
    @Override
    public int compareTo(Delayed delayed) {
        if(delayed == this) {
            return 0;
        }

        if(delayed instanceof DelayedRequest ) {
            long diff = delay - ((DelayedRequest)delayed).delay;
            return ((diff == 0) ? 0 : ((diff < 0) ? -1 : 1 ));
        }

        long d = (getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
        return (( d == 0) ? 0 : ( ( d < 0 ) ? -1 : 1));
    }

    /**
     * Gets the request's delay
     * @return the request's delay
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert( delay - ( System.currentTimeMillis() - origin ),
                TimeUnit.MILLISECONDS );
    }

    /**
     * Gets the request
     * @return the request
     */
    public Request getRequest() {
        return request;
    }

}
