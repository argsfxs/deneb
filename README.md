# Deneb

A Java client core for [Project Gemini](https://geminiprotocol.net/) that can be used to build sophisticated clients (for example, browsers).

## Usage

### Sending a request

~~~java
RequestOptions options = new RequestOptions();
        
 GeminiRequest request = new GeminiRequestBuilder( "geminiprotocol.net", options )
    .withPath( "docs/faq.gmi" )
    .build();
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
~~~

### Options

The following options can be specified per request:

| Option                         | Default value | Description                                                                                 |
|--------------------------------|---------------|---------------------------------------------------------------------------------------------|
| validationEnabled              | false         | Enables validation of the server certificate                                                |
| trustedCertificateSupplier     | null          | Sets a supplier to obtain known server certificates                                         |
| trustedCertificateConsumer     | null          | Sets a consumer to store known server certificates                                          |
| certificateRenewalCheckEnabled | false         | Enables the check that a known server certificate is renewed within a grace period          |
| certificateGracePeriod         | 90            | Defines the number of days a known server certificate can be renewed before its expiry date |