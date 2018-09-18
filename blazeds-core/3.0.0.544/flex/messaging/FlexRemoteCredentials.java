package flex.messaging;

public class FlexRemoteCredentials
{
    private String service;

    private String destination;

    private String username;

    private Object credentials;

    public FlexRemoteCredentials(String service, String destination, 
            String username, Object credentials)
    {
        super();
        this.service = service;
        this.destination = destination;
        this.username = username;
        this.credentials = credentials;
    }

    public String getUsername()
    {
        return username;
    }

    public Object getCredentials()
    {
        return credentials;
    }

    public String getService()
    {
        return service;
    }

    public String getDestination()
    {
        return destination;
    }

}
