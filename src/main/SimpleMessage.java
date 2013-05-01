package main;

import java.sql.Timestamp;

class SimpleMessage implements Message {
    private final String message;
    private final Timestamp timestamp;
    private final String username;
    private final String destination; //<--this will need to be changed. type most likely wrong.

    public SimpleMessage(String message, 
                         Timestamp timestamp, 
                         String username, 
                         String destination) {
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
        this.destination = destination;
    }

    private String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.message;
    }

    @Override
    public int hashCode() {
        //TODO
    }
}
