package com.github.argsfxs.deneb;

import com.github.argsfxs.deneb.options.RequestOptions;
import com.github.argsfxs.deneb.request.GeminiRequest;
import com.github.argsfxs.deneb.request.GeminiRequestBuilder;
import com.github.argsfxs.deneb.response.GeminiResponse;
import com.github.argsfxs.deneb.response.GeminiSuccessResponse;
import com.github.argsfxs.deneb.security.TrustedCertificate;
import com.github.argsfxs.deneb.security.TrustedCertificateConsumer;
import com.github.argsfxs.deneb.security.TrustedCertificateSupplier;

import java.io.*;
import java.util.HashMap;

public class Main
{
    private static final TrustedCertificateConsumer tcc = trustedCertificate -> {
        HashMap<Object, Object> certMap = new HashMap<>();
        certMap.put( String.format( "%s:%d", trustedCertificate.getHost(),
            trustedCertificate.getPort() ), trustedCertificate );
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream( "db" +
                ".obj" );
            ObjectOutputStream objectOutputStream
                = new ObjectOutputStream( fileOutputStream );
            objectOutputStream.writeObject( certMap );
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    };
    
    private static final TrustedCertificateSupplier tcs = ( hostname, port ) -> {
        try
        {
            FileInputStream fileInputStream = new FileInputStream( "db.obj" );
            ObjectInputStream objectInputStream
                = new ObjectInputStream( fileInputStream );
            HashMap<String, TrustedCertificate> certMap = ( HashMap<String,
                TrustedCertificate> ) objectInputStream.readObject();
            objectInputStream.close();
            return certMap.get( String.format( "%s:%d", hostname, port ) );
        }
        catch ( ClassNotFoundException | IOException e )
        {
            return null;
        }
    };
    
    
    public static void main( String[] args ) throws IOException
    {
        new File( "db.obj" ).createNewFile();
        RequestOptions options = new RequestOptions();
        options.setValidationEnabled( true );
        options.setCertificateRenewalCheckEnabled( true );
        options.setTrustedCertificateConsumer( tcc );
        options.setTrustedCertificateSupplier( tcs );
        
        GeminiRequest request = new GeminiRequestBuilder( "geminiprotocol" +
            ".net", options ).withPath( "docs/gemtext-specification.gmi" ).build();
        GeminiResponse response = request.send();
        
        System.out.println( response.getHeader() );
        System.out.println( response.getStatus() );
        System.out.println();
        if ( response instanceof GeminiSuccessResponse )
        {
            BufferedReader in = new BufferedReader(
                new InputStreamReader( ( ( GeminiSuccessResponse ) response ).getContent() ) );
            String line;
            while ( ( line = in.readLine() ) != null )
            {
                System.out.println( line );
            }
        }
        
    }
}
