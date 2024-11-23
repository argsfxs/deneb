package com.github.argsfxs.deneb.options;

import com.github.argsfxs.deneb.security.TrustedCertificateConsumer;
import com.github.argsfxs.deneb.security.TrustedCertificateSupplier;

/**
 * This class represents options to control the behavior when a request is being sent.
 */
public class RequestOptions
{
    private boolean validationEnabled = false;
    
    private boolean certificateRenewalCheckEnabled = false;
    
    private TrustedCertificateSupplier tcs;
    
    private TrustedCertificateConsumer tcc;
    
    private int certificateGracePeriod = 90;
    
    /**
     * Retrieves the option whether validation of the server certificate is enabled.
     *
     * @return <code>true</code> if validation is enabled, <code>false</code> otherwise
     */
    public boolean isValidationEnabled()
    {
        return validationEnabled;
    }
    
    /**
     * Sets the options whether to validate the server certificate.
     *
     * @param validationEnabled to enable or disable the validation
     */
    public void setValidationEnabled( boolean validationEnabled )
    {
        this.validationEnabled = validationEnabled;
    }
    
    /**
     * Retrieves the supplier for the server's trusted certificate.
     *
     * @return the supplier for the stored trusted certificates
     */
    public TrustedCertificateSupplier getTrustedCertificateSupplier()
    {
        return tcs;
    }
    
    /**
     * Sets the supplier for the server's trusted certificate.
     *
     * @param tcs the supplier for the stored trusted certificates
     */
    public void setTrustedCertificateSupplier( TrustedCertificateSupplier tcs )
    {
        this.tcs = tcs;
    }
    
    /**
     * Retrieves the consumer for the server's trusted certificate.
     *
     * @return the consumer for the received trusted certificates
     */
    public TrustedCertificateConsumer getTrustedCertificateConsumer()
    {
        return tcc;
    }
    
    /**
     * Sets the consumer for the server's trusted certificate.
     *
     * @param tcc the consumer for the received trusted certificates
     */
    public void setTrustedCertificateConsumer( TrustedCertificateConsumer tcc )
    {
        this.tcc = tcc;
    }
    
    /**
     * Retrieves the option whether the certificate renewal check is enabled.<br/>
     * This could prevent man-in-the-middle-attacks by checking whether the current server
     * certificate is identical with one that has been previously received.
     *
     * @return <code>true</code> if the renewal check is enabled, <code>false</code> otherwise
     */
    public boolean isCertificateRenewalCheckEnabled()
    {
        return certificateRenewalCheckEnabled;
    }
    
    /**
     * Sets the option whether to check server certificate renewal.
     *
     * @param certificateRenewalCheckEnabled to enable or disable the check
     */
    public void setCertificateRenewalCheckEnabled( boolean certificateRenewalCheckEnabled )
    {
        this.certificateRenewalCheckEnabled = certificateRenewalCheckEnabled;
    }
    
    /**
     * Retrieves the grace period of the certificate renewal check.<br/>
     * The check fails if the server certificate has been changed although the expiry date
     * is not within the grace period.
     *
     * @return the number of days a certificate can be renewed before its expiry date
     */
    public int getCertificateGracePeriod()
    {
        return certificateGracePeriod;
    }
    
    /**
     * Sets the grace period of the certificate renewal check.
     *
     * @param certificateGracePeriod the number of days a certificate can be renewed before its
     *                               expiry date
     */
    public void setCertificateGracePeriod( int certificateGracePeriod )
    {
        this.certificateGracePeriod = certificateGracePeriod;
    }
}
