package com.github.argsfxs.deneb.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MIMETypeTest
{
    @Test
    public void testSuccess()
    {
        MIMEType m = MIMEType.from( "text/plain" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain", m.toString() );
        assertEquals( 0, m.getParameters().size() );
        
        m = MIMEType.from( "text/plain;charset=us-ascii" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain;charset=us-ascii", m.toString() );
        assertEquals( 1, m.getParameters().size() );
        assertEquals( "us-ascii", m.getParameters().get( "charset" ) );
        
        m = MIMEType.from( "text/plain;   charset=us-ascii" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain;   charset=us-ascii", m.toString() );
        assertEquals( 1, m.getParameters().size() );
        assertEquals( "us-ascii", m.getParameters().get( "charset" ) );
        
        m = MIMEType.from( "text/plain; charset=\"us-ascii\"" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain; charset=\"us-ascii\"", m.toString() );
        assertEquals( 1, m.getParameters().size() );
        assertEquals( "us-ascii", m.getParameters().get( "charset" ) );
        
        m = MIMEType.from( "text/plain; charset=us-ascii (Plain text)" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain; charset=us-ascii (Plain text)",
            m.toString() );
        assertEquals( 1, m.getParameters().size() );
        assertEquals( "us-ascii", m.getParameters().get( "charset" ) );
        
        m = MIMEType.from( "text/plain; charset=\"us-ascii (Plain text)\"" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain; charset=\"us-ascii (Plain text)\"",
            m.toString() );
        assertEquals( 1, m.getParameters().size() );
        assertEquals( "us-ascii (Plain text)", m.getParameters().get(
            "charset" ) );
        
        m = MIMEType.from( "text/plain; charset=us-ascii (Plain text); " +
            "lang=en (English)" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain; charset=us-ascii (Plain text); lang=en " +
            "(English)", m.toString() );
        assertEquals( 2, m.getParameters().size() );
        assertEquals( "us-ascii", m.getParameters().get( "charset" ) );
        assertEquals( "en", m.getParameters().get( "lang" ) );
        
        m = MIMEType.from( "text/plain; charset=\"us-ascii (Plain text)\"; " +
            "lang=\"en (English)\"" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "plain", m.getSubType() );
        assertEquals( "text/plain; charset=\"us-ascii (Plain text)\"; " +
            "lang=\"en (English)\"", m.toString() );
        assertEquals( 2, m.getParameters().size() );
        assertEquals( "us-ascii (Plain text)", m.getParameters().get(
            "charset" ) );
        assertEquals( "en (English)", m.getParameters().get( "lang" ) );
        
        m = MIMEType.from( "text/gemini; lang=en; charset=UTF-8" );
        assertEquals( "text", m.getMediaType() );
        assertEquals( "gemini", m.getSubType() );
        assertEquals( "text/gemini; lang=en; charset=UTF-8", m.toString() );
        assertEquals( 2, m.getParameters().size() );
        assertEquals( "en", m.getParameters().get( "lang" ) );
        assertEquals( "UTF-8", m.getParameters().get( "charset" ) );
    }
    
    @Test
    public void testWrongInput()
    {
        MIMEType m = MIMEType.from( null );
        assertNull( m );
        
        m = MIMEType.from( "" );
        assertNull( m );
        
        m = MIMEType.from( " " );
        assertNull( m );
        
        m = MIMEType.from( "foo-bar" );
        assertNull( m );
    }
    
    @Test
    public void testEquals()
    {
        MIMEType a = MIMEType.from( "text/plain; charset=us-ascii" );
        MIMEType b = MIMEType.from( "text/plain; charset=us-ascii" );
        assertEquals( a, b );
    }
    
}
