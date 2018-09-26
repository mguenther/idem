package net.mguenther.idem.sequence;

import net.mguenther.idem.provider.TimeProvider;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.mguenther.idem.sequence.SequenceConfig.sequenceProviderConfig;
import static net.mguenther.idem.sequence.SequenceConfig.useDefaults;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class SequenceTest {

    @Test
    public void nextSequenceNumberShouldIncreaseSequenceNumberLinearly() {

        final Sequence provider = new Sequence(useDefaults(65536));
        final Map<Long, List<SequenceNumber>> groupedByTick = Stream.iterate(0, i -> i + 1)
                .limit(100)
                .map(i -> provider.nextSequenceNumber())
                .collect(Collectors.groupingBy(SequenceNumber::getTimestamp));

        assertFalse(groupedByTick.isEmpty());

        for (Long tick : groupedByTick.keySet()) {
            final List<SequenceNumber> numbers = groupedByTick.get(tick);
            assertFalse(numbers.isEmpty());
            int expectedSequenceNumber = 0;
            for (SequenceNumber number : numbers) {
                assertThat(number.getSequenceNumber(), is(toByteArray(expectedSequenceNumber)));
                expectedSequenceNumber++;
            }
        }
    }

    @Test(expected = OutOfSequenceNumbersException.class)
    public void nextSequenceNumberShouldThrowOutOfSequenceNumbersExceptionIfPoolIsExhausted() {

        final SequenceConfig config = sequenceProviderConfig(1)
                .withMaxWaitTime(0, TimeUnit.MILLISECONDS)
                .build();
        final Sequence provider = new Sequence(config);

        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
    }

    @Test(expected = BackwardsClockDriftException.class)
    public void nextSequenceNumberShouldThrowBackwardsClockDriftExceptionIfDriftIsDetected() {

        final SequenceConfig config = SequenceConfig.sequenceProviderConfig(65536)
                .withTimeProvider(new DriftingTimeProvider())
                .build();
        final Sequence provider = new Sequence(config);

        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
    }

    private byte[] toByteArray(final int expectedSequenceNumber) {
        final byte[] bytes = new byte[2];
        bytes[1] = (byte) (expectedSequenceNumber & 0xFF);
        bytes[0] = (byte) ((expectedSequenceNumber>> 8) & 0xFF);
        return bytes;
    }

    class DriftingTimeProvider implements TimeProvider {

        boolean firstCall = true;

        @Override
        public long getTimestamp() {

            if (firstCall) {
                firstCall = false;
                return System.currentTimeMillis();
            } else {
                return System.currentTimeMillis() - 1000;
            }
        }
    }
}
