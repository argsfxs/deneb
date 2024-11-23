package com.github.argsfxs.deneb.security;

import java.util.function.Consumer;

/**
 * <p>This class represents the consumer of a trusted certificate.</p>
 * <p>Per the specification, clients are supposed to use a TOFU approach (trust on first use)
 * when connecting to a new server. However, subsequent connections should validate the certificate
 * to prevent man-in-the-middle attacks.</p>
 * <p>The consumer must implement logic handle new {@link TrustedCertificate} objects, e.g. store
 * them in a database.</p>
 */
@FunctionalInterface
public interface TrustedCertificateConsumer extends Consumer<TrustedCertificate>
{
    /**
     * Stores a trusted certificate.
     *
     * @param trustedCertificate the valid certificate to store
     */
    @Override
    void accept( TrustedCertificate trustedCertificate );
}
