package exception;

public class InvalidCertificateException extends RuntimeException
{
    public InvalidCertificateException( String reason )
    {
        super( reason );
    }
}