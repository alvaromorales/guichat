package protocol;

/**
 * Represents a LoginSuccess response message
 */
public class LoginSuccess implements Response {
    private String username;
    
    /**
     * Represents a login success message
     * @param username the username that the client logged in with
     */
    public LoginSuccess(String username) {
        this.username = username;
    }

    /**
     * Gets the username that the client logged in with
     * @return the username that the client logged in with
     */
    public String getUsername() {
        return username;
    }
    
}
