package security;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a trusted certificate that can be stored in a database and retrieved
 * to compare it to the current server certificate.
 */
public class TrustedCertificate implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private final String host;
    
    private final int port;
    
    private final Date expiryDate;
    
    private final String fingerPrint;
    
    /**
     * Creates a new trusted certificate object to be stored in a database.
     *
     * @param host        the certificate host
     * @param port        the certificate port
     * @param expiryDate  the certificate expiry date
     * @param fingerPrint the certificate fingerprint
     */
    public TrustedCertificate( String host, int port, Date expiryDate,
        String fingerPrint )
    {
        this.host = host;
        this.port = port;
        this.expiryDate = expiryDate;
        this.fingerPrint = fingerPrint;
    }
    
    /**
     * Returns the host the certificate is valid for.
     *
     * @return the certificate host
     */
    public String getHost()
    {
        return host;
    }
    
    /**
     * Returns the port the certificate is valid for.
     *
     * @return the certificate port
     */
    public int getPort()
    {
        return port;
    }
    
    /**
     * Returns the expiry date of the certificate.
     *
     * @return the expiry date
     */
    public Date getExpiryDate()
    {
        return expiryDate;
    }
    
    /**
     * Returns the certificate fingerprint.
     *
     * @return the fingerprint
     */
    public String getFingerPrint()
    {
        return fingerPrint;
    }
}
