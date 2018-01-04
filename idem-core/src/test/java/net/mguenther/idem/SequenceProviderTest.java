package net.mguenther.idem;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.mguenther.idem.SequenceProviderConfig.sequenceProviderConfig;
import static net.mguenther.idem.SequenceProviderConfig.useDefaults;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class SequenceProviderTest {

    @Test
    public void nextSequenceNumberShouldIncreaseSequenceNumberLinearly() {

        final SequenceProvider provider = new SequenceProvider(useDefaults(65536));
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

        final SequenceProviderConfig config = sequenceProviderConfig(1)
                .withMaxWaitTime(0, TimeUnit.MILLISECONDS)
                .build();
        final SequenceProvider provider = new SequenceProvider(config);

        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
    }

    @Test(expected = BackwardsClockDriftException.class)
    public void nextSequenceNumberShouldThrowBackwardsClockDriftExceptionIfDriftIsDetected() {

        final SequenceProviderConfig config = SequenceProviderConfig.sequenceProviderConfig(65536)
                .withTimeProvider(new DriftingTimeProvider())
                .build();
        final SequenceProvider provider = new SequenceProvider(config);

        provider.nextSequenceNumber();
        provider.nextSequenceNumber();
    }

    private byte[] toByteArray(final int expectedSequenceNumber) {
        final byte[] bytes = new byte[2];
        bytes[0] = (byte) (expectedSequenceNumber & 0xFF);
        bytes[1] = (byte) ((expectedSequenceNumber>> 8) & 0xFF);
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
