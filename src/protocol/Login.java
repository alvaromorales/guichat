package protocol;

/**
 * Represents a Login request or response
 */
public class Login implements Request, Response {
    private String username;
    
    /**
     * Creates a new Login object
     * @param username the username part of the request/response
     */
    public Login(String username) {
        this.username = username;
    }

    /**
     * Gets the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }
        
    /**
     * Gets the String representation of a Login object
     * @return the String representation of a Login object
     */
    @Override
    public String toString() {
        return "Login [username=" + username + "]";
    }
}
