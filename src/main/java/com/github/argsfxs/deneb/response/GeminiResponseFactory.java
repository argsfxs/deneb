package com.github.argsfxs.deneb.response;

import com.github.argsfxs.deneb.exception.SocketIOException;
import com.github.argsfxs.deneb.util.MIMEType;
import com.github.argsfxs.deneb.util.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * This class is used to create a {@link GeminiResponse]} from a server input stream.
 */
public class GeminiResponseFactory
{
    private static final Logger logger =
        LogManager.getLogger( GeminiResponseFactory.class );
    
    private GeminiResponseFactory()
    {
    }
    
    /**
     * Creates a response from an InputStream.
     *
     * @param in the server's input stream
     * @return the response object
     */
    public static GeminiResponse create( InputStream in )
    {
        int previous = -1;
        int current;
        StringBuilder headerBuilder = new StringBuilder();
        
        try
        {
            while ( ( current = in.read() ) != -1 )
            {
                // check for CR (13) followed by LF (10) without appending
                // the CR
                if ( previous == 13 && current == 10 )
                {
                    break; // don't append CR or LF
                }
                
                // append previous character (if not CRLF detected in
                // previous iteration)
                if ( previous != -1 )
                {
                    headerBuilder.append( ( char ) previous );
                }
                
                previous = current;
            }
            
            // append the last character if end of stream is reached (no CRLF
            // detected)
            if ( current == -1 && previous != -1 )
            {
                headerBuilder.append( ( char ) previous );
            }
        }
        catch ( IOException e )
        {
            logger.error( "Exception while reading from socket: {}",
                e.getMessage() );
            throw new SocketIOException( e );
        }
        
        String header = headerBuilder.toString();
        Status status = Status.fromHeader( header );
        
        switch ( status.getCode() / 10 )
        {
            case 1:
            {
                return new GeminiInputResponse( status, header,
                    readMeta( header ) );
            }
            case 2:
            {
                return new GeminiSuccessResponse( status, header,
                    MIMEType.from( readMeta( header ) ), in );
            }
            case 3:
            {
                return new GeminiRedirectResponse( status, header,
                    makeURI( readMeta( header ) ) );
            }
            case 4:
            {
                return new GeminiTempFailResponse( status, header,
                    readMeta( header ) );
            }
            case 5:
            {
                return new GeminiPermFailResponse( status, header,
                    readMeta( header ) );
            }
            case 6:
            {
                return new GeminiAuthResponse( status, header,
                    readMeta( header ) );
            }
            default:
            {
                logger.error( "Could not parse response status, creating " +
                    "generic error response." );
                return new GeminiErrorResponse( status, header,
                    readMeta( header ) );
            }
        }
    }
    
    private static String readMeta( String header )
    {
        String[] segments = StringUtils.split( header );
        if ( segments.length < 2 )
        {
            return null;
        }
        return String.join( " ", Arrays.copyOfRange( segments, 1,
            segments.length ) );
    }
    
    private static URI makeURI( String uriString )
    {
        if ( StringUtils.isBlank( uriString ) )
        {
            logger.error( "No redirect URI received!" );
            return null;
        }
        try
        {
            return new URI( uriString );
        }
        catch ( URISyntaxException e )
        {
            logger.error( "Invalid redirect URI received!" );
            return null;
        }
    }
    
}
