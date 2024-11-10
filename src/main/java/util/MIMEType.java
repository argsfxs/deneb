package util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represent the MIME type returned by the server in the status header of a success
 * response.
 */
public class MIMEType
{
    private final String mediaType;
    
    private final String subType;
    
    private final String typeString;
    
    private final Map<String, String> parameters;
    
    private MIMEType( String mediaType, String subType, String typeString,
        Map<String, String> parameters )
    {
        this.mediaType = mediaType;
        this.subType = subType;
        this.typeString = typeString;
        this.parameters = parameters;
    }
    
    /**
     * Parses the type returned by the server.
     *
     * @param typeString the type as returned in the status header
     * @return the MIMEType object or <code>null</code> if the input is invalid
     */
    public static MIMEType from( String typeString )
    {
        if ( StringUtils.isEmpty( typeString ) )
        {
            return null;
        }
        String[] typeSegments = StringUtils.split( typeString, "/" );
        if ( typeSegments.length != 2 )
        {
            return null;
        }
        List<String> parameterSegments =
            Arrays.stream( StringUtils.split( typeSegments[ 1 ], ';' ) ).collect(
                Collectors.toList() );
        Map<String, String> parameterMap = new HashMap<>();
        if ( parameterSegments.size() > 1 )
        {
            for ( String parameter :
                parameterSegments.stream().filter( s -> s.contains( "=" ) )
                                 .collect(
                                     Collectors.toList() ) )
            {
                String[] keyValue = StringUtils.split( parameter, "=" );
                if ( keyValue.length == 2 )
                {
                    parameterMap.put( keyValue[ 0 ].trim(),
                        cleanValue( keyValue[ 1 ] ) );
                }
            }
            
        }
        return new MIMEType( typeSegments[ 0 ],
            StringUtils.substringBefore( typeSegments[ 1 ], ";" ), typeString,
            parameterMap );
    }
    
    private static String cleanValue( String value )
    {
        if ( value.length() < 2 )
        {
            return value;
        }
        if ( value.charAt( 0 ) == '"' && value.charAt( value.length() - 1 ) == '"' )
        {
            String cleaned = StringUtils.removeStart( value, "\"" );
            cleaned = StringUtils.removeEnd( cleaned, "\"" );
            return cleaned;
        }
        if ( value.contains( " " ) )
        {
            String[] segments = value.split( " " );
            return segments[ 0 ];
        }
        return value;
    }
    
    /**
     * Returns the media type (first component) of the MIME type.
     *
     * @return the media type
     */
    public String getMediaType()
    {
        return mediaType;
    }
    
    /**
     * Returns the subtype (second component) of the MIME type.
     *
     * @return the subtype
     */
    public String getSubType()
    {
        return subType;
    }
    
    /**
     * Returns a map of optional parameters of the MIME type.
     *
     * @return the parameter map
     */
    public Map<String, String> getParameters()
    {
        return parameters;
    }
    
    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        MIMEType mimeType = ( MIMEType ) o;
        return Objects.equals( typeString, mimeType.typeString );
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash( typeString );
    }
    
    @Override
    public String toString()
    {
        return typeString;
    }
}
