package com.github.argsfxs.deneb.security;

import com.github.argsfxs.deneb.exception.CertificateValidationException;
import com.github.argsfxs.deneb.exception.InvalidCertificateException;
import com.github.argsfxs.deneb.options.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.x500.X500Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
public class ValidationTrustManagerTest
{
    
    @Mock
    private X509Certificate cert;
    
    @Mock
    private X500Principal principal;
    
    @Mock
    private RequestOptions options;
    
    @Mock
    private TrustedCertificateSupplier supplier;
    
    @Mock
    private TrustedCertificateConsumer consumer;
    
    @Mock
    private TrustedCertificate tc;
    
    @BeforeEach
    public void setUp()
    {
        when( options.isValidationEnabled() ).thenReturn( true );
    }
    
    @Test
    public void validationDisabled() throws CertificateEncodingException
    {
        when( options.isValidationEnabled() ).thenReturn( false );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        
        vtm.checkServerTrusted( null, null );
    }
    
    @Test
    public void noCertificate()
    {
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = {};
        
        CertificateValidationException cve =
            assertThrows( CertificateValidationException.class,
                () -> vtm.checkServerTrusted( chain, null ) );
        assertEquals( "No server certificate received.", cve.getMessage() );
    }
    
    @Test
    public void wrongHost() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.baz" );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> vtm.checkServerTrusted( chain, null ) );
        assertEquals( "Certificate host doesn't match!", ice.getMessage() );
    }
    
    @Test
    public void notBeforeWrong() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.bar" );
        when( cert.getNotBefore() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getNotAfter() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> vtm.checkServerTrusted( chain, null ) );
        assertEquals( "Certificate not yet valid!", ice.getMessage() );
    }
    
    @Test
    public void notAfterWrong() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.bar" );
        when( cert.getNotBefore() ).thenReturn( Date.from( LocalDate.now().minusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getNotAfter() ).thenReturn( Date.from( LocalDate.now().minusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> vtm.checkServerTrusted( chain, null ) );
        assertEquals( "Certificate has expired!", ice.getMessage() );
    }
    
    @Test
    public void newCertificate() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.bar" );
        when( cert.getNotBefore() ).thenReturn( Date.from( LocalDate.now().minusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getNotAfter() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        when( options.getTrustedCertificateSupplier() ).thenReturn( supplier );
        when( supplier.get( "foo.bar", 1965 ) ).thenReturn( null );
        when( options.getTrustedCertificateConsumer() ).thenReturn( consumer );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        vtm.checkServerTrusted( chain, null );
    }
    
    @Test
    public void renewalCheckCorrect() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.bar" );
        when( cert.getNotBefore() ).thenReturn( Date.from( LocalDate.now().minusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getNotAfter() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        when( options.getTrustedCertificateSupplier() ).thenReturn( supplier );
        when( supplier.get( "foo.bar", 1965 ) ).thenReturn( tc );
        when( tc.getFingerPrint() ).thenReturn( "foo" );
        when( options.isCertificateRenewalCheckEnabled() ).thenReturn( true );
        when( options.getCertificateGracePeriod() ).thenReturn( 30 );
        when( tc.getExpiryDate() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        vtm.checkServerTrusted( chain, null );
    }
    
    @Test
    public void renewalCheckWrong() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.bar" );
        when( cert.getNotBefore() ).thenReturn( Date.from( LocalDate.now().minusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getNotAfter() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        when( options.getTrustedCertificateSupplier() ).thenReturn( supplier );
        when( supplier.get( "foo.bar", 1965 ) ).thenReturn( tc );
        when( tc.getFingerPrint() ).thenReturn( "foo" );
        when( options.isCertificateRenewalCheckEnabled() ).thenReturn( true );
        when( options.getCertificateGracePeriod() ).thenReturn( 30 );
        when( tc.getExpiryDate() ).thenReturn( Date.from( LocalDate.now().plusDays( 100 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        InvalidCertificateException ice =
            assertThrows( InvalidCertificateException.class,
                () -> vtm.checkServerTrusted( chain, null ) );
        assertEquals( "Certificate renewal out of grace period. Potential " +
            "MITM attack!", ice.getMessage() );
    }
    
    @Test
    public void success() throws CertificateEncodingException
    {
        when( cert.getSubjectX500Principal() ).thenReturn( principal );
        when( principal.getName() ).thenReturn( "CN=foo.bar" );
        when( cert.getNotBefore() ).thenReturn( Date.from( LocalDate.now().minusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getNotAfter() ).thenReturn( Date.from( LocalDate.now().plusDays( 10 ).atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() ) );
        when( cert.getEncoded() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d".getBytes() );
        when( options.getTrustedCertificateSupplier() ).thenReturn( supplier );
        when( supplier.get( "foo.bar", 1965 ) ).thenReturn( tc );
        when( tc.getFingerPrint() ).thenReturn(
            "e04fd020ea3a9610a2d808002b30309d" );
        ValidationTrustManager vtm = new ValidationTrustManager( "foo.bar",
            1965, options );
        X509Certificate[] chain = { ( cert ) };
        
        vtm.checkServerTrusted( chain, null );
    }
    
}
