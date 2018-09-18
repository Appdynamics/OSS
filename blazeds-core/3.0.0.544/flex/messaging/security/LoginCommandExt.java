package flex.messaging.security;

/**
 * Extensions to the LoginCommand interface. 
 */
public interface LoginCommandExt {
	
    /**
     * Classes that implement the flex.messaging.security.LoginCommand interface, should also
     * implement this interface if the name stored in java.security.Principal created as a result
     * of a succesfull authentication differs from the username that is actually passed in to 
     * the authentication.  
     * 
     * Implementing this interace gives such LoginCommand's a chance to return the resulting
     * username so that it can be compared to the one stored in Principal. 
     * 
     * Returns the value that would be returned by java.security.Principal.getName() if 
     * username/credentials had been authenticated 
     * 
     * @param username - User whose comparable name will be retrieved
     * @param credentials - Credentials for user whose comparable name will be retrieved
     * @return - value that would be returned by java.security.Principal.getName() if 
     *           username/credentials had been authenticated 
     */
    String getPrincipalNameFromCredentials(String username, Object credentials);

}
