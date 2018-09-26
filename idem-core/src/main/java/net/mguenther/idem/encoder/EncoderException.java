package net.mguenther.idem.encoder;

/**
 * This exception must be used to indicate an error situation that occured while attempting
 * to encode an input into a value of its designated output type.
 */
public class EncoderException extends RuntimeException {

    public EncoderException(final String message) {
        super(message);
    }

    public EncoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
