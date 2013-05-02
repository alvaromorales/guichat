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

    public String getMessage() {
        return this.message;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDestination() {
        return this.destination;
    }

    @Override
    public String toString() {
        return "<User: " + this.username + 
               "; Conversation: " + this.destination +
               "; Message: " + this.message + ">";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (char c: this.message.toCharArray()) hash = hash*31 + c;
        for (char c: this.username.toCharArray()) hash = hash*31 + c;
        return hash;
    }
}
