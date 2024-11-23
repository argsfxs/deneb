package com.github.argsfxs.deneb.response;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.github.argsfxs.deneb.util.Status.*;
import static org.junit.jupiter.api.Assertions.*;

public class GeminiResponseFactoryTest
{
    
    @Test
    public void testInput()
    {
        String input = "10 provide user name\r\n";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "10 provide user name", response.getHeader() );
        assertEquals( INPUT_EXPECTED, response.getStatus() );
        assertInstanceOf( GeminiInputResponse.class, response );
        assertEquals( "provide user name",
            ( ( GeminiInputResponse ) response ).getPrompt() );
        
        // no prompt
        
        input = "10\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "10", response.getHeader() );
        assertEquals( INPUT_EXPECTED, response.getStatus() );
        assertInstanceOf( GeminiInputResponse.class, response );
        assertNull( ( ( GeminiInputResponse ) response ).getPrompt() );
    }
    
    @Test
    public void testSuccess() throws IOException
    {
        String input = "20 text/plain\r\nfoo bar baz";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "20 text/plain", response.getHeader() );
        assertEquals( SUCCESS, response.getStatus() );
        assertInstanceOf( GeminiSuccessResponse.class, response );
        assertEquals( "text/plain",
            ( ( GeminiSuccessResponse ) response ).getMimeType().toString() );
        BufferedReader br = new BufferedReader(
            new InputStreamReader( ( ( GeminiSuccessResponse ) response ).getContent() ) );
        String line = br.readLine();
        assertEquals( "foo bar baz", line );
        
        // no type and body
        
        input = "20\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "20", response.getHeader() );
        assertEquals( SUCCESS, response.getStatus() );
        assertInstanceOf( GeminiSuccessResponse.class, response );
        assertNull( ( ( GeminiSuccessResponse ) response ).getMimeType() );
        br = new BufferedReader(
            new InputStreamReader( ( ( GeminiSuccessResponse ) response ).getContent() ) );
        line = br.readLine();
        assertNull( line );
    }
    
    @Test
    public void testRedirect()
    {
        String input = "30 /new\r\n";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "30 /new", response.getHeader() );
        assertEquals( TEMPORARY_REDIRECTION, response.getStatus() );
        assertInstanceOf( GeminiRedirectResponse.class, response );
        assertEquals( "/new",
            ( ( GeminiRedirectResponse ) response ).getURI().toString() );
        
        // no URI
        
        input = "30\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "30", response.getHeader() );
        assertEquals( TEMPORARY_REDIRECTION, response.getStatus() );
        assertInstanceOf( GeminiRedirectResponse.class, response );
        assertNull( ( ( GeminiRedirectResponse ) response ).getURI() );
        
        // invalid URI
        
        input = "30 :foo\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "30 :foo", response.getHeader() );
        assertEquals( TEMPORARY_REDIRECTION, response.getStatus() );
        assertInstanceOf( GeminiRedirectResponse.class, response );
        assertNull( ( ( GeminiRedirectResponse ) response ).getURI() );
    }
    
    @Test
    public void testTempFail()
    {
        String input = "41 Undergoing maintenance at this time\r\n";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "41 Undergoing maintenance at this time",
            response.getHeader() );
        assertEquals( SERVER_UNAVAILABLE, response.getStatus() );
        assertInstanceOf( GeminiTempFailResponse.class, response );
        assertEquals( "Undergoing maintenance at this time",
            ( ( GeminiTempFailResponse ) response ).getErrorMessage().get() );
        
        // no message set
        
        input = "41\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "41", response.getHeader() );
        assertEquals( SERVER_UNAVAILABLE, response.getStatus() );
        assertInstanceOf( GeminiTempFailResponse.class, response );
        assertFalse( ( ( GeminiTempFailResponse ) response ).getErrorMessage().isPresent() );
    }
    
    @Test
    public void testPermFail()
    {
        String input = "59 Syntax Error\r\n";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "59 Syntax Error", response.getHeader() );
        assertEquals( BAD_REQUEST, response.getStatus() );
        assertInstanceOf( GeminiPermFailResponse.class, response );
        assertEquals( "Syntax Error",
            ( ( GeminiPermFailResponse ) response ).getErrorMessage().get() );
        
        // no message set
        
        input = "52\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "52", response.getHeader() );
        assertEquals( GONE, response.getStatus() );
        assertInstanceOf( GeminiPermFailResponse.class, response );
        assertFalse( ( ( GeminiPermFailResponse ) response ).getErrorMessage().isPresent() );
    }
    
    @Test
    public void testAuth()
    {
        String input = "62 Expired\r\n";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "62 Expired", response.getHeader() );
        assertEquals( CERTIFICATE_NOT_VALID, response.getStatus() );
        assertInstanceOf( GeminiAuthResponse.class, response );
        assertEquals( "Expired",
            ( ( GeminiAuthResponse ) response ).getErrorMessage().get() );
        
        // no message set
        
        input = "62\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "62", response.getHeader() );
        assertEquals( CERTIFICATE_NOT_VALID, response.getStatus() );
        assertInstanceOf( GeminiAuthResponse.class, response );
        assertFalse( ( ( GeminiAuthResponse ) response ).getErrorMessage().isPresent() );
    }
    
    @Test
    public void testInvalid()
    {
        String input = "70 foo\r\n";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "70 foo", response.getHeader() );
        assertEquals( INVALID, response.getStatus() );
        assertInstanceOf( GeminiErrorResponse.class, response );
        assertEquals( "foo",
            ( ( GeminiErrorResponse ) response ).getErrorMessage().get() );
        
        // no message set
        
        input = "70\r\n";
        in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        response = GeminiResponseFactory.create( in );
        
        assertEquals( "70", response.getHeader() );
        assertEquals( INVALID, response.getStatus() );
        assertInstanceOf( GeminiErrorResponse.class, response );
        assertFalse( ( ( GeminiErrorResponse ) response ).getErrorMessage().isPresent() );
    }
    
    @Test
    public void testNoCRLFInvalid()
    {
        String input = "10 foo";
        InputStream in =
            new ByteArrayInputStream( input.getBytes( StandardCharsets.UTF_8 ) );
        
        GeminiResponse response = GeminiResponseFactory.create( in );
        
        assertEquals( "10 foo", response.getHeader() );
        assertEquals( INPUT_EXPECTED, response.getStatus() );
        assertInstanceOf( GeminiInputResponse.class, response );
        assertEquals( "foo", ( ( GeminiInputResponse ) response ).getPrompt() );
    }
    
    
}
