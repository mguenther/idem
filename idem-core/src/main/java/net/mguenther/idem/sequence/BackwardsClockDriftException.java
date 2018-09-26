package net.mguenther.idem.sequence;

import java.util.Locale;

public class BackwardsClockDriftException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Detected a backwards clock drift. Requested sequence number for " +
                                                "timestamp %s, but last timestamp was already at %s. The clock " +
                                                "seems to have drifted backwards by %s ms.";

    public BackwardsClockDriftException(final long currentTime,
                                        final long lastTime) {
        super(String.format(Locale.getDefault(), ERROR_MESSAGE, currentTime, lastTime, (lastTime - currentTime)));
    }
}
