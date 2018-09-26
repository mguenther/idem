package net.mguenther.idem.sequence;

import net.mguenther.idem.Wait;
import net.mguenther.idem.provider.TimeProvider;

/**
 * {@code Sequence} implements a mechanism that generates unique sequence numbers for a
 * dedicated time slot (aka tick), where each tick represents a millisecond since the Unix epoch.
 * {@code Sequence} guarantees that it only hands out sequence number that are unique
 * within its own context.
 *
 * This means, as idem is coordination-free, be sure to share the {@code Sequence} between
 * multiple {@code IdGenerator}s, otherwise you could potentially introduce duplicate numbers.
 *
 * In order to guarantee uniqueness of sequence numbers per worker ID (cf. {@code WorkerIdProvider}
 * you should instantiate {@code Sequence} directly, but rather go through the
 * {@code SequenceFactory}, which ensures that idem isolates multiple instances of
 * {@code Sequence} based on the worker ID that the enclosing {@code IdGenerator} uses.
 */
public final class Sequence {

    private final Object lock = new Object();

    private final TimeProvider timeProvider;

    private final int maxPoolNumbersPerTick;

    private final int maxWaitTime;

    private long tick;

    private int count;

    /**
     * !!! Do not use this constructor in client code. Use {@code SequenceFactory} instead !!!
     */
    Sequence(final SequenceConfig config) {
        this.timeProvider = config.getTimeProvider();
        this.maxPoolNumbersPerTick = config.getMaxPoolNumbersPerTick();
        this.maxWaitTime = config.getMaxWaitTimeInMillis();
        this.tick = Long.MIN_VALUE;
        this.count = 0;
    }

    /**
     * Generates the next unique sequence number.
     *
     * @return
     *      {@code byte[]} representing the next sequence number
     */
    public SequenceNumber nextSequenceNumber() {
        synchronized (lock) {
            final long tick = updateCounter();
            return new SequenceNumber(tick, sequenceToByteArray());
        }
    }

    private long updateCounter() {
        long currentTick = timeProvider.getTimestamp();
        if (tick < currentTick) {
            this.tick = currentTick;
            this.count = 0;
        } else if (tick == currentTick) {
            if (isSequenceNumberPoolExhausted()) {
                currentTick = waitForNextTick(currentTick);
            }
            this.count++;
        } else {
            throw new BackwardsClockDriftException(currentTick, tick);
        }
        return currentTick;
    }

    private boolean isSequenceNumberPoolExhausted() {
        return count == maxPoolNumbersPerTick;
    }

    private long waitForNextTick(final long initialCurrentTime) {
        long updatedTick = initialCurrentTime;
        int wait = 0;
        while (updatedTick <= tick) {
            if (wait >= maxWaitTime) throw new OutOfSequenceNumbersException(initialCurrentTime, maxWaitTime);
            delay();
            updatedTick = timeProvider.getTimestamp();
            wait++;
        }
        return updatedTick;
    }

    private void delay() {
        try {
            Wait.delay(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private byte[] sequenceToByteArray() {
        final byte[] bytes = new byte[2];
        bytes[1] = (byte) (count & 0xFF);
        bytes[0] = (byte) ((count >> 8) & 0xFF);
        return bytes;
    }
}
