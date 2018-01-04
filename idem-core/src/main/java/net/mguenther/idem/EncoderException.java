package net.mguenther.idem;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class EncoderException extends RuntimeException {

    public EncoderException(final String message) {
        super(message);
    }

    public EncoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
