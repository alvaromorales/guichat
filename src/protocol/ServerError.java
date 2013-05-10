package protocol;

/**
 * Represents a ServerError response message
 */
public class ServerError implements Response {
    private String error;
    private Type type;

    /**
     * Types of server errors
     */
    public static enum Type {
        LOGIN_TAKEN,
        UNAUTHORIZED
    }
    
    /**
     * Creates a new ServerError response object
     * @param type the type of error
     * @param error the error to send to the client
     */
    public ServerError(Type type, String error) {
        this.type = type;
        this.error = error;
    }

    /**
     * Gets the server error type
     * @return the server error type
     */
    public Type getType() {
        return type;
    }
    
    /**
     * Gets the server error message
     * @return the server error message
     */
    public String getError() {
        return error;
    }

    /**
     * Gets the String representation of a ServerError object
     * @return the String representation of a ServerError object
     */
    @Override
    public String toString() {
        return "ServerError [type=" + type + ", error=" + error + "]";
    }
    
}
