package server;

import protocol.Registration.*;
import protocol.StopServer;

public interface Visitor<E> {
    public E visit(LogoutRequest request);
    public E visit(StopServer request);
}