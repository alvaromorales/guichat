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
     * Checks to see if the given Object is equal to
     * this Message
     * 
     * @return boolean (true is equal; false otherwise)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleMessage other = (SimpleMessage) obj;
        if (conversation == null) {
            if (other.conversation != null)
                return false;
        } else if (!conversation.equals(other.conversation))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
