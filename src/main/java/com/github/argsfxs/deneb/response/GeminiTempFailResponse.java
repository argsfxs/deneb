package com.github.argsfxs.deneb.response;

import com.github.argsfxs.deneb.util.Status;

/**
 * This class represents a temporarily failing response with the status codes 4x.
 */
public class GeminiTempFailResponse extends GeminiErrorResponse
{
    GeminiTempFailResponse( Status status, String header, String errorMessage )
    {
        super( status, header, errorMessage );
    }
}
