package com.github.argsfxs.deneb.exception;

public class CertificateValidationException extends RuntimeException
{
    public CertificateValidationException( String reason )
    {
        super( reason );
    }
}