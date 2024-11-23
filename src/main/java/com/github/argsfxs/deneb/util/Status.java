package com.github.argsfxs.deneb.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents the status returned from the server.
 */
public enum Status
{
    INPUT_EXPECTED( 10 ),
    SENSITIVE_INPUT( 11 ),
    SUCCESS( 20 ),
    TEMPORARY_REDIRECTION( 30 ),
    PERMANENT_REDIRECTION( 31 ),
    TEMPORARY_FAILURE( 40 ),
    SERVER_UNAVAILABLE( 41 ),
    CGI_ERROR( 42 ),
    PROXY_ERROR( 43 ),
    SLOW_DOWN( 44 ),
    PERMANENT_FAILURE( 50 ),
    NOT_FOUND( 51 ),
    GONE( 52 ),
    PROXY_REQUEST_REFUSED( 53 ),
    BAD_REQUEST( 59 ),
    REQUIRES_CERTIFICATE( 60 ),
    CERTIFICATE_NOT_AUTHORIZED( 61 ),
    CERTIFICATE_NOT_VALID( 62 ),
    INVALID( 0 );
    
    private static final Logger logger = LogManager.getLogger( Status.class );
    
    private final int code;
    
    Status( int code )
    {
        this.code = code;
    }
    
    /**
     * Returns the status from the response header obtained from the server.
     *
     * @param header the response header
     * @return the corresponding Status object
     */
    public static Status fromHeader( String header )
    {
        if ( StringUtils.length( header ) < 2 )
        {
            logStatusError( header );
            return INVALID;
        }
        int code;
        try
        {
            code = Integer.parseInt( StringUtils.substring( header, 0, 2 ) );
        }
        catch ( NumberFormatException nfe )
        {
            logStatusError( header );
            return INVALID;
        }
        if ( code < 10 || code > 69 )
        {
            logStatusError( header );
            return INVALID;
        }
        return fromCode( code );
    }
    
    /**
     * Returns the status from its numerical value.
     *
     * @param code the numerical value
     * @return the corresponding Status object
     */
    public static Status fromCode( int code )
    {
        
        for ( Status s : Status.values() )
        {
            if ( s.code == code )
            {
                return s;
            }
        }
        return fromCode( ( code / 10 ) * 10 );
    }
    
    private static void logStatusError( String header )
    {
        logger.error( "Invalid response status returned from server: {}",
            header );
    }
    
    /**
     * Returns the numerical value of this Status.
     *
     * @return the numerical value
     */
    public int getCode()
    {
        return code;
    }
    
}
