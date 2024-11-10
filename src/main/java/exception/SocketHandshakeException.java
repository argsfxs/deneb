package exception;

public class SocketHandshakeException extends RuntimeException
{
    public SocketHandshakeException( Exception e )
    {
        super( e );
    }
}