package protocol;

/**
 * Represents a ServerError response message
 */
public class ServerError implements Response {
    private String error;
    
    /**
     * Creates a new ServerError response object
     * @param error the error to send to the client
     */
    public ServerError(String error) {
        this.error = error;
    }

    /**
     * Gets the server error message
     * @return the server error message
     */
    public String getError() {
        return error;
    }
    
}
