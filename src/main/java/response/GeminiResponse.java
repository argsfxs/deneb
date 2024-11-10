package response;

import util.Status;

/**
 * This class represents a generic response. It is the base class for all Gemini responses.
 */
public class GeminiResponse
{
    private final Status status;
    
    private final String header;
    
    GeminiResponse( Status status, String header )
    {
        this.status = status;
        this.header = header;
    }
    
    public Status getStatus()
    {
        return status;
    }
    
    public String getHeader()
    {
        return header;
    }
}
