package security;

import exception.CertificateValidationException;
import options.RequestOptions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import static security.CertificateValidation.validate;
import static security.CertificateValidation.validateGracePeriod;

/**
 * This class is used to validate the SSL connection.
 */
public class ValidationTrustManager implements X509TrustManager
{
    private static final Logger logger =
        LogManager.getLogger( ValidationTrustManager.class );
    
    private final String host;
    
    private final int port;
    
    private final RequestOptions options;
    
    /**
     * Create a new trust manager for validation of the SSL connection.
     *
     * @param host    the server host
     * @param port    the server port
     * @param options the request options
     */
    public ValidationTrustManager( String host, int port,
        RequestOptions options )
    {
        this.host = host;
        this.port = port;
        this.options = options;
    }
    
    @Override
    public void checkClientTrusted( X509Certificate[] chain, String authType )
    {
        // do nothing
    }
    
    /**
     * Validates the server certificate.
     *
     * @param chain    the peer certificate chain
     * @param authType the key exchange algorithm used
     * @throws CertificateEncodingException when the certificate can't be encoded
     */
    @Override
    public void checkServerTrusted( X509Certificate[] chain,
        String authType ) throws CertificateEncodingException
    {
        // do we need to check the certificate?
        if ( !options.isValidationEnabled() )
        {
            return;
        }
        
        // did we get a certificate from the server?
        if ( chain.length == 0 )
        {
            logger.error( "No server certificate received." );
            throw new CertificateValidationException( "No server certificate " +
                "received." );
        }
        
        X509Certificate cert = chain[ 0 ];
        String certHost =
            StringUtils.substringAfter( cert.getSubjectX500Principal().getName(), "=" );
        Date notBefore = cert.getNotBefore();
        Date notAfter = cert.getNotAfter();
        String fingerprint =
            StringUtils.upperCase( DigestUtils.sha256Hex( cert.getEncoded() ) );
        
        // validate hostname and validity dates
        validate( certHost, host, notBefore, notAfter );
        
        // get trusted certificate from db
        TrustedCertificateSupplier tcs =
            options.getTrustedCertificateSupplier();
        if ( tcs == null )
        {
            logger.error( "No certificate supplier set." );
            throw new CertificateValidationException( "No certificate " +
                "supplier set." );
        }
        TrustedCertificate tc = tcs.get( host, port );
        if ( tc == null )
        {
            // first time connecting to host:port
            // TOFU, accept certificate and save it in db
            TrustedCertificateConsumer tcc =
                options.getTrustedCertificateConsumer();
            if ( tcc == null )
            {
                logger.error( "No certificate consumer set." );
                throw new CertificateValidationException( "No certificate " +
                    "consumer set." );
            }
            tcc.accept( new TrustedCertificate( host, port, notAfter,
                fingerprint ) );
            return;
        }
        
        // compare fingerprints
        if ( !StringUtils.equals( tc.getFingerPrint(), fingerprint ) )
        {
            // check expiry date for MITM protection
            if ( options.isCertificateRenewalCheckEnabled() )
            {
                validateGracePeriod( options.getCertificateGracePeriod(),
                    tc.getExpiryDate() );
            }
        }
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[ 0 ];
    }
    
}
