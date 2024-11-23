package com.github.argsfxs.deneb.exception;

public class SocketConnectException extends RuntimeException
{
    public SocketConnectException( Exception e )
    {
        super( e );
    }
}