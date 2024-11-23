package com.github.argsfxs.deneb.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest
{
    @Test
    public void allValues()
    {
        String values =
            Arrays.stream( Status.values() ).map( status -> String.valueOf( status.getCode() ) )
                  .collect(
                      Collectors.joining( "+" ) );
        assertEquals( "10+11+20+30+31+40+41+42+43+44+50+51+52+53+59+60+61+62" +
            "+0", values );
    }
    
    @Test
    public void fromHeaderTooShort()
    {
        assertEquals( Status.INVALID, Status.fromHeader( "1" ) );
    }
    
    @Test
    public void fromHeaderNFE()
    {
        assertEquals( Status.INVALID, Status.fromHeader( "foo" ) );
    }
    
    @Test
    public void fromHeaderGreater69()
    {
        assertEquals( Status.INVALID, Status.fromHeader( "70" ) );
    }
    
    @Test
    public void fromHeaderSuccess()
    {
        assertEquals( Status.SUCCESS, Status.fromHeader( "20" ) );
    }
    
    @Test
    public void fromCode()
    {
        for ( int i = 10; i <= 69; i++ )
        {
            switch ( i )
            {
                case 10:
                    assertStatus( 10, Status.INPUT_EXPECTED );
                case 11:
                    assertStatus( 11, Status.SENSITIVE_INPUT );
                case 12:
                    assertStatus( 12, Status.INPUT_EXPECTED );
                case 13:
                    assertStatus( 13, Status.INPUT_EXPECTED );
                case 14:
                    assertStatus( 14, Status.INPUT_EXPECTED );
                case 15:
                    assertStatus( 15, Status.INPUT_EXPECTED );
                case 16:
                    assertStatus( 16, Status.INPUT_EXPECTED );
                case 17:
                    assertStatus( 17, Status.INPUT_EXPECTED );
                case 18:
                    assertStatus( 18, Status.INPUT_EXPECTED );
                case 19:
                    assertStatus( 19, Status.INPUT_EXPECTED );
                case 20:
                    assertStatus( 20, Status.SUCCESS );
                case 21:
                    assertStatus( 21, Status.SUCCESS );
                case 22:
                    assertStatus( 22, Status.SUCCESS );
                case 23:
                    assertStatus( 23, Status.SUCCESS );
                case 24:
                    assertStatus( 24, Status.SUCCESS );
                case 25:
                    assertStatus( 25, Status.SUCCESS );
                case 26:
                    assertStatus( 26, Status.SUCCESS );
                case 27:
                    assertStatus( 27, Status.SUCCESS );
                case 28:
                    assertStatus( 28, Status.SUCCESS );
                case 29:
                    assertStatus( 29, Status.SUCCESS );
                case 30:
                    assertStatus( 30, Status.TEMPORARY_REDIRECTION );
                case 31:
                    assertStatus( 31, Status.PERMANENT_REDIRECTION );
                case 32:
                    assertStatus( 32, Status.TEMPORARY_REDIRECTION );
                case 33:
                    assertStatus( 33, Status.TEMPORARY_REDIRECTION );
                case 34:
                    assertStatus( 34, Status.TEMPORARY_REDIRECTION );
                case 35:
                    assertStatus( 35, Status.TEMPORARY_REDIRECTION );
                case 36:
                    assertStatus( 36, Status.TEMPORARY_REDIRECTION );
                case 37:
                    assertStatus( 37, Status.TEMPORARY_REDIRECTION );
                case 38:
                    assertStatus( 38, Status.TEMPORARY_REDIRECTION );
                case 39:
                    assertStatus( 39, Status.TEMPORARY_REDIRECTION );
                case 40:
                    assertStatus( 40, Status.TEMPORARY_FAILURE );
                case 41:
                    assertStatus( 41, Status.SERVER_UNAVAILABLE );
                case 42:
                    assertStatus( 42, Status.CGI_ERROR );
                case 43:
                    assertStatus( 43, Status.PROXY_ERROR );
                case 44:
                    assertStatus( 44, Status.SLOW_DOWN );
                case 45:
                    assertStatus( 45, Status.TEMPORARY_FAILURE );
            }
        }
    }
    
    private void assertStatus( int code, Status expected )
    {
        assertEquals( Status.fromCode( code ), expected );
    }
}
