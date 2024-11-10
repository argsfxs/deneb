package security;

/**
 * <p>This class represents the supplier of a trusted certificate.</p>
 * <p>Per the specification, clients are supposed to use a TOFU approach (trust on first use)
 * when connecting to a new server. However, subsequent connections should validate the certificate
 * to prevent man-in-the-middle attacks.</p>
 * <p>The supplier must implement logic retrieve the known {@link TrustedCertificate} objects, e
 * .g. query them from a database.</p>
 */
@FunctionalInterface
public interface TrustedCertificateSupplier
{
    /**
     * Retrieves a trusted certificate.
     *
     * @param hostname the server hostname
     * @param port     the server port
     * @return the stored trusted certificate
     */
    TrustedCertificate get( String hostname, int port );
}
