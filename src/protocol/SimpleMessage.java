package protocol;

import java.sql.Timestamp;

public class SimpleMessage implements Message {
    private final String message;
    private final Timestamp timestamp;
    private final String username;
    private final String conversation;

    public SimpleMessage(String message, 
                         Timestamp timestamp, 
                         String username,
                         String conversation) {
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
        this.conversation = conversation;
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

    public String getConversation() {
        return this.conversation;
    }
    
    @Override
    public String toString() {
        return "<User: " + this.username + 
               "; Message: " + this.message + ">";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (char c: this.message.toCharArray()) hash = hash*31 + c;
        for (char c: this.username.toCharArray()) hash = hash*31 + c;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        //check edges
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof SimpleMessage)) return false;
        
        SimpleMessage that = (SimpleMessage) obj;
        //just check that the name's are equal. name's unique for a ChatWindow.
        if (this.message.equals(that.message) && 
            this.timestamp.equals(that.timestamp) &&
            this.username.equals(that.username) &&
            this.conversation.equals(that.conversation)) {
            return true;
        }
        //else return false
        return false;
    }
}
