package security;

import exception.InvalidCertificateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

class CertificateValidation
{
    private static final Logger logger =
        LogManager.getLogger( ValidationTrustManager.class );
    
    private CertificateValidation()
    {
    }
    
    static void validate( String certificateHost, String requestHost,
        Date notBefore, Date notAfter )
    {
        validateHost( certificateHost, requestHost );
        validateDate( notBefore, notAfter );
    }
    
    static void validateGracePeriod( int gracePeriod, Date expiryDate )
    {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate expiryDateLd =
            expiryDate.toInstant().atZone( zone ).toLocalDate();
        LocalDate today = LocalDate.now( zone );
        long daysBetween = DAYS.between( today, expiryDateLd );
        if ( daysBetween > gracePeriod )
        {
            logger.error( "Certificate renewal out of grace period: {}",
                daysBetween );
            throw new InvalidCertificateException( "Certificate renewal out " + "of grace period." +
                " Potential MITM attack!" );
        }
    }
    
    static void validateHost( String certificateHost,
        String requestHost )
    {
        if ( !StringUtils.equals( certificateHost, requestHost ) )
        {
            logger.error( "Certificate host doesn't match! CertHost: {}, " +
                "ReqHost: {}", certificateHost, requestHost );
            throw new InvalidCertificateException( "Certificate host doesn't "
                + "match!" );
        }
    }
    
    static void validateDate( Date notBefore, Date notAfter )
    {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate notBeforeLd =
            notBefore.toInstant().atZone( zone ).toLocalDate();
        LocalDate notAfterLd =
            notAfter.toInstant().atZone( zone ).toLocalDate();
        LocalDate today = LocalDate.now( zone );
        
        if ( today.isBefore( notBeforeLd ) )
        {
            logger.error( "Certificate not yet valid!" );
            throw new InvalidCertificateException( "Certificate not yet " +
                "valid!" );
        }
        if ( today.isAfter( notAfterLd ) )
        {
            logger.error( "Certificate has expired!" );
            throw new InvalidCertificateException( "Certificate has expired!" );
        }
    }
}
