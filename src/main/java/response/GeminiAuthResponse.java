package response;

import util.Status;

/**
 * This class represents an auth response (related to client certificates) with the status codes 6x.
 */
public class GeminiAuthResponse extends GeminiErrorResponse
{
    GeminiAuthResponse( Status status, String header, String errorMessage )
    {
        super( status, header, errorMessage );
    }
}