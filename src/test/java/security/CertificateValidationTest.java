package security;

import exception.InvalidCertificateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static security.CertificateValidation.*;

public class CertificateValidationTest
{
    
    @Test
    public void hostMatch()
    {
        validateHost( "foo.bar", "foo.bar" );
    }
    
    @Test
    public void hostNoMatch()
    {
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> validateHost( "foo.bar", "foo.baz" ) );
        assertEquals( "Certificate host doesn't match!", ice.getMessage() );
    }
    
    @Test
    public void notBeforeIsToday()
    {
        Date notBefore = new Date();
        Date notAfter = new Date( Long.MAX_VALUE );
        validateDate( notBefore, notAfter );
    }
    
    @Test
    public void notAfterIsToday()
    {
        Date notBefore = new Date( Long.MIN_VALUE );
        Date notAfter = new Date();
        validateDate( notBefore, notAfter );
    }
    
    @Test
    public void notBeforeIsTomorrow()
    {
        ZoneId zone = ZoneId.systemDefault();
        Date notBefore =
            Date.from( LocalDate.now( zone ).plusDays( 1 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() );
        Date notAfter = new Date( Long.MAX_VALUE );
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> validateDate( notBefore, notAfter ) );
        assertEquals( "Certificate not yet valid!", ice.getMessage() );
    }
    
    @Test
    public void notAfterWasYesterday()
    {
        ZoneId zone = ZoneId.systemDefault();
        Date notBefore = new Date( Long.MIN_VALUE );
        Date notAfter =
            Date.from( LocalDate.now( zone ).minusDays( 1 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() );
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> validateDate( notBefore, notAfter ) );
        assertEquals( "Certificate has expired!", ice.getMessage() );
    }
    
    @Test
    public void renewalInGracePeriod()
    {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate expiryDate = LocalDate.now( zone ).plusDays( 10 );
        validateGracePeriod( 90,
            Date.from( expiryDate.atStartOfDay().atZone( zone ).toInstant() ) );
    }
    
    @Test
    public void renewalOutOfGracePeriod()
    {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate expiryDate = LocalDate.now( zone ).plusDays( 100 );
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> validateGracePeriod( 90,
                    Date.from( expiryDate.atStartOfDay().atZone( zone ).toInstant() ) ) );
        assertEquals( "Certificate renewal out of grace period. Potential " +
            "MITM attack!", ice.getMessage() );
    }
    
}
