package protocol;

import server.Visitor;

/**
 * Represents a Registration request or response
 */
public class Registration {
    private String username;
    
    /**
     * Creates a new Registration object
     * @param username the username part of the Registration request/response
     */
    public Registration(String username) {
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
     * Gets the String representation of a Registration object
     * @return the String representation of a Registration object
     */
    @Override
    public String toString() {
        return "Registration [username=" + username + "]";
    }

    /**
     * Checks if a Registration object is equal to another Object
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
        Registration other = (Registration) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    } 
    
    /**
     * Represents a LoginRequest
     */
    public static class LoginRequest extends Registration implements Request {

        /**
         * Creates a new LoginRequest object
         * @param username the username part of the LoginRequest
         */
        public LoginRequest(String username) {
            super(username);
        }

        /**
         * Accepts a visitor
         */
        @Override
        public <E> E accept(Visitor<E> v) {
            return null;
        }
    }
    
    public static class LogoutRequest extends Registration implements Request {

        /**
         * Creates a new LogoutRequest object
         * @param username the username part of the LogoutRequest
         */
        public LogoutRequest(String username) {
            super(username);
        }

        /**
         * Accepts a visitor
         */
        @Override
        public <E> E accept(Visitor<E> v) {
            return v.visit(this);
        } 
    }
    
    public static class LoginResponse extends Registration implements Response {

        /**
         * Creates a new LoginResponse object
         * @param username the username part of the LoginResponse
         */
        public LoginResponse(String username) {
            super(username);
        }
    }
}