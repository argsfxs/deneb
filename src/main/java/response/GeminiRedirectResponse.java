package response;

import util.Status;

import java.net.URI;

/**
 * This class represents a redirection response with the status codes 3x.
 */
public class GeminiRedirectResponse extends GeminiResponse
{
    private final URI uri;
    
    GeminiRedirectResponse( Status status, String header, URI uri )
    {
        super( status, header );
        this.uri = uri;
    }
    
    public URI getURI()
    {
        return uri;
    }
}
