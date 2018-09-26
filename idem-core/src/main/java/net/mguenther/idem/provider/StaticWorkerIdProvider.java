package net.mguenther.idem.provider;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class StaticWorkerIdProvider implements WorkerIdProvider {

    private final byte[] workerId;

    public StaticWorkerIdProvider(final String source) {
        try {
            workerId = workerId(source);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getWorkerId() {
        return workerId;
    }

    private byte[] workerId(final String source) throws NoSuchAlgorithmException {
        final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        return sha1.digest(source.getBytes(Charset.forName("utf-8")));
    }
}
