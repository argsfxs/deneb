package response;

import util.Status;

import java.util.Optional;

/**
 * This class represents a generic error response. It is the base class for all Gemini error
 * responses.
 */
public class GeminiErrorResponse extends GeminiResponse
{
    private final String errorMessage;
    
    GeminiErrorResponse( Status status, String header, String errorMessage )
    {
        super( status, header );
        this.errorMessage = errorMessage;
    }
    
    public Optional<String> getErrorMessage()
    {
        return Optional.ofNullable( errorMessage );
    }
}
