package net.mguenther.idem.flake;

import net.mguenther.idem.encoder.LongEncoder;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import net.mguenther.idem.provider.TimeProvider;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Flake64Test {

    private static final long MAX_DEVIATION_ALLOWED = 10L;

    private final TimeProvider timeProvider = new LinearTimeProvider();

    // we use a Long-based implementation of the Flake64 ID generator, but
    // the tests in this class assert that properties that are common to all
    // Flake64-based implementations are correct, as all alignment tests
    // operator directly on the bits of the generated IDs
    private Flake64<Long> flake;

    @Before
    public void prepareTest() {
        flake = new Flake64L(
                timeProvider,
                new StaticWorkerIdProvider("ASvmcvljs=!"), // this worker ID has its LSB set to 1
                new LongEncoder());
    }

    @Test
    public void timeSegmentShouldBeCorrectlyAligned() {
        // we check alignment by extracting the timestamp part from the generated ID
        // and compare the timestamp with what the TimeProvider yields. we expect that
        // both the newly obtained timestamp and the timestamp from the ID be within
        // milliseconds of each other and test for a maximum difference of
        // MAX_DEVIATION_ALLOWED. this would not be case if the segment would not be
        // at the proper place (bit-wise) in the generated ID
        long id = flake.nextId();
        long now  = timeProvider.getTimestamp() & Flake64.TIMESTAMP_LOWER_BITS;
        long timestampFromId = id >> 22;
        assertTrue(now - timestampFromId < MAX_DEVIATION_ALLOWED);
    }

    @Test
    public void sequenceNumberShouldBeCorrectlyAligned() {
        // we check if the lower bits that are reserved for the sequence number are
        // zero (the first sequence number for a time slot ms-wise). if the sequence
        // number part would be overlapping with any of the other segments, then this
        // would not be zero if we use a worker ID for which the encoded worker ID
        // has its LSB set to 1 (as is the case with the worker ID used in this test)
        long id = flake.nextId();
        long sequenceNumber = (id & Flake64.SEQUENCE_LOWER_BITS);
        assertThat(sequenceNumber, is(0L));
    }
}
