package com.github.argsfxs.deneb.request;

import com.github.argsfxs.deneb.exception.SocketHandshakeException;
import com.github.argsfxs.deneb.exception.SocketIOException;
import com.github.argsfxs.deneb.response.GeminiResponse;
import com.github.argsfxs.deneb.response.GeminiResponseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * This class represents a Gemini request to a Gemini server.
 */
public class GeminiRequest
{
    private final String url;
    
    private final SSLSocket socket;
    
    private final Logger logger = LogManager.getLogger( GeminiRequest.class );
    
    GeminiRequest( String url, SSLSocket socket )
    {
        this.url = url;
        this.socket = socket;
    }
    
    private GeminiRequest withCertificate()
    {
        throw new UnsupportedOperationException( "not yet supported" );
    }
    
    /**
     * Sends the request to the server.
     *
     * @return the server response
     */
    public GeminiResponse send()
    {
        initHandshake();
        InputStream in = null;
        OutputStream out;
        
        try
        {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            PrintWriter pw = new PrintWriter( out, true );
            pw.println( url );
        }
        catch ( IOException e )
        {
            logger.error( "Exception during socket read/write: {}",
                e.getMessage() );
            throw new SocketIOException( e );
        }
        
        return GeminiResponseFactory.create( in );
    }
    
    private void initHandshake()
    {
        try
        {
            socket.startHandshake();
        }
        catch ( IOException e )
        {
            logger.error( "Exception during TLS handshake: {}",
                e.getMessage() );
            throw new SocketHandshakeException( e );
        }
    }
    
    @Override
    public String toString()
    {
        return url;
    }
    
}
