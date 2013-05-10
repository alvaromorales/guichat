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

    /**
     * Checks if a Login object is equal to another Object
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
        Login other = (Login) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
    
}
