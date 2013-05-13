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
        return "username=" + username;
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
         * Never called
         */
        @Override
        public <E> E accept(Visitor<E> v) {
            return null;
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
            if (getUsername() == null) {
                if (other.getUsername() != null)
                    return false;
            } else if (!getUsername().equals(other.getUsername()))
                return false;
            return true;
        }

        /**
         * Gets the String representation of a LoginRequest object
         */
        @Override
        public String toString() {
            return "LoginRequest [" + super.toString() + "]";
        }
    }
    
    /**
     * Represents a LogoutRequest
     */
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
                
        /**
         * Checks if a LogoutRequest object is equal to another Object
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
            LogoutRequest other = (LogoutRequest) obj;
            if (getUsername() == null) {
                if (other.getUsername() != null)
                    return false;
            } else if (!getUsername().equals(other.getUsername()))
                return false;
            return true;
        } 
        
        /**
         * Gets the String representation of a LogoutRequest object
         */
        @Override
        public String toString() {
            return "LogoutRequest [" + super.toString() + "]";
        }
        
    }
    
    /**
     * Represents a LoginResponse
     */
    public static class LoginResponse extends Registration implements Response {

        /**
         * Creates a new LoginResponse object
         * @param username the username part of the LoginResponse
         */
        public LoginResponse(String username) {
            super(username);
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
            if (getUsername() == null) {
                if (other.getUsername() != null)
                    return false;
            } else if (!getUsername().equals(other.getUsername()))
                return false;
            return true;
        }
        
        /**
         * Gets the String representation of a LoginResponse object
         */
        @Override
        public String toString() {
            return "LoginResponse [" + super.toString() + "]";
        }
    }
}