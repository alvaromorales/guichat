package protocol;

/**
 * Represents a Login request or response
 */
public class LoginResponse implements Response {
    private String username;
    
    /**
     * Creates a new LoginResponse object
     * @param username the username part of the request/response
     */
    public LoginResponse(String username) {
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
     * Gets the String representation of a LoginResponse object
     * @return the String representation of a LoginResponse object
     */
    @Override
    public String toString() {
        return "LoginResponse [username=" + username + "]";
    }

    /**
     * Checks if a LoginResponse object is equal to another Object
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
        LoginResponse other = (LoginResponse) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
    
}
