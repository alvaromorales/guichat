package protocol;

import java.sql.Timestamp;

/**
 * Represents a SimpleMessage
 */
public class SimpleMessage implements Message {
    private final String message;
    private final Timestamp timestamp;
    private final String username;
    private final String conversation;

    /**
     * Constructs a SimpleMessage
     * 
     * @param message: content of the Message
     * @param timestamp: time message was created
     * @param username: name of the user that wrote the Message
     * @param conversation: name of converstation Message belongs to
     */
    public SimpleMessage(String message, 
                         Timestamp timestamp, 
                         String username,
                         String conversation) {
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
        this.conversation = conversation;
    }

    /**
     * Getter for message
     * 
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Getter for timestamp
     * 
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    /**
     * Getter for username
     * 
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Getter for conversation
     * 
     * @return conversation
     */
    public String getConversation() {
        return this.conversation;
    }
    
    /**
     * Converts Message to a String
     * 
     * @return string representation of the Message
     */
    @Override
    public String toString() {
        return "<User: " + this.username + 
               "; Message: " + this.message + ">";
    }

    /**
     * Creates a hashcode for the Message
     * 
     * @return hash of the Message (int)
     */
    @Override
    public int hashCode() {
        int hash = 7;
        for (char c: this.message.toCharArray()) hash = hash*31 + c;
        for (char c: this.username.toCharArray()) hash = hash*31 + c;
        return hash;
    }

    /**
     * Checks to see if the given Object is equal to
     * this Message
     * 
     * @return boolean (true is equal; false otherwise)
     */
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
