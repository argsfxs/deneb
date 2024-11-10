package response;

import util.MIMEType;
import util.Status;

import java.io.InputStream;

/**
 * This class represents a successful response with the status codes 2x.
 */
public class GeminiSuccessResponse extends GeminiResponse
{
    private final MIMEType mimeType;
    
    private final InputStream content;
    
    GeminiSuccessResponse( Status status, String header, MIMEType mimeType,
        InputStream content )
    {
        super( status, header );
        this.mimeType = mimeType;
        this.content = content;
    }
    
    public MIMEType getMimeType()
    {
        return mimeType;
    }
    
    public InputStream getContent()
    {
        return content;
    }
    
}
