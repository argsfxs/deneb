package com.github.argsfxs.deneb.exception;

public class SocketIOException extends RuntimeException
{
    public SocketIOException( Exception e )
    {
        super( e );
    }
}