package exception;

public class SocketIOException extends RuntimeException
{
    public SocketIOException( Exception e )
    {
        super( e );
    }
}