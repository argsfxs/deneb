package com.github.argsfxs.deneb.request;

import com.github.argsfxs.deneb.exception.URILengthExceededException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GeminiRequestBuilderTest
{
    @Test
    public void test()
    {
        String url = new GeminiRequestBuilder( "milky.way" )
            .withPath( "/bar" )
            .withPort( 1337 )
            .withScheme( "foo" )
            .withQueryString( "launch sequence initiated" )
            .buildUrl();
        assertEquals( "foo://milky.way/bar?launch%20sequence%20initiated",
            url );
        
        url = new GeminiRequestBuilder( "milky.way" )
            .withPath( "bar" )
            .withPort( 1337 )
            .withScheme( "foo" )
            .withQueryString( "launch\r\nsequence\r\ninitiated" )
            .buildUrl();
        assertEquals( "foo://milky.way/bar?launch%0D%0Asequence%0D" +
            "%0Ainitiated", url );
        
        url = new GeminiRequestBuilder( "milky.way" )
            .withPath( "" )
            .withPort( 1337 )
            .withScheme( "foo" )
            .buildUrl();
        assertEquals( "foo://milky.way/", url );
    }
    
    @Test
    public void testUrlTooLong()
    {
        GeminiRequestBuilder builder = new GeminiRequestBuilder( "milky.way" )
            .withPath( "/bar" )
            .withPort( 1337 )
            .withScheme( "foo" )
            .withQueryString( "Lorem ipsum dolor sit amet, consectetuer " +
                "adipiscing elit. Aenean commodo ligula eget dolor. Aenean " +
                "massa. Cum sociis natoque penatibus et magnis dis parturient" +
                " montes, nascetur ridiculus mus. Donec quam felis, ultricies" +
                " nec, pellentesque eu, pretium quis, sem. Nulla consequat " +
                "massa quis enim. Donec pede justo, fringilla vel, aliquet " +
                "nec, vulputate eget, arcu. In enim justo, rhoncus ut, " +
                "imperdiet a, venenatis vitae, justo. Nullam dictum felis eu " +
                "pede mollis pretium. Integer tincidunt. Cras dapibus. " +
                "Vivamus elementum semper nisi. Aenean vulputate eleifend " +
                "tellus. Aenean leo ligula, porttitor eu, consequat vitae, " +
                "eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra " +
                "quis, feugiat a, tellus. Phasellus viverra nulla ut metus " +
                "varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam " +
                "ultricies nisi vel augue. Curabitur ullamcorper ultricies " +
                "nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus " +
                "eget condimentum rhoncus, sem quam semper libero, sit amet " +
                "adipiscing sem neque sed ipsum." );
        assertThrows( URILengthExceededException.class, builder::buildUrl );
    }
}
