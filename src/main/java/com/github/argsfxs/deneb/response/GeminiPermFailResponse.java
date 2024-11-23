package com.github.argsfxs.deneb.response;

import com.github.argsfxs.deneb.util.Status;

/**
 * This class represents a permanently failing response with the status codes 5x.
 */
public class GeminiPermFailResponse extends GeminiErrorResponse
{
    GeminiPermFailResponse( Status status, String header, String errorMessage )
    {
        super( status, header, errorMessage );
    }
}
