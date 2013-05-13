package protocol;

import client.ClientVisitor;

/**
 * This is a marker interface for Response objects.
 */
public interface Response {
    public <E> E accept(ClientVisitor<E> v);
}
