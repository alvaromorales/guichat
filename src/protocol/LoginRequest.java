package protocol;

/**
 * Represents a Login request
 */
public class LoginRequest implements Request {
    private String username;
    
    /**
     * Creates a new Login request object
     * @param username the username the client wants to be logged in as
     */
    public LoginRequest(String username) {
        this.username = username;
    }

    /**
     * Gets the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }
        
}
