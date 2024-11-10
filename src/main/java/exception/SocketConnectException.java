package exception;

public class SocketConnectException extends RuntimeException
{
    public SocketConnectException( Exception e )
    {
        super( e );
    }
}