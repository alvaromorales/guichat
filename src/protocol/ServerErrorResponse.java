package protocol;

/**
 * Represents a ServerErrorResponse response message
 */
public class ServerErrorResponse implements Response {
    private String error;
    private Type errorType;

    /**
     * Types of server errors
     */
    public static enum Type {
        LOGIN_TAKEN,
        UNAUTHORIZED
    }
    
    /**
     * Creates a new ServerErrorResponse response object
     * @param type the type of error
     * @param error the error to send to the client
     */
    public ServerErrorResponse(Type type, String error) {
        this.errorType = type;
        this.error = error;
    }

    /**
     * Gets the server error type
     * @return the server error type
     */
    public Type getType() {
        return errorType;
    }
    
    /**
     * Gets the server error message
     * @return the server error message
     */
    public String getError() {
        return error;
    }

    /**
     * Gets the String representation of a ServerErrorResponse object
     * @return the String representation of a ServerErrorResponse object
     */
    @Override
    public String toString() {
        return "ServerErrorResponse [errorType=" + errorType + ", error=" + error + "]";
    }

    /**
     * Compares if a ServerErrorResponse object is equal to another object
     * @param obj the object to compare to
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerErrorResponse other = (ServerErrorResponse) obj;
        if (error == null) {
            if (other.error != null)
                return false;
        } else if (!error.equals(other.error))
            return false;
        if (errorType != other.errorType)
            return false;
        return true;
    }
    
    
    
}
