package protocol;

import server.Visitor;

/**
 * Represents a StopServer request
 * This is a special request that is only ever issues by the server (never by the client)
 */
public class StopServer implements Request {

    /**
     * Creates a new StopServer request instance
     */
    public StopServer() {

    }

    /**
     * Accepts a visitor
     */
    @Override
    public <E> E accept(Visitor<E> v) {
        return v.visit(this);
    }
}