package protocol;
import server.Visitor;


/**
 * This is a marker interface for various types of request objects.
 */
public interface Request {
    public <E> E accept(Visitor<E> v);
}
