package net.mguenther.idem.sequence;

import java.util.Locale;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class OutOfSequenceNumbersException extends RuntimeException {

    private static final String ERROR_MESSAGE = "The ID generator ran out of sequence numbers for requested timestamp " +
                                                "%s and gave up after %s milliseconds waiting.";

    public OutOfSequenceNumbersException(final long forRequestedTime,
                                         final long waitingTime) {
        super(String.format(Locale.getDefault(), ERROR_MESSAGE, forRequestedTime, waitingTime));
    }
}
