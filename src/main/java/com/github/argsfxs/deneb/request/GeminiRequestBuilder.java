package com.github.argsfxs.deneb.request;

import com.github.argsfxs.deneb.exception.SocketConnectException;
import com.github.argsfxs.deneb.exception.URILengthExceededException;
import com.github.argsfxs.deneb.options.RequestOptions;
import com.github.argsfxs.deneb.security.ValidationTrustManager;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/**
 * This class is used to build a {@link GeminiRequest}.
 */
public class GeminiRequestBuilder
{
    private static final String[] protocols = new String[]{ "TLSv1.3" };
    
    private static final String[] cipherSuites = new String[]{
        "TLS_AES_128_GCM_SHA256" };
    
    private final String host;
    
    private final RequestOptions options;
    
    private final Logger logger =
        LogManager.getLogger( GeminiRequestBuilder.class );
    
    private String scheme = "gemini";
    
    private String path = "/";
    
    private String queryString = "";
    
    private int port = 1965;
    
    /**
     * Creates a new request builder.<br/>
     * The request will use default options when being sent.
     *
     * @param host the host to connect to
     */
    public GeminiRequestBuilder( String host )
    {
        this( host, new RequestOptions() );
    }
    
    /**
     * Creates a new request builder.
     *
     * @param host    the host to connect to
     * @param options the options controlling the behavior when the request is being sent
     */
    public GeminiRequestBuilder( String host, RequestOptions options )
    {
        this.host = host;
        this.options = options;
    }
    
    /**
     * Sets the request path.<b/>
     * The default path is <code>/</code>.
     *
     * @param path the request path
     * @return the builder object
     */
    public GeminiRequestBuilder withPath( String path )
    {
        this.path = path;
        return this;
    }
    
    /**
     * Sets the request scheme.
     * The default scheme is <code>gemini</code>.
     *
     * @param scheme the request scheme
     * @return the builder object
     */
    public GeminiRequestBuilder withScheme( String scheme )
    {
        this.scheme = scheme;
        return this;
    }
    
    /**
     * Sets the request port.
     * The default port is <code>1965</code>.
     *
     * @param port the request port
     * @return the builder object
     */
    public GeminiRequestBuilder withPort( int port )
    {
        this.port = port;
        return this;
    }
    
    /**
     * Sets the request query string.
     * The default query string is empty.
     *
     * @param queryString the request query string
     * @return the builder object
     */
    public GeminiRequestBuilder withQueryString( String queryString )
    {
        this.queryString = encodeQuery( queryString );
        return this;
    }
    
    /**
     * Builds the request.
     *
     * @return the request that is ready to be sent
     */
    public GeminiRequest build()
    {
        String url = buildUrl();
        SSLSocket socket = connect( host, port );
        return new GeminiRequest( url, socket );
    }
    
    String buildUrl()
    {
        String url = String.format( "%s://%s%s", scheme, host,
            normalizePath( path ) );
        if ( !StringUtils.isEmpty( queryString ) )
        {
            url = String.format( "%s?%s", url, queryString );
        }
        int urlLength = url.getBytes( StandardCharsets.UTF_8 ).length;
        if ( urlLength > 1024 )
        {
            logger.error( "Provided URI too long! Length: {}", urlLength );
            throw new URILengthExceededException();
        }
        return url;
    }
    
    private String normalizePath( String path )
    {
        return path.startsWith( "/" ) ? path : "/" + path;
    }
    
    private String encodeQuery( String queryString )
    {
        String encoded;
        try
        {
            encoded = new URLCodec().encode( queryString );
        }
        catch ( EncoderException e )
        {
            encoded = queryString;
            logger.error( "Couldn't encode query string: {}",
                String.valueOf( e ) );
        }
        return encoded.replaceAll( "\\+", "%20" );
    }
    
    private SSLSocket connect( String host, int port )
    {
        SSLSocket socket;
        try
        {
            SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null,
                new TrustManager[]{ new ValidationTrustManager( host, port,
                    options ) },
                new SecureRandom() );
            SSLContext.setDefault( sc );
            SSLSocketFactory factory =
                ( SSLSocketFactory ) SSLSocketFactory.getDefault();
            socket = ( SSLSocket ) factory.createSocket( host, port );
            socket.setEnabledProtocols( protocols );
            socket.setEnabledCipherSuites( cipherSuites );
            socket.setUseClientMode( true );
        }
        catch ( GeneralSecurityException | IOException e )
        {
            logger.error( "Couldn't connect to socket: {}", e.getMessage() );
            throw new SocketConnectException( e );
        }
        return socket;
    }
}
