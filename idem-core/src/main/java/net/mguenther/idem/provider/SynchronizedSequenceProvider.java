package net.mguenther.idem.provider;

import net.mguenther.idem.SequenceProvider;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class SynchronizedSequenceProvider implements SequenceProvider {

    private final Object lock = new Object();
    private long time;
    private int count;

    public SynchronizedSequenceProvider() {
        this(Long.MIN_VALUE, Integer.MAX_VALUE);
    }

    private SynchronizedSequenceProvider(final long time, final int count) {
        this.time = time;
        this.count = count;
    }

    @Override
    public byte[] nextSequenceNumber(final long currentTime) {
        synchronized (lock) {
            updateCounter(currentTime);
            return counterToByteArray();
        }
    }

    private void updateCounter(final long currentTime) {
        if (time < currentTime) {
            this.time = currentTime;
            this.count = Integer.MIN_VALUE;
        } else if (time == currentTime) {
            this.time = currentTime;
            this.count++;
        } else {
            throw new IllegalStateException("Time cannot flow backwards.");
        }
    }

    private byte[] counterToByteArray() {
        final byte[] bytes = new byte[2];
        bytes[0] = (byte) (count & 0xFF);
        bytes[1] = (byte) ((count >> 8) & 0xFF);
        return bytes;
    }
}
