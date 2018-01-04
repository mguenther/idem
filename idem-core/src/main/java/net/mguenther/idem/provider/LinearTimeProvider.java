package net.mguenther.idem.provider;

import net.mguenther.idem.TimeProvider;

import java.util.stream.Stream;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class LinearTimeProvider implements TimeProvider {

    private static final int DEFAULT_MEAN_TIME_OFFSET_ACCURACY = 10;

    private static final int NANOSECONDS_TO_MILLISECONDS = 1_000_000;

    private final long meanTimeOffset;

    public LinearTimeProvider() {
        this(DEFAULT_MEAN_TIME_OFFSET_ACCURACY);
    }

    public LinearTimeProvider(final int accuracyOfMeanTimeOffset) {
        meanTimeOffset = meanTimeOffset(accuracyOfMeanTimeOffset);
    }

    private long meanTimeOffset(int steps) {
        if (steps < 1) {
            throw new IllegalArgumentException("Given value for steps (" + steps + ") is not in admissible range [1,*).");
        }
        final double meanTimeOffset = Stream.iterate(timeOffsetEstimate(), n -> timeOffsetEstimate())
                .limit(steps)
                .map(Long::doubleValue)
                .reduce(0.0, (l, r) -> l + r) / steps;
        return (long) meanTimeOffset;
    }

    private Long timeOffsetEstimate() {
        return System.currentTimeMillis() * NANOSECONDS_TO_MILLISECONDS - System.nanoTime();
    }

    @Override
    public long getTimestamp() {
        return fromLinearTime(meanTimeOffset);
    }

    private long fromLinearTime(final long offset) {
        return offset + (System.nanoTime() / NANOSECONDS_TO_MILLISECONDS);
    }
}
