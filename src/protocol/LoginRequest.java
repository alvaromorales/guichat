package protocol;

/**
 * Represents a Login request or response
 */
public class LoginRequest implements Request {
    private String username;
    
    /**
     * Creates a new LoginRequest object
     * @param username the username part of the request/response
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
        
    /**
     * Gets the String representation of a LoginRequest object
     * @return the String representation of a LoginRequest object
     */
    @Override
    public String toString() {
        return "LoginRequest [username=" + username + "]";
    }

    /**
     * Checks if a LoginRequest object is equal to another Object
     * @param obj the object to compare
     * @return true if equals, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LoginRequest other = (LoginRequest) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
    
}
